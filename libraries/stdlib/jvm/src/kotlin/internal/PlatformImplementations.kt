/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package kotlin.internal

import java.lang.reflect.Method
import java.util.regex.MatchResult
import kotlin.random.FallbackThreadLocalRandom
import kotlin.random.Random

internal open class PlatformImplementations {

    private object ReflectThrowable {
        @JvmField
        public val addSuppressed: Method?
        @JvmField
        public val getSuppressed: Method?

        init {
            val throwableClass = Throwable::class.java
            val throwableMethods = throwableClass.methods
            addSuppressed = throwableMethods.find {
                it.name == "addSuppressed" && it.parameterTypes.singleOrNull() == throwableClass
            }
            getSuppressed = throwableMethods.find { it.name == "getSuppressed" }
        }
    }

    public open fun addSuppressed(cause: Throwable, exception: Throwable) {
        ReflectThrowable.addSuppressed?.invoke(cause, exception)
    }

    public open fun getSuppressed(exception: Throwable): List<Throwable> {
        @Suppress("UNCHECKED_CAST")
        return ReflectThrowable.getSuppressed?.invoke(exception)?.let { (it as Array<Throwable>).asList() }
            ?: emptyList()
    }

    public open fun getMatchResultNamedGroup(matchResult: MatchResult, name: String): MatchGroup? {
        throw UnsupportedOperationException("Retrieving groups by name is not supported on this platform.")
    }

    public open fun defaultPlatformRandom(): Random = FallbackThreadLocalRandom()
}


@JvmField
internal val IMPLEMENTATIONS: PlatformImplementations = run {
    val version = getJavaVersion()
    if (version >= 0x10008) {
        try {
            return@run castToBaseType<PlatformImplementations>(Class.forName("kotlin.internal.jdk8.JDK8PlatformImplementations").newInstance())
        } catch (e: ClassNotFoundException) { }
        try {
            return@run castToBaseType<PlatformImplementations>(Class.forName("kotlin.internal.JRE8PlatformImplementations").newInstance())
        } catch (e: ClassNotFoundException) { }
    }

    if (version >= 0x10007) {
        try {
            return@run castToBaseType<PlatformImplementations>(Class.forName("kotlin.internal.jdk7.JDK7PlatformImplementations").newInstance())
        } catch (e: ClassNotFoundException) { }
        try {
            return@run castToBaseType<PlatformImplementations>(Class.forName("kotlin.internal.JRE7PlatformImplementations").newInstance())
        } catch (e: ClassNotFoundException) { }
    }

    PlatformImplementations()
}

@kotlin.internal.InlineOnly
private inline fun <reified T : Any> castToBaseType(instance: Any): T {
    try {
        return instance as T
    } catch (e: ClassCastException) {
        val instanceCL = instance.javaClass.classLoader
        val baseTypeCL = T::class.java.classLoader
        if (instanceCL != baseTypeCL) {
            throw ClassNotFoundException("Instance class was loaded from a different classloader: $instanceCL, base type classloader: $baseTypeCL", e)
        }
        throw e
    }
}

private fun getJavaVersion(): Int {
    val default = 0x10006
    val version = System.getProperty("java.specification.version") ?: return default
    val firstDot = version.indexOf('.')
    if (firstDot < 0)
        return try { version.toInt() * 0x10000 } catch (e: NumberFormatException) { default }

    var secondDot = version.indexOf('.', firstDot + 1)
    if (secondDot < 0) secondDot = version.length

    val firstPart = version.substring(0, firstDot)
    val secondPart = version.substring(firstDot + 1, secondDot)
    return try {
        firstPart.toInt() * 0x10000 + secondPart.toInt()
    } catch (e: NumberFormatException) {
        default
    }
}

/**
 * Constant check of api version used during compilation
 *
 * This function is evaluated at compile time to a constant value,
 * so there should be no references to it in other modules.
 *
 * The function usages are validated to have literal argument values.
 */
@PublishedApi
@SinceKotlin("1.2")
internal fun apiVersionIsAtLeast(major: Int, minor: Int, patch: Int) =
    KotlinVersion.CURRENT.isAtLeast(major, minor, patch)
