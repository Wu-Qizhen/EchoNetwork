package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class Comment {

    private Long id;

    private String content;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Integer likeCount = 0;

    private Long articleId;

    private Long userId;

    private Long parentId; // 父级评论 ID

    private List<Long> replyIds = new ArrayList<>();

    private List<Long> likeUserIds = new ArrayList<>();

    protected void onCreate() {
        createdTime = LocalDateTime.now();
    }
}
