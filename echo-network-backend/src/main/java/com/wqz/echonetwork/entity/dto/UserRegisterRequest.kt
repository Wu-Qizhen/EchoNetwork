package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
data class UserRegisterRequest(
    var username: String = "",
    var email: String = "",
    var password: String = "",
    var captcha: String = ""
) {
    fun isParamsValid(): Boolean {
        return username.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                captcha.isNotBlank()
    }

    // 改为前端校验
    /* fun getValidationError(): String? {
         if (username.isBlank()) {
             return "用户名不能为空"
         }
         if (email.isBlank()) {
             return "邮箱不能为空"
         }
         if (password.isBlank()) {
             return "密码不能为空"
         }
         if (password.length < 6) {
             return "密码长度不能少于 6 位"
         }
         return null
     }*/
}
