import tanvd.kosogor.accessors.jar
import tanvd.kosogor.proxy.publishJar

group = rootProject.group
version = rootProject.version


dependencies {
    api(kotlin("stdlib"))

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")
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
}