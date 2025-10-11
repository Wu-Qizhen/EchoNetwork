package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class Comment { // 评论

    private Long id; // 评论 ID

    private String content; // 评论内容

    private LocalDateTime createTime; // 创建时间

    private Integer likeCount = 0; // 点赞数

    private Long articleId; // 所属文章 ID

    private Long userId; // 评论者 ID

    /* private Long parentId; // 父级评论 ID

    private List<Long> replyIds = new ArrayList<>(); // 子评论 ID

    private List<Long> likeUserIds = new ArrayList<>(); // 点赞用户 ID */

    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
