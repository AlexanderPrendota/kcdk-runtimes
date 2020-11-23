import tanvd.kosogor.accessors.jar
import tanvd.kosogor.proxy.publishJar

plugins {
    kotlin("jvm")
    id("tanvd.kosogor")
}

group = rootProject.group
version = rootProject.version

dependencies {
    api(kotlin("stdlib"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
    api("org.slf4j", "slf4j-api", "1.7.30")
    api("com.amazonaws:aws-lambda-java-events:3.1.1")
    api("com.amazonaws:aws-lambda-java-core:1.2.1")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jar("jar") {
    this.exclude("com/kotlin/aws/runtime/Adapter.class")
}

publishJar {
    bintray {
        username = "alexanderprendota"
        repository = "io.kcdk"
        info {
            description = "Kotlin AWS Custom runtime for GraalVM"
            githubRepo = "AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
            vcsUrl = "https://github.com/AlexanderPrendota/kotlin-aws-lambda-custom-runtimes"
            labels.addAll(listOf("kotlin", "graalvm", "aws", "runtime"))
        }
    }
}