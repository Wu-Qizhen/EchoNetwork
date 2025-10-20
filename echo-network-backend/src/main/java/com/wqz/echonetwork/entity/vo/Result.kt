package com.wqz.echonetwork.entity.vo

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.4
 */
data class Result<T>(
    var code: Int? = null,
    var message: String = "",
    var data: T? = null
) {
    companion object {
        @JvmStatic
        fun <T> success(): Result<T> = success("请求成功")

        @JvmStatic
        fun <T> success(message: String): Result<T> = success(200, message)

        @JvmStatic
        fun <T> success(code: Int, message: String): Result<T> = Result(code, message, null)

        @JvmStatic
        fun <T> success(message: String, data: T): Result<T> = Result(200, message, data)

        @JvmStatic
        fun <T> success(data: T): Result<T> = success("请求成功", data)

        @JvmStatic
        fun <T> error(message: String): Result<T> = Result(400, message, null)

        @JvmStatic
        fun <T> error(code: Int, message: String): Result<T> = Result(code, message, null)

        @JvmStatic
        fun <T> paramsError(): Result<T> = error("参数错误")

        @JvmStatic
        fun <T> unauthorized(): Result<T> = error(401, "未授权")

        @JvmStatic
        fun <T> forbidden(): Result<T> = error(403, "无访问权限")
    }

    fun asJsonString(): String {
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls)
    }
}
