plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":core:compiler.common.jvm"))
    api(project(":compiler:config.jvm"))
    api(intellijDependency("asm-all"))
    api(intellijDependency("guava"))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
