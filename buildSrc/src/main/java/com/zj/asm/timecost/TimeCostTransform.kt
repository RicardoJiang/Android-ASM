package com.zj.asm.timecost

import com.android.build.api.instrumentation.*
import com.zj.asm.timecost.TimeCostClassVisitor
import groovy.util.logging.Log
import org.gradle.api.provider.Property
import org.objectweb.asm.ClassVisitor

abstract class TimeCostTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return TimeCostClassVisitor(nextClassVisitor,classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
       return (classData.className.contains("com.zj.android_asm"))
    }
}
