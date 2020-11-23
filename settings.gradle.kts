rootProject.name = "kotlin-aws-lambda-custom-runtimes"

pluginManagement {
    val kotlinVersion: String by settings
    val kosogorVersion: String by settings
    val detektVersion: String by settings
    val bintrayVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("tanvd.kosogor") version kosogorVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
        id("com.jfrog.bintray") version bintrayVersion
    }
}

include(":runtimes:runtime-graalvm")
include(":runtimes:runtime-js")

include(":plugins:plugin-graalvm")
include(":plugins:plugin-js")
