import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.bmuschko.gradle.docker.tasks.container.*
import com.bmuschko.gradle.docker.tasks.image.*
import org.apache.tools.ant.taskdefs.condition.Os

group = "org.example"
version = "0.0.1"

val jarFileName: String = "${rootProject.name}-$version-all.jar"

plugins {
  kotlin("jvm") version "1.3.72"
  id("com.github.johnrengelman.shadow") version "6.0.0"
  id("com.bmuschko.docker-remote-api") version "6.6.0"
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib-jdk8"))
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.10.3")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.6.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
}

val createDockerfile by tasks.creating(Dockerfile::class) {
  val nativeFileName = File(jarFileName).nameWithoutExtension
  from("oracle/graalvm-ce:20.1.0-java11")
  instruction("RUN gu install native-image")
  instruction("RUN mkdir -p /working/build")
  entryPoint("bash")
  defaultCommand(
    "-c",
    """
      native-image --enable-url-protocols=https \
        -Djava.net.preferIPv4Stack=true \
        -H:+ReportUnsupportedElementsAtRuntime --no-server -jar /working/build/libs/$jarFileName; \
      mkdir -p /working/build/native; \
      cp -f $nativeFileName /working/build/native/$nativeFileName;
    """.trimIndent()
  )
}

val buildGraalNativeBuildImage by tasks.creating(DockerBuildImage::class) {
  dependsOn(createDockerfile)
  images.add("kotlin/graal-native-build:latest")
}

val createGraalNativeBuildContainer by tasks.creating(DockerCreateContainer::class) {
  val buildDir = when {
    Os.isFamily(Os.FAMILY_WINDOWS) -> rootProject.buildDir.absolutePath
      .replace('\\', '/')
      .replace("C:", "//c", ignoreCase = true)
    else -> rootProject.buildDir.absolutePath
  }

  println("Build directory: $buildDir")
  dependsOn(buildGraalNativeBuildImage)
  targetImageId(buildGraalNativeBuildImage.imageId)
  hostConfig.autoRemove.set(true)
  hostConfig.binds.set(mapOf(buildDir to "/working/build"))
}

val startGraalNativeBuildContainer by tasks.creating(DockerStartContainer::class) {
  dependsOn(createGraalNativeBuildContainer)
  targetContainerId(createGraalNativeBuildContainer.containerId)
  finalizedBy("logContainer")
}

tasks.create("logContainer", DockerLogsContainer::class) {
  targetContainerId(createGraalNativeBuildContainer.containerId)
  follow.set(true)
  tailAll.set(true)
  onNext {
    // Each log message from the container will be passed as it's made available
    logger.quiet(this.toString())
  }
}

tasks.create("nativeBuild") {
  dependsOn(":shadowJar", startGraalNativeBuildContainer)
}

tasks.withType<ShadowJar> {
  manifest {
    attributes(
      mapOf(
        "Main-Class" to "com.kotlin.aws.runtime.KotlinAWSCustomRuntimeKt"
      )
    )
  }

  archiveFileName.set(jarFileName)
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks {
  compileKotlin {
    kotlinOptions.jvmTarget = "11"
  }
  compileTestKotlin {
    kotlinOptions.jvmTarget = "11"
  }
}