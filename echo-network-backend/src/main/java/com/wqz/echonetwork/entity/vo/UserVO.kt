package com.wqz.echonetwork.entity.vo

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.20
 */
data class UserVO(
    var id: Long,
    var username: String,
    var email: String,
    // var phone: String? = null,
    var nickname: String,
    var bio: String?,
    var avatarUrl: String?,
    var followerCount: Int,
    var followingCount: Int,
    var articleCount: Int,
    var createTime: LocalDateTime,
    var lastLoginTime: LocalDateTime,
    var role: Int,
    var status: Int
)
