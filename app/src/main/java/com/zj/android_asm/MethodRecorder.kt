package com.zj.android_asm

import android.util.Log

object MethodRecorder {
    private val mMethodRecordMap = HashMap<String, MethodRecordItem>()

    @JvmStatic
    fun onMethodEnter(className: String, methodName: String, parameterList: List<Any?>?) {
        val key = "${className},${methodName}"
        val startTime = System.currentTimeMillis()
        val list = parameterList?.filterNotNull() ?: emptyList()
        mMethodRecordMap[key] = MethodRecordItem(startTime, list)
    }

    @JvmStatic
    fun onMethodExit(
        response: Any? = null,
        className: String,
        methodName: String,
        parameterTypes: String,
        returnType: String
    ) {
        val key = "${className},${methodName}"
        mMethodRecordMap[key]?.let {
            val parameters = it.parameterList.joinToString(",")
            val duration = System.currentTimeMillis() - it.startTime
            val result =
                "类名：$className \n方法名：$methodName \n参数类型：[$parameterTypes] \n入参：[$parameters] \n返回类型：$returnType \n返回值：$response \n耗时：$duration ms \n"
            Log.i("methodRecord", result)
        }
    }
}