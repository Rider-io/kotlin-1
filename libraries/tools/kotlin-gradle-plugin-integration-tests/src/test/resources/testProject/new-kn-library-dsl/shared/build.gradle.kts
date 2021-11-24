import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType
import org.jetbrains.kotlin.gradle.targets.native.tasks.artifact.*
import org.jetbrains.kotlin.konan.target.KonanTarget.*

plugins {
    kotlin("multiplatform")
}

kotlin {
    linuxX64()
}

kotlinArtifact("mylib", Library) {
    target = LINUX_X64
}

kotlinArtifact("myslib", Library) {
    target = LINUX_X64
    modes = setOf(NativeBuildType.DEBUG)
    isStatic = true
    addModule(project(":lib"))
}