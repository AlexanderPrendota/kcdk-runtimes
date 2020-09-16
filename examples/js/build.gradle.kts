plugins {
    id("org.jetbrains.kotlin.js") version "1.4.0"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.4.0"
    id("com.kotlin.aws.js.js-plugin") version "0.0.1" apply true
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime-js:1.0-M1-1.4.0-rc")
    implementation("com.kotlin.aws.js.runtime:kotlin-js-aws-runtime:0.0.1")
}

runtime {
    handler = "com.js.sample.Handler::handler"
    outputDir = project.buildDir.absolutePath + "/my_lambda"
}
