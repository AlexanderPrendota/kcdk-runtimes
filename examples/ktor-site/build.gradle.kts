
import com.kotlin.aws.runtime.dsl.runtime

group = rootProject.group
version = rootProject.version

plugins {
    id("com.kotlin.aws.runtime") version "0.1.0" apply true
}

dependencies {
    implementation("com.kotlin.aws.runtime", "runtime", "0.1.0")
    implementation("io.kotless", "ktor-lang", "0.1.6")
}


runtime {
    handler = "io.kotless.examples.Server::handleRequest"
}