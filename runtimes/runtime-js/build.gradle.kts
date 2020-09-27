plugins {
    kotlin("js") version "1.4.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.0"
    `maven-publish`
}

group = "com.kotlin.aws.js.runtime"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    js {
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.executable()
    }
    sourceSets {
        main {
            kotlin.srcDir("src")
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlin", "kotlin-stdlib-js")
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-runtime-js", "1.0-M1-1.4.0-rc")
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core-js", "1.3.8")
    implementation(npm("node-fetch", "2.6.0"))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.Kotlin2JsCompile>().configureEach {
    kotlinOptions {
        moduleKind = "commonjs"
    }
}

publishing {
    publications {
        create<MavenPublication>("default") {
            from(components["kotlin"])
        }
    }
    repositories {
        mavenLocal()
    }
}