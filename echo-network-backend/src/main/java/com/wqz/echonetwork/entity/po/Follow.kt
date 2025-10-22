package com.wqz.echonetwork.entity.po

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
data class Follow(
    var id: Long? = null, // 关注 ID
    var createTime: LocalDateTime? = null, // 创建时间
    var followerUserId: Long? = null, // 关注者 ID
    var followingUserId: Long? = null // 被关注者 ID
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
    }
}
