
plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:util"))
    api(project(":compiler:backend"))
    api(project(":compiler:frontend"))
    api(project(":compiler:frontend.java"))
    compileOnly(intellijCore())

    compileOnly(intellijDep()) { includeJars("platform-core-ui", "platform-util-ui") }

    compileOnly(intellijDep()) { includeJars("asm-all", "trove4j", "guava", rootProject = rootProject) }
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}

