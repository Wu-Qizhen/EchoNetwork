package com.wqz.echonetwork.entity

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
data class Comment(
    var id: Long? = null, // 评论 ID
    var content: String = "", // 评论内容
    var createTime: LocalDateTime? = null, // 创建时间
    var likeCount: Int = 0, // 点赞数
    var articleId: Long? = null, // 所属文章 ID
    var userId: Long? = null // 评论者 ID
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
    }
}
