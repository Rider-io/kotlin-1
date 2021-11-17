/*
 * Copyright 2010-2021 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
@file:JsQualifier("Math")
package kotlin.polyfills

@JsPolyfill("""
if (typeof Math.imul === "undefined") {
    Math.imul = function imul(a, b) {
        return ((a & 0xffff0000) * (b & 0xffff) + (a & 0xffff) * (b | 0)) | 0;
    }
}
""")
external fun imul(a: Int, b: Int): Int