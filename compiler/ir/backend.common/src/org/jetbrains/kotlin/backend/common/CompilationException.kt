/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

class CompilationException(
    message: String,
    private val file: IrFile?,
    private val ir: IrHolder<Any>,
) : RuntimeException(
    message
) {
    override val message: String
        get() = "Back-end (JS): Please report this problem.\nProblem with `$content`.\nDetails: " + super.message

    val line: Int
        get() = file?.fileEntry?.getLineNumber((ir.value as? IrElement)?.startOffset ?: UNDEFINED_OFFSET)?.plus(1) ?: -1

    val column: Int
        get() = file?.fileEntry?.getColumnNumber((ir.value as? IrElement)?.startOffset ?: UNDEFINED_OFFSET)?.plus(1) ?: -1

    val path: String
        get() = file?.path ?: "<unknown>"

    val content: String
        get() = ir.dumpKotlinLike()
}

sealed class IrHolder<out T : Any>(val value: T) {
    abstract fun dumpKotlinLike(): String

    class IrElementHolder(value: IrElement) : IrHolder<IrElement>(value) {
        override fun dumpKotlinLike(): String =
            value.dumpKotlinLike()
    }

    class IrTypeHolder(value: IrType) : IrHolder<IrType>(value) {
        override fun dumpKotlinLike(): String =
            value.dumpKotlinLike()
    }
}

fun compilationException(
    message: String,
    element: IrElement,
    context: CommonBackendContext
): Nothing {
    throw CompilationException(
        message,
        context.currentFile,
        IrHolder.IrElementHolder(element)
    )
}

fun compilationException(
    message: String,
    type: IrType,
    context: CommonBackendContext
): Nothing {
    throw CompilationException(
        message,
        context.currentFile,
        IrHolder.IrTypeHolder(type)
    )
}

fun compilationException(
    message: String,
    element: IrElement,
    file: IrFile,
): Nothing {
    throw CompilationException(
        message,
        file,
        IrHolder.IrElementHolder(element)
    )
}

fun compilationException(
    message: String,
    type: IrType,
    file: IrFile,
): Nothing {
    throw CompilationException(
        message,
        file,
        IrHolder.IrTypeHolder(type)
    )
}