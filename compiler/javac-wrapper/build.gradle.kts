plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":compiler:util"))
    api(project(":compiler:frontend.java"))

    compileOnly(toolsJarApi())
    compileOnly(intellijCore())
    compileOnly(intellijDep()) { includeIntellijCoreJarDependencies(project) }
}

sourceSets {
    "main" {
        projectDefault()
    }
    "test" { }
}
