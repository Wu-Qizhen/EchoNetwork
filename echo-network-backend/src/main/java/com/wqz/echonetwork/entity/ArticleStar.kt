package com.wqz.echonetwork.entity

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
data class ArticleStar(
    var id: Long? = null, // 收藏 ID
    var userId: Long? = null, // 收藏用户 ID
    var articleId: Long? = null, // 收藏文章 ID
    var createTime: LocalDateTime? = null // 创建时间
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
    }
}
