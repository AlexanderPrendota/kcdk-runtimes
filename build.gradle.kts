import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

group = "com.kotlin.aws.runtime"
version = "0.1.2"

val jarFileName: String = "${rootProject.name}-$version-all.jar"

plugins {
    id("tanvd.kosogor") apply true
    kotlin("jvm") apply false
    id("io.gitlab.arturbosch.detekt") apply true
}

subprojects {
    apply {
        plugin("tanvd.kosogor")
        plugin("io.gitlab.arturbosch.detekt")
    }

    repositories {
        jcenter()
        gradlePluginPortal()
    }


    tasks.withType<KotlinJvmCompile> {
        kotlinOptions {
            jvmTarget = "1.8"
            languageVersion = "1.3"
            apiVersion = "1.3"
        }
    }

    detekt {
        parallel = true

        config = rootProject.files("detekt.yml")

        reports {
            xml {
                enabled = false
            }
            html {
                enabled = false
            }
        }
    }

    afterEvaluate {
        System.setProperty("gradle.publish.key", System.getenv("gradle_publish_key") ?: "")
        System.setProperty("gradle.publish.secret", System.getenv("gradle_publish_secret") ?: "")
    }
}

