package com.wqz.echonetwork.entity.dto

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
data class CircleJoinResponse(
    var circleId: Long? = null,
    var userId: Long? = null,
    var joinTime: LocalDateTime? = null,
    var role: Int? = null
)
