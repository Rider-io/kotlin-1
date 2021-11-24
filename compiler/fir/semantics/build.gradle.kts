plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:fir:providers"))
    implementation(project(":core:util.runtime"))

    compileOnly(project(":kotlin-reflect-api"))
    compileOnly(intellijDependency("guava"))
}

sourceSets {
    "main" { projectDefault() }
    "test" { none() }
}
