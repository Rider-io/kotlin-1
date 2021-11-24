plugins {
    kotlin("jvm")
    id("jps-compatible")
}

dependencies {
    api(project(":core:util.runtime"))
    api(commonDep("javax.inject"))
    compileOnly(kotlinStdlib())
    compileOnly(intellijCore())
    testApi(kotlinStdlib())
    testCompileOnly("org.jetbrains:annotations:13.0")
    testApi(project(":kotlin-test:kotlin-test-jvm"))
    testApi(project(":kotlin-test:kotlin-test-junit"))
    testApi(commonDep("junit:junit"))
    testCompileOnly(intellijCore())
    testRuntimeOnly(intellijDep()) { includeJars("trove4j", "util") }
}

sourceSets {
    "main" { projectDefault() }
    "test" { projectDefault() }
}

testsJar {}

projectTest(parallel = true) {
    dependsOn(":dist")
    workingDir = rootDir
}
