plugins {
    id("io.kcdk.js") version "0.0.1" apply true
}

group = "io.kcdk.js.examples.js"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    jcenter()
}

dependencies {
    implementation("com.kotlin.aws.js.runtime", "kotlin-aws-js-runtime", "0.0.1")
    implementation(npm("is-number", "7.0.0"))
}

runtime {
    handler = "com.kotlin.aws.examples.js::handler"
}
