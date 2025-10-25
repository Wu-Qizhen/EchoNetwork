package com.wqz.echonetwork.entity.vo

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
data class CircleMemberVO(
    var id: Long? = null,
    var user: UserVO? = null,
    var joinTime: java.time.LocalDateTime? = null,
    var role: Int? = null
)
