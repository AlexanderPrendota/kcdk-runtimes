package com.kotlin.aws.runtime.dsl

import com.kotlin.aws.runtime.utils.GraalSettings
import org.gradle.api.Project
import java.io.File
import java.io.Serializable

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
        var reflectConfiguration: String? = null
        var flags: List<String>? = null
        var image: String? = null

        internal fun getFlagsOrDefault(): List<String> {
            val projectFlags = flags
            return if (projectFlags == null) {
                GraalSettings.FULL_GRAAL_VM_FLAGS
            } else {
                projectFlags + GraalSettings.BASE_GRAAL_FLAGS
            }
        }

        internal fun getImageOrDefault() = image ?: GraalSettings.GRAAL_VM_DOCKER_IMAGE

    }

}
