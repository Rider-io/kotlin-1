plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:config"))
    api(project(":core:compiler.common.jvm"))
    compileOnly(intellijDependency("asm-all"))
}

sourceSets {
    "main" { projectDefault() }
    "test" { }
}
