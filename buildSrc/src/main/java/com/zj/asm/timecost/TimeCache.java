package com.zj.asm.timecost;

import java.util.HashMap;
import java.util.Map;

public class TimeCache {
    private static volatile TimeCache mInstance;

    private static final byte[] mLock = new byte[0];

    private final Map<String, Long> mStartTimes = new HashMap<>();

    private final Map<String, Long> mEndTimes = new HashMap<>();

    private TimeCache() {}

    public static TimeCache getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new TimeCache();
                }
            }
        }
        return mInstance;
    }
    public void putStartTime(String className, long time) {
        mStartTimes.put(className, time);
    }

    public void putEndTime(String className, long time) {
        mEndTimes.put(className, time);
    }

    public void printlnTime(String className) {
        if (!mStartTimes.containsKey(className) || !mEndTimes.containsKey(className)) {
            System.out.println("className ="+ className + "not exist");
        }
        long currTime = mEndTimes.get(className) - mStartTimes.get(className);
        System.out.println("className ="+ className + "，time consuming " + currTime+ "  ns");
    }
}

