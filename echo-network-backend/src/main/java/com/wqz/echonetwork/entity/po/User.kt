package com.wqz.echonetwork.entity.po

import com.wqz.echonetwork.entity.enums.UserRole
import com.wqz.echonetwork.entity.enums.UserStatus
import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
data class User(
    var id: Long? = null, // 用户 ID
    var username: String = "", // 用户名
    var email: String = "", // 邮箱
    var phone: String? = null, // 手机号 TODO
    var password: String = "", // 密码
    var nickname: String = "", // 昵称
    var bio: String = "", // 个人简介或签名
    var avatarUrl: String? = null, // 头像
    var createTime: LocalDateTime? = null, // 创建时间
    var lastLoginTime: LocalDateTime? = null, // 最后登录时间
    var status: Int = UserStatus.NORMAL.id, // 账号状态 0 正常 1 禁用 2 删除
    var role: Int = UserRole.USER.id, // 账号角色 0 普通用户 1 超级用户 2 管理员
    var followerCount: Int = 0 // 粉丝数
)
