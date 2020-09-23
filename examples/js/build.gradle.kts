plugins {
    id("com.kotlin.aws.js.plugin") version "0.0.1" apply true
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-js"))
    implementation("com.kotlin.aws.js.runtime:kotlin-aws-js-runtime:0.0.1")
}

runtime {
    handler = "com.js.sample::handler"
}
