package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.20
 */
data class UserProfileResponse(
    val id: Long,
    val username: String,
    val email: String,
    val phone: String? = null,
    val nickname: String,
    val bio: String? = null,
    val avatarUrl: String? = null,
    val createTime: String,
    val lastLoginTime: String,
    val status: Int,
    val role: Int,
    val followerCount: Int,
    val followingCount: Int,
    val articleCount: Int,
    val isFollowing: Boolean
)
