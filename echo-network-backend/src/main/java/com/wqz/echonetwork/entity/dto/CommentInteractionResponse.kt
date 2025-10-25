package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
data class CommentInteractionResponse(
    var commentId: Long? = null,
    var likeCount: Int? = null,
    var isLiked: Boolean? = null
)
