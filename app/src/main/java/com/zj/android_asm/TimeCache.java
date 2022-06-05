package com.zj.android_asm;

import java.util.HashMap;
import java.util.Map;

public class TimeCache {
    public static Map<String, Long> mStartTimes = new HashMap<>();

    public static Map<String, Long> mEndTimes = new HashMap<>();

    public static void putStartTime(String methodName) {
        mStartTimes.put(methodName, System.currentTimeMillis());
    }

    public static void putEndTime(String methodName) {
        mEndTimes.put(methodName, System.currentTimeMillis());
        printlnTime(methodName);
    }

    public static void printlnTime(String methodName) {
        if (!mStartTimes.containsKey(methodName) || !mEndTimes.containsKey(methodName)) {
            System.out.println("className ="+ methodName + "not exist");
        }
        long currTime = mEndTimes.get(methodName) - mStartTimes.get(methodName);
        System.out.println("methodName ="+ methodName + "，time consuming " + currTime+ "  ms");
    }
}


