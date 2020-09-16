import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile

group = "io.examples"
version = "0.1.0"

plugins {
    kotlin("jvm") version "1.3.72" apply true
}

subprojects {
    apply {
        plugin("kotlin")
    }

    repositories {
        mavenLocal()
        jcenter()
    }

    tasks.withType<KotlinJvmCompile> {
        kotlinOptions {
            jvmTarget = "11"
            languageVersion = "1.3"
            apiVersion = "1.3"
        }
    }
}
