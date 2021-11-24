plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":kotlin-annotations-jvm"))
    api(project(":compiler:util"))
    api(project(":compiler:backend-common"))
    api(project(":compiler:frontend"))
    api(project(":compiler:frontend.java"))
    api(project(":compiler:serialization"))
    api(project(":compiler:backend.common.jvm"))
    compileOnly(intellijCore())
    compileOnly(intellijDependency("trove4j"))
    compileOnly(intellijDependency("asm-all"))
    compileOnly(intellijDependency("guava"))
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" {}
}
