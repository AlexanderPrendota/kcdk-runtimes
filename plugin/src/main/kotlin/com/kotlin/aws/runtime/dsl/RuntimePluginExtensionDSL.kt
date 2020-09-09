package com.kotlin.aws.runtime.dsl

import org.gradle.api.Project
import java.io.File
import java.io.Serializable
import java.nio.file.Path

@DslMarker
annotation class RuntimeDSLTag

class RuntimePluginExtension : Serializable {
    var handler: String? = null
    var generationPath: File? = null

    internal fun generationPathOrDefault(project: Project): File {
        if (generationPath != null) return generationPath!!
        val default = File(project.buildDir, "kotlin-gen")
        default.mkdirs()
        return default
    }

    internal val config: RuntimeConfig = RuntimeConfig()

    @RuntimeDSLTag
    fun config(configure: RuntimeConfig.() -> Unit) {
        config.configure()
    }

    @RuntimeDSLTag
    class RuntimeConfig : Serializable {
        var reflectConfigurationFile: Path? = null
        var flags: List<String>? = null
        var image: String? = null
    }

}
