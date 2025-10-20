package com.wqz.echonetwork.entity

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
data class ArticleTag(
    var id: Long? = null,
    var articleId: Long? = null,
    var tagId: Long? = null,
    var createTime: LocalDateTime? = null
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
    }
}
