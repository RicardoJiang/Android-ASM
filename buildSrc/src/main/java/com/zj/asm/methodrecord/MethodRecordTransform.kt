package com.zj.asm.methodrecord

import com.android.build.api.instrumentation.*
import com.zj.asm.timecost.TimeCostClassVisitor
import groovy.util.logging.Log
import org.gradle.api.provider.Property
import org.objectweb.asm.ClassVisitor

abstract class MethodRecordTransform: AsmClassVisitorFactory<InstrumentationParameters.None> {
    override fun createClassVisitor(classContext: ClassContext, nextClassVisitor: ClassVisitor): ClassVisitor {
        return MethodRecordClassVisitor(nextClassVisitor,classContext.currentClassData.className)
    }

    override fun isInstrumentable(classData: ClassData): Boolean {
       return (classData.className.contains("com.zj.android_asm"))
    }
}
