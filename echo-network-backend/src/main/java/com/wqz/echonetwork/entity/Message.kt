package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class Message { // 私信 TODO

    private Long id; // 私信 ID

    private Long senderId; // 发送者 ID

    private Long receiverId; // 接收者 ID

    private String content; // 内容

    private LocalDateTime createTime; // 创建时间

    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
