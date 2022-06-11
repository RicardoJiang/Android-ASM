package com.zj.asm.methodrecord

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import java.util.regex.Pattern

class MethodRecordClassVisitor(nextVisitor: ClassVisitor, private val className: String) :
    ClassVisitor(Opcodes.ASM5, nextVisitor) {
    override fun visitMethod(
        access: Int,
        name: String?,
        descriptor: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor {
        val methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions)
        val newMethodVisitor =
            object : AdviceAdapter(Opcodes.ASM5, methodVisitor, access, name, descriptor) {

                @Override
                override fun onMethodEnter() {
                    // 方法开始
                    if (isNeedVisiMethod() && descriptor != null) {
                        val parameterTypeList = MethodRecordUtil.getParameterTypeList(descriptor)
                        val parametersIdentifier = MethodRecordUtil.newParameterArrayList(mv, this)
                        MethodRecordUtil.fillParameterArray(
                            parameterTypeList,
                            mv,
                            parametersIdentifier,
                            access
                        )
                        MethodRecordUtil.onMethodEnter(mv, name, parametersIdentifier)
                    }
                    super.onMethodEnter();
                }

                @Override
                override fun onMethodExit(opcode: Int) {
                    // 方法结束
//                    if (isNeedVisiMethod(name)) {
//                        mv.visitLdcInsn(name);
//                        mv.visitLdcInsn(className)
//                        mv.visitMethodInsn(
//                            INVOKESTATIC, "com/zj/android_asm/TimeCache", "putEndTime",
//                            "(Ljava/lang/String;Ljava/lang/String;)V", false
//                        );
//                    }
                    super.onMethodExit(opcode);
                }
            }
        return newMethodVisitor
    }

    private fun isNeedVisiMethod(): Boolean {
        return className.contains("MethodRecorder").not()
    }
}