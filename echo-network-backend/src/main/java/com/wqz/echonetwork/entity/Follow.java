package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class Follow {
    private Long id;

    private LocalDateTime createdTime;

    private Long followerUserId;

    private Long followingUserId;

    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
