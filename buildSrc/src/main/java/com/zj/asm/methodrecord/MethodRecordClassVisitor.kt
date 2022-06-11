package com.zj.asm.methodrecord

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.commons.AdviceAdapter

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
                        val parametersIdentifier = MethodRecordUtil.newParameterArrayList(mv, this)
                        MethodRecordUtil.fillParameterArray(
                            methodDesc, mv, parametersIdentifier, access
                        )
                        MethodRecordUtil.onMethodEnter(mv, className, name, parametersIdentifier)
                    }
                    super.onMethodEnter()
                }

                @Override
                override fun onMethodExit(opcode: Int) {
                    // 方法结束
                    if (isNeedVisiMethod()) {
                        if ((opcode in IRETURN..RETURN) || opcode == ATHROW) {
                            when (opcode) {
                                in IRETURN..DRETURN -> {
                                    MethodRecordUtil.loadReturnData(mv, methodDesc)
                                    MethodRecordUtil.onMethodExit(mv, className, name, methodDesc)
                                }
                                ARETURN -> {
                                    mv.visitInsn(DUP)
                                    MethodRecordUtil.onMethodExit(mv, className, name, methodDesc)
                                }
                                RETURN -> {
                                    mv.visitLdcInsn("void")
                                    MethodRecordUtil.onMethodExit(mv, className, name, methodDesc)
                                }
                                else -> {
                                }
                            }
                        }
                    }
                    super.onMethodExit(opcode);
                }
            }
        return newMethodVisitor
    }

    private fun isNeedVisiMethod(): Boolean {
        val filterClassList = listOf("MethodRecordItem", "MethodRecorder")
        return filterClassList.none { className.contains(it) }
    }
}