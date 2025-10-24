package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.24
 */
data class ArticleInteractionResponse(
    var articleId: Long,
    var likeCount: Int,
    var starCount: Int,
    var isLiked: Boolean,
    var isStarred: Boolean
)
