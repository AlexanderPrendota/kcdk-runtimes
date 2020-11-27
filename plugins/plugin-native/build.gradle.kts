import tanvd.kosogor.proxy.publishJar
import tanvd.kosogor.proxy.publishPlugin

plugins {
    kotlin("jvm")
    id("tanvd.kosogor")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(kotlin("stdlib"))

    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("gradle-plugin-api"))

    api("org.jetbrains.kotlin", "kotlin-gradle-plugin")
}

publishJar {
    publication {
        artifactId = "io.kcdk.native.gradle.plugin"
    }
}

publishPlugin {
    id = "io.kcdk.native"
    displayName = "Kotlin Cloud Development Gradle plugin"
    implementationClass = "com.kotlin.aws.native.plugin.RuntimeKotlinGradlePlugin"
    version = project.version.toString()
    info {
        description = "Kotlin Cloud Development Kit plugin for AWS"
        website = "https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
        vcsUrl = "https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
        tags.addAll(listOf("kotlin", "aws", "runtime", "native"))
    }
}