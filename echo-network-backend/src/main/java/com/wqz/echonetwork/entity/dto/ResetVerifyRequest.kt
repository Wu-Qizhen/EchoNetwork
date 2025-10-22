package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.21
 */
data class ResetVerifyRequest(
    val email: String,
    val captcha: String
) {

    init {
        require(email.isNotEmpty()) { "邮箱不能为空" }
        require(isValidEmail(email)) { "邮箱格式错误" }
        require(captcha.length == 6) { "验证码长度错误" }
    }

    private fun isValidEmail(email: String): Boolean {
        return Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$").matches(email)
    }
}
