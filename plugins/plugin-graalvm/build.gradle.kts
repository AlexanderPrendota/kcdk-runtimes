import tanvd.kosogor.proxy.publishJar
import tanvd.kosogor.proxy.publishPlugin

plugins {
    kotlin("jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(kotlin("stdlib"))

    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("gradle-plugin-api"))

    implementation("com.github.jengelman.gradle.plugins", "shadow", "6.0.0")

    implementation("com.bmuschko", "gradle-docker-plugin", "6.6.1")
    implementation("com.github.docker-java:docker-java:3.2.5")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.2.5")
}

publishPlugin {
    id = "io.kcdk"
    displayName = "Kotlin Cloud Development Gradle plugin"
    implementationClass = "com.kotlin.aws.runtime.RuntimeKotlinGradlePlugin"
    version = project.version.toString()
    info {
        description = "Kotlin Cloud Development Kit plugin for AWS"
        website = "https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
        vcsUrl = "https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
        tags.addAll(listOf("kotlin", "aws", "runtime", "graalvm"))
    }
}

publishJar {
    publication {
        artifactId = "io.kcdk.gradle.plugin"
    }
}
