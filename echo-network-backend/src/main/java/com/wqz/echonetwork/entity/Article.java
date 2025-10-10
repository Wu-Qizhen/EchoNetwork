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
public class Article {
    private Long id;

    private String title;

    private String content;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private LocalDateTime publishedTime;

    private ArticleStatus status = ArticleStatus.DRAFT;

    private Integer viewCount = 0;

    private Integer likeCount = 0;

    private Integer commentCount = 0;

    private Long authorId;

    private List<Long> commentIds = new ArrayList<>();

    private List<Long> likeUserIds = new ArrayList<>();

    private Set<Tag> tags = new HashSet<>();

    protected void onCreate() {
        createdTime = LocalDateTime.now();
        if (status == ArticleStatus.PUBLISHED && publishedTime == null) {
            publishedTime = LocalDateTime.now();
        }
    }

    protected void onUpdate() {
        updatedTime = LocalDateTime.now();
    }
}
