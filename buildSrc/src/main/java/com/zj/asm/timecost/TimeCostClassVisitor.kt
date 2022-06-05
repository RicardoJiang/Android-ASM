package com.zj.asm.timecost

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class TimeCostClassVisitor(nextVisitor: ClassVisitor) : ClassVisitor(Opcodes.ASM5) {
}