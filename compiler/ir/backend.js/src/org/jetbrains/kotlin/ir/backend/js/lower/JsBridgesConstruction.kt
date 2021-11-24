/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.ir.backend.js.lower

import org.jetbrains.kotlin.backend.common.ir.isOverridableOrOverrides
import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.backend.js.JsIrBackendContext
import org.jetbrains.kotlin.ir.backend.js.JsLoweredDeclarationOrigin
import org.jetbrains.kotlin.ir.backend.js.utils.erasedUpperBound
import org.jetbrains.kotlin.ir.backend.js.utils.hasStableJsName
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.types.IrDynamicType
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.isNullable
import org.jetbrains.kotlin.ir.types.makeNullable
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.render
import org.jetbrains.kotlin.name.Name

class JsBridgesConstruction(context: JsIrBackendContext) : BridgesConstruction<JsIrBackendContext>(context) {
    override fun getFunctionSignature(function: IrSimpleFunction): JsCommonSignature =
        function.jsCommonSignature(context.irBuiltIns, considerReturnType = false)

    override fun getBridgeOrigin(bridge: IrSimpleFunction): IrDeclarationOrigin =
        if (bridge.hasStableJsName(context))
            JsLoweredDeclarationOrigin.BRIDGE_WITH_STABLE_NAME
        else
            JsLoweredDeclarationOrigin.BRIDGE_WITHOUT_STABLE_NAME
}

data class JsCommonSignature(
    val name: Name,
    val extensionReceiverType: IrType?,
    val valueParametersType: List<IrType>,
    val returnType: IrType?,
    // Needed for bridges to final non-override methods
    // that indirectly implement interfaces. For example:
    //    interface I { fun foo() }
    //    class C1 { fun foo() {} }
    //    class C2 : C1(), I
    val isVirtual: Boolean,
) {
    override fun toString(): String {
        val er = extensionReceiverType?.let { "(er: ${it.render()}) " } ?: ""
        val parameters = valueParametersType.joinToString(", ") { it.render() }
        val nonVirtual = if (!isVirtual) "(non-virtual) " else ""
        return "[$nonVirtual$er$name($parameters) -> ${returnType?.render()}]"
    }
}

fun IrSimpleFunction.jsCommonSignature(
    irBuiltIns: IrBuiltIns,
    considerReturnType: Boolean
): JsCommonSignature =
    JsCommonSignature(
        name,
        extensionReceiverParameter?.type?.eraseGenerics(irBuiltIns),
        valueParameters.map { it.type.eraseGenerics(irBuiltIns) },
        returnType.eraseGenerics(irBuiltIns).takeIf { considerReturnType },
        isOverridableOrOverrides
    )

private fun IrType.eraseGenerics(irBuiltIns: IrBuiltIns): IrType {
    if (this is IrDynamicType) return this
    val defaultType = this.erasedUpperBound?.defaultType ?: irBuiltIns.anyType
    if (!this.isNullable()) return defaultType
    return defaultType.makeNullable()
}
