package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.24
 */
data class ArticleQueryRequest(
    var page: Int? = 1,
    var size: Int? = 10,
    var authorId: Long? = null,
    var circleId: Long? = null,
    var tagId: Long? = null,
    // var status: Int? = null,
    var status: Int? = 1,
    var keyword: String? = null,
    var sortBy: String? = "publishTime",
    var sortOrder: String? = "DESC"
)
