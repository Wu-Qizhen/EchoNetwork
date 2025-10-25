package com.wqz.echonetwork.entity.vo

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
data class CommentVO(
    var id: Long? = null,
    var content: String? = null,
    var createTime: LocalDateTime? = null,
    var likeCount: Int? = null,
    var user: UserVO? = null,
    var articleId: Long? = null,
    var isLiked: Boolean? = null
)
