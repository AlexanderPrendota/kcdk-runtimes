import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

group = "org.example"
version = "0.0.1"

plugins {
  kotlin("jvm") version "1.3.72"
  id("com.github.johnrengelman.shadow") version "6.0.0"
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

tasks.withType<ShadowJar> {
  manifest {
    attributes(
      mapOf(
        "Main-Class" to "com.kotlin.aws.runtime.KotlinAWSCustomRuntimeKt"
      )
    )
  }
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