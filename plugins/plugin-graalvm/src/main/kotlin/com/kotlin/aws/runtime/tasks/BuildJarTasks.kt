package com.kotlin.aws.runtime.tasks

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.kotlin.aws.runtime.utils.Groups
import com.kotlin.aws.runtime.utils.mySourceSets
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.get


internal fun Project.createGraalJar(): Jar {
    return tasks.create("graalJar", Jar::class.java) {
        it.group = Groups.build
        it.manifest { manifest ->
            manifest.attributes(mapOf("Main-Class" to "com.kotlin.aws.runtime.KotlinAWSCustomRuntimeKt"))
        }
    }
}

internal fun Project.createShadowJarGraal(jar: Jar): ShadowJar {
    return tasks.create("shadowJarGraal", ShadowJar::class.java) {
        it.group = Groups.shadow
        it.archiveClassifier.set("graal")
        it.archiveVersion.set("")
        it.from(mySourceSets["main"].output)
        it.configurations.add(configurations["compileClasspath"])
        it.configurations.add(configurations["runtimeClasspath"])
        it.manifest.inheritFrom(jar.manifest)
    }
}
