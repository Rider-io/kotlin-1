/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.backend.common

import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.path
import org.jetbrains.kotlin.ir.util.dumpKotlinLike

class CompilationException(
    message: String,
    private val file: IrFile?,
    private val element: IrElement,
) : RuntimeException(
    message
) {
    override val message: String
        get() = "Back-end (JS): Please report this problem.\nProblem with `$content`.\nDetails: " + super.message

    val line: Int
        get() = file?.fileEntry?.getLineNumber(element.startOffset)?.plus(1) ?: -1

    val column: Int
        get() = file?.fileEntry?.getColumnNumber(element.startOffset)?.plus(1) ?: -1

    val path: String
        get() = file?.path ?: "<unknown>"

    val content: String
        get() = element.dumpKotlinLike()
}

fun compilationException(
    message: String,
    element: IrElement,
    context: CommonBackendContext
): Nothing {
    throw CompilationException(
        message,
        context.currentFile,
        element
    )
}