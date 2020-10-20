import tanvd.kosogor.proxy.publishJar
import tanvd.kosogor.proxy.publishPlugin

plugins {
    kotlin("jvm") version "1.4.0"
    id("java-gradle-plugin")
    id("tanvd.kosogor") version "1.0.9" apply true
}

group = "io.kcdk.js"
version = "0.0.1"

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    api(kotlin("stdlib"))

    implementation(kotlin("stdlib-js"))
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("gradle-plugin-api"))

    api("org.jetbrains.kotlin", "kotlin-gradle-plugin", "1.4.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

publishJar {
    publication {
        artifactId = "io.kcdk.js.gradle.plugin"
    }
}

publishPlugin {
    id = "io.kcdk.js"
    displayName = "Kotlin Cloud Development Gradle plugin"
    implementationClass = "com.kotlin.aws.js.plugin.RuntimeKotlinGradlePlugin"
    version = project.version.toString()
    info {
        description = "Kotlin Cloud Development Kit plugin for AWS"
        website = "https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
        vcsUrl = "https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
        tags.addAll(listOf("kotlin", "aws", "runtime", "js"))
    }
}