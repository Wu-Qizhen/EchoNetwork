package com.wqz.echonetwork.entity;

import com.wqz.echonetwork.entity.enums.ArticleStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class Article { // 文章
    private Long id; // 文章 ID

    private String title; // 文章标题

    private String content; // 文章内容

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime updateTime; // 更新时间

    private LocalDateTime publishTime; // 发布时间

    private Integer status = ArticleStatus.DRAFT.getId(); // 文章状态 0 草稿 1 发布 2 删除

    private Integer viewCount = 0; // 浏览次数

    private Integer likeCount = 0; // 点赞数

    private Integer starCount = 0; // 收藏数

    private Integer commentCount = 0; // 评论数

    private Long authorId; // 作者 ID

    private Long circleId; // 圈子 ID

    /* private List<Long> commentIds = new ArrayList<>(); // 评论 ID

    private Set<Long> likeUserIds = new HashSet<>(); // 点赞用户 ID */

    private Set<Long> tagIds = new HashSet<>(); // 标签 ID

    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == ArticleStatus.PUBLISHED.getId() && publishTime == null) {
            publishTime = LocalDateTime.now();
        }
    }

    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
