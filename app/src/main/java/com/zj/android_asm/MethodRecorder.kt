package com.zj.android_asm

import android.util.Log
import com.google.gson.Gson
import java.util.HashMap

object MethodRecorder {
    private val mStartTimes: Map<String, Long> = HashMap()

    @JvmStatic
    fun onMethodEnter(className: String, methodName: String, parameterList: List<Any?>?) {
        val noNullParameterList = parameterList?.filterNotNull() ?: emptyList<Any>()
        Log.i("tiaoshi", "className:" + className)
        Log.i("tiaoshi", "here start:$methodName")
        noNullParameterList.forEach {
            Log.i("tiaoshi", "here parameter:" + it.toString())
        }
//        Log.i("tiaoshi", "here parameter:" + Gson().toJson(noNullParameterList))
    }

    @JvmStatic
    fun onMethodExit(response: Any?=null,className: String, methodName: String) {
        Log.i("tiaoshi", ("response $methodName:" + response?.toString()))
    }
}