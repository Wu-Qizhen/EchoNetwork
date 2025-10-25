package com.wqz.echonetwork.entity.vo

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
data class CircleListItemVO(
    var id: Long? = null,
    var name: String = "",
    var description: String = "",
    var creator: UserVO? = null,
    var createTime: java.time.LocalDateTime? = null,
    var memberCount: Int? = null,
    var articleCount: Int? = null,
    var isMember: Boolean? = null,
    var memberRole: Int? = null
)
