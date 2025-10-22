package com.wqz.echonetwork.entity.po

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
data class CircleMember(
    var id: Long? = null, // 圈子成员 ID
    var circleId: Long? = null, // 圈子 ID
    var userId: Long? = null, // 成员用户 ID
    var joinTime: LocalDateTime? = null, // 加入时间
    var role: Int? = null // 成员角色
) {

    init {
        onCreate()
    }

    fun onCreate() {
        joinTime = LocalDateTime.now()
    }
}
