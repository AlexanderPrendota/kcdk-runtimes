import tanvd.kosogor.proxy.publishJar
import tanvd.kosogor.proxy.publishPlugin

group = rootProject.group
version = rootProject.version

dependencies {
    api(kotlin("stdlib"))

    implementation(gradleApi())
    implementation(gradleKotlinDsl())
    implementation(kotlin("gradle-plugin-api"))

    implementation("com.github.jengelman.gradle.plugins", "shadow", "6.0.0")

    implementation("com.bmuschko", "gradle-docker-plugin", "6.6.1")
    implementation("com.github.docker-java:docker-java:3.2.5")
    implementation("com.github.docker-java:docker-java-transport-httpclient5:3.2.5")
    implementation("javax.activation:activation:1.1.1")
    implementation("org.ow2.asm:asm:7.3.1")
}

publishPlugin {
    id = "com.kotlin.aws.runtime"
    displayName = "Kotlin AWS Runtime"
    implementationClass = "com.kotlin.aws.runtime.RuntimeKotlinGradlePlugin"
    version = project.version.toString()
}

publishJar {
    publication {
        artifactId = "com.kotlin.aws.runtime.gradle.plugin"
    }
}
