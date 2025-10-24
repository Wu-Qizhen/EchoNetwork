package com.wqz.echonetwork.entity.po

import java.time.LocalDateTime

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
data class Message( // 私信 TODO
    var id: Long? = null, // 私信 ID
    var senderId: Long? = null, // 发送者 ID
    var receiverId: Long? = null, // 接收者 ID
    var content: String = "", // 内容
    var createTime: LocalDateTime? = null // 创建时间
) {

    init {
        onCreate()
    }

    fun onCreate() {
        createTime = LocalDateTime.now()
    }
}
