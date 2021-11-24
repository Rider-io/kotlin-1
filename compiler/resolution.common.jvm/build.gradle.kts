plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:resolution.common"))
    api(project(":core:compiler.common.jvm"))
    api(project(":compiler:psi"))
    implementation(project(":compiler:util"))
    implementation(commonDep("io.javaslang","javaslang"))
    compileOnly(intellijCore())
    compileOnly(intellijDep()) { includeJars("asm-all", "trove4j", "guava", rootProject = rootProject) }
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
