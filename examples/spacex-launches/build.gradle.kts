import com.kotlin.aws.native.plugin.utils.runtime

plugins {
    kotlin("multiplatform") version "1.4.20"
    kotlin("plugin.serialization") version "1.4.20"
    id("io.kcdk.native") version "0.1.2"
}

group = "io.kcdk.native.examples.hello-native"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://kotlin.bintray.com/kotlinx/")
    mavenLocal()
}

kotlin {
    linuxX64()

    macosX64 {
        binaries {
            executable()
        }
    }
    sourceSets {
        val commonMain by getting {}
        val posixMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
                implementation("io.ktor:ktor-client-curl:1.4.2")
                implementation("io.ktor:ktor-client-serialization:1.4.2")
            }
        }
        val linuxX64Main by getting {
            dependsOn(posixMain)
            dependencies {
                implementation("com.kotlin.aws.runtime:runtime-native:0.1.2")
            }
        }
        val macosX64Main by getting {
            dependsOn(posixMain)
        }
    }
}

runtime {
    handler = "spaceXHandler"
}