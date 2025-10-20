package com.wqz.echonetwork.entity

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
data class ArticleLike(
    var id: Long? = null, // 点赞 ID
    var createTime: LocalDateTime? = null, // 点赞时间
    var userId: Long? = null, // 点赞用户 ID
    var articleId: Long? = null // 点赞文章 ID
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
    }
}
