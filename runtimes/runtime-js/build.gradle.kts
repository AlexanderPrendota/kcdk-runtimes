plugins {
    kotlin("js") version "1.4.0"
    id("com.jfrog.bintray") version "1.8.5"
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

if (hasProperty("bintrayApiKey")) {
    bintray {
        user = "alexanderprendota"
        key = project.findProperty("bintrayApiKey").toString()
        publish = true
        setPublications("maven-publish")
        pkg.apply {
            repo = "io.kcdk"
            name = "runtime.js"
            setLicenses("Apache-2.0")
            vcsUrl = "https://github.com/AlexanderPrendota/kcdk-runtimes"
            version.apply {
                name = project.version.toString()
            }
        }
    }
}
