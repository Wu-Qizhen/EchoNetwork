package com.wqz.echonetwork.entity.dto

import com.wqz.echonetwork.entity.vo.UserVO
import java.util.Date

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.20
 */
data class UserLoginResponse(
    var token: String,
    var user: UserVO,
    var expire: Date
)
