package com.kotlin.aws.runtime.dsl

import java.io.Serializable
import java.nio.file.Path

@RuntimeDSLTag
class GraalVMConfigurationDSL : Serializable {
    var reflectConfigurationFile: Path? = null
    var flags: List<String>? = null
    var image: String? = null
}
