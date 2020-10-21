rootProject.name = "examples"
include(":ktor-kotless-hello-world")
include(":aws-hello-world")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
}
