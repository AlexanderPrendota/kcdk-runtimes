package com.kotlin.aws.runtime.dsl

import java.io.Serializable

@RuntimeDSLTag
class RuntimeConfig : Serializable {
    internal val graalVm: GraalVMConfigurationDSL = GraalVMConfigurationDSL()

    @RuntimeDSLTag
    fun graalVm(configure: GraalVMConfigurationDSL.() -> Unit) {
        graalVm.configure()
    }
}