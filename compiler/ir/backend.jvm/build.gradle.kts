plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":kotlin-annotations-jvm"))
    api(project(":compiler:backend"))
    api(project(":compiler:ir.tree"))
    api(project(":compiler:ir.backend.common"))
    api(project(":compiler:backend.common.jvm"))
    compileOnly(project(":compiler:ir.tree.impl"))
    compileOnly(intellijCore())
    compileOnly(intellijDependency("asm-all"))
    compileOnly(intellijDependency("guava"))
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
