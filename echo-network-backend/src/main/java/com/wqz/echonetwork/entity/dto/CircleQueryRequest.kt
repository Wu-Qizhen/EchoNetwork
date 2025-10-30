package com.wqz.echonetwork.entity.dto

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.30
 */
data class CircleQueryRequest(
    var page: Int? = 1,
    var size: Int? = 10,
    var creatorId: Long? = null,
    var memberId: Long? = null,
    var keyword: String? = null,
    var sortBy: String? = "createTime",
    var sortOrder: String? = "DESC"
)
