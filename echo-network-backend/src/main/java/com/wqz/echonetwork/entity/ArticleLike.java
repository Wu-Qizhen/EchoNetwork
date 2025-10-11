package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class ArticleLike { // 文章点赞

    private Long id; // 点赞 ID

    private LocalDateTime createTime; // 点赞时间

    private Long userId; // 点赞用户 ID

    private Long articleId; // 点赞文章 ID

    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
