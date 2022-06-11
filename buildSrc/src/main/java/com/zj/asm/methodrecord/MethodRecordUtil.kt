package com.zj.asm.methodrecord

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter
import org.objectweb.asm.commons.LocalVariablesSorter
import java.util.regex.Pattern

object MethodRecordUtil {
    fun getParameterTypeList(descriptor: String): List<String> {
        val parameterTypeList = mutableListOf<String>()
        val m =
            Pattern.compile("(L.*?;|\\[{0,2}L.*?;|[ZCBSIFJD]|\\[{0,2}[ZCBSIFJD]{1})")
                .matcher(descriptor.substring(0, descriptor.lastIndexOf(')') + 1))

        while (m.find()) {
            parameterTypeList.add(m.group(1))
        }
        return parameterTypeList
    }

    fun newParameterArrayList(mv: MethodVisitor, localVariablesSorter: LocalVariablesSorter): Int {
        mv.visitTypeInsn(AdviceAdapter.NEW, "java/util/ArrayList")
        mv.visitInsn(AdviceAdapter.DUP)
        mv.visitMethodInsn(
            AdviceAdapter.INVOKESPECIAL,
            "java/util/ArrayList",
            "<init>",
            "()V",
            false
        )
        val parametersIdentifier = localVariablesSorter.newLocal(Type.getType(List::class.java))
        mv.visitVarInsn(AdviceAdapter.ASTORE, parametersIdentifier)
        return parametersIdentifier
    }

    fun fillParameterArray(
        methodDesc: String,
        mv: MethodVisitor,
        parametersIdentifier: Int,
        access: Int
    ) {
        val isStatic = (access and Opcodes.ACC_STATIC) != 0
        var cursor = if (isStatic) 0 else 1
        val methodType = Type.getMethodType(methodDesc)
        methodType.argumentTypes.forEach {
            mv.visitVarInsn(AdviceAdapter.ALOAD, parametersIdentifier)
            val opcode = it.getOpcode(Opcodes.ILOAD)
            mv.visitVarInsn(opcode, cursor)
            if (it.sort >= Type.BOOLEAN && it.sort <= Type.DOUBLE) {
                typeCastToObject(mv, it)
            }
            cursor += it.size
            mv.visitMethodInsn(
                AdviceAdapter.INVOKEINTERFACE,
                "java/util/List",
                "add",
                "(Ljava/lang/Object;)Z",
                true
            )
            mv.visitInsn(AdviceAdapter.POP)
        }
    }

    fun loadReturnData(mv: MethodVisitor, methodDesc: String) {
        val methodType = Type.getMethodType(methodDesc)
        if (methodType.returnType.size == 1) {
            mv.visitInsn(AdviceAdapter.DUP)
        } else {
            mv.visitInsn(AdviceAdapter.DUP2)
        }
        typeCastToObject(mv, methodType.returnType)
    }

    private fun typeCastToObject(mv: MethodVisitor, type: Type) {
        when (type) {
            Type.INT_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Integer",
                    "valueOf",
                    "(I)Ljava/lang/Integer;",
                    false
                )
            }
            Type.CHAR_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Character",
                    "valueOf",
                    "(C)Ljava/lang/Character;",
                    false
                )
            }
            Type.BYTE_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Byte",
                    "valueOf",
                    "(B)Ljava/lang/Byte;",
                    false
                )
            }
            Type.BOOLEAN_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Boolean",
                    "valueOf",
                    "(Z)Ljava/lang/Boolean;",
                    false
                )
            }
            Type.SHORT_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Short",
                    "valueOf",
                    "(S)Ljava/lang/Short;",
                    false
                )
            }
            Type.FLOAT_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Float",
                    "valueOf",
                    "(F)Ljava/lang/Float;",
                    false
                )
            }
            Type.LONG_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Long",
                    "valueOf",
                    "(J)Ljava/lang/Long;",
                    false
                )
            }
            Type.DOUBLE_TYPE -> {
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Double",
                    "valueOf",
                    "(D)Ljava/lang/Double;",
                    false
                )
            }
        }
    }

    fun onMethodEnter(
        mv: MethodVisitor,
        className: String,
        name: String?,
        parametersIdentifier: Int
    ) {
        mv.visitLdcInsn(className)
        mv.visitLdcInsn(name)
        mv.visitVarInsn(AdviceAdapter.ALOAD, parametersIdentifier)
        mv.visitMethodInsn(
            AdviceAdapter.INVOKESTATIC, "com/zj/android_asm/MethodRecorder", "onMethodEnter",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", false
        )
    }

    fun onMethodExit(
        mv: MethodVisitor,
        className: String,
        name: String?,
        methodDesc: String,
    ) {
        val methodType = Type.getMethodType(methodDesc)
        val parameterTypes = methodType.argumentTypes.joinToString(",") { it.descriptor }
        val returnType = methodType.returnType.descriptor
        mv.visitLdcInsn(className)
        mv.visitLdcInsn(name)
        mv.visitLdcInsn(parameterTypes)
        mv.visitLdcInsn(returnType)
        mv.visitMethodInsn(
            AdviceAdapter.INVOKESTATIC,
            "com/zj/android_asm/MethodRecorder",
            "onMethodExit",
            "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
            false
        )
    }
}