package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.23
 */
data class ArticleUpdateRequest(
    var id: Long? = null,
    var title: String,
    var content: String,
    var circleId: Long? = null,
    var tags: List<String>? = null,
    var status: Int,
)
