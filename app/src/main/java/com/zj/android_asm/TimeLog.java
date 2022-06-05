package com.zj.android_asm;

public class TimeLog {
    public static final void beforeMethod() {
        System.out.println("方法开始运行...");
    }

    public static final void afterMethod() {
        System.out.println("方法运行结束...");
    }
}
