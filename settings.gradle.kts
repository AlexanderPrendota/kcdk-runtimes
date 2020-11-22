rootProject.name = "kotlin-aws-lambda-custom-runtimes"

pluginManagement {
    val kotlinVersion: String by settings
    val kosogorVersion: String by settings
    val detektVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("tanvd.kosogor") version kosogorVersion
        id("io.gitlab.arturbosch.detekt") version detektVersion
    }
}

include(":runtimes:runtime-graalvm")
include(":plugins:plugin-graalvm")
include(":plugins:plugin-js")
