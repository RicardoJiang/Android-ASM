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
        parameterTypeList: List<String>,
        mv: MethodVisitor,
        parametersIdentifier: Int,
        access: Int
    ) {
        var cursor = if ((Opcodes.ACC_STATIC and access) == 0) 0 else -1
        parameterTypeList.forEach {
            val type = it
            mv.visitVarInsn(AdviceAdapter.ALOAD, parametersIdentifier)
            if ("Z" == type) {
                mv.visitVarInsn(AdviceAdapter.ILOAD, ++cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Boolean",
                    "valueOf",
                    "(Z)Ljava/lang/Boolean;",
                    false
                )
            } else if ("C" == type) {
                mv.visitVarInsn(AdviceAdapter.ILOAD, ++cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Character",
                    "valueOf",
                    "(C)Ljava/lang/Character;",
                    false
                )
            } else if ("B" == type) {
                mv.visitVarInsn(AdviceAdapter.ILOAD, ++cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Byte",
                    "valueOf",
                    "(B)Ljava/lang/Byte;",
                    false
                )
            } else if ("S" == type) {
                mv.visitVarInsn(AdviceAdapter.ILOAD, ++cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Short",
                    "valueOf",
                    "(S)Ljava/lang/Short;",
                    false
                )
            } else if ("I" == type) {
                mv.visitVarInsn(AdviceAdapter.ILOAD, ++cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Integer",
                    "valueOf",
                    "(I)Ljava/lang/Integer;",
                    false
                )
            } else if ("F" == type) {
                mv.visitVarInsn(AdviceAdapter.FLOAD, ++cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Float",
                    "valueOf",
                    "(F)Ljava/lang/Float;",
                    false
                )
            } else if ("J" == type) {
                mv.visitVarInsn(AdviceAdapter.LLOAD, ++cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Long",
                    "valueOf",
                    "(J)Ljava/lang/Long;",
                    false
                )
            } else if ("D" == type) {
                cursor += 2
                mv.visitVarInsn(AdviceAdapter.DLOAD, cursor)  //获取对应的参数
                mv.visitMethodInsn(
                    AdviceAdapter.INVOKESTATIC,
                    "java/lang/Double",
                    "valueOf",
                    "(D)Ljava/lang/Double;",
                    false
                )
            } else {
                ++cursor
                mv.visitVarInsn(AdviceAdapter.ALOAD, cursor)  //获取对应的参数
            }
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

    fun onMethodEnter(
        mv: MethodVisitor,
        className: String,
        name: String?,
        parametersIdentifier: Int
    ) {
        mv.visitLdcInsn(className)
        mv.visitLdcInsn(name)
        mv.visitVarInsn(AdviceAdapter.ALOAD, parametersIdentifier);
        mv.visitMethodInsn(
            AdviceAdapter.INVOKESTATIC, "com/zj/android_asm/MethodRecorder", "onMethodEnter",
            "(Ljava/lang/String;Ljava/lang/String;Ljava/util/List;)V", false
        );
    }
}