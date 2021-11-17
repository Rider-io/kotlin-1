/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.backend.js.transformers.irToJs

import org.jetbrains.kotlin.ir.backend.js.utils.JsGenerationContext
import org.jetbrains.kotlin.ir.declarations.IrDeclarationBase

class JsPolyfillTransformers : BaseIrElementToJsNodeTransformer<JsNode, JsGenerationContext> {
    override fun visitDeclaration(declaration: IrDeclarationBase, data: JsGenerationContext): JsFunction {
        return super.visitDeclaration(declaration, data)
    }
}