package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class ArticleStar { // 收藏

    private Long id; // 收藏 ID

    private Long userId; // 收藏用户 ID

    private Long articleId; // 收藏文章 ID

    private LocalDateTime createTime; // 创建时间

    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
