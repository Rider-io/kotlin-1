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
    var file: IrFile?,
    val ir: Any?, /* IrElement | IrType */
    cause: Throwable? = null
) : RuntimeException(
    message,
    cause,
) {
    override val message: String
        get() = "Back-end (JS): Please report this problem.\nProblem with `$content`.\nDetails: " + super.message

    val line: Int
        get() = file?.fileEntry?.getLineNumber((ir as? IrElement)?.startOffset ?: UNDEFINED_OFFSET)?.plus(1) ?: -1

    val column: Int
        get() = file?.fileEntry?.getColumnNumber((ir as? IrElement)?.startOffset ?: UNDEFINED_OFFSET)?.plus(1) ?: -1

    val path: String
        get() = file?.path ?: "<unknown-path>"

    val content: String
        get() = when (ir) {
            is IrElement -> ir.dumpKotlinLike()
            is IrType -> ir.dumpKotlinLike()
            else -> "<unknown-content>"
        }
}

fun compilationException(
    message: String,
    element: IrElement
): Nothing {
    throw CompilationException(
        message,
        null,
        element
    )
}

fun compilationException(
    message: String,
    type: IrType?
): Nothing {
    throw CompilationException(
        message,
        null,
        type,
    )
}