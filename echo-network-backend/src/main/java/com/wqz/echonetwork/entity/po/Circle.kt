package com.wqz.echonetwork.entity.po

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
data class Circle(
    var id: Long? = null, // 圈子 ID
    var name: String = "", // 圈子名称
    var description: String = "", // 圈子描述
    var creatorId: Long? = null, // 创建者 ID
    var createTime: LocalDateTime? = null // 创建时间
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
    }
}
