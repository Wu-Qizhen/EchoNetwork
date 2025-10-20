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

    public ArticleStar() {
    }

    public ArticleStar(Long id, Long userId, Long articleId, LocalDateTime createTime) {
        this.id = id;
        this.userId = userId;
        this.articleId = articleId;
        this.createTime = createTime;
    }

    protected void onCreate() {
        createTime = LocalDateTime.now();
    }

    /**
     * 获取
     *
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取
     *
     * @return userId
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置
     *
     * @param userId
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取
     *
     * @return articleId
     */
    public Long getArticleId() {
        return articleId;
    }

    /**
     * 设置
     *
     * @param articleId
     */
    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    /**
     * 获取
     *
     * @return createTime
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置
     *
     * @param createTime
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return "ArticleStar{id = " + id + ", userId = " + userId + ", articleId = " + articleId + ", createTime = " + createTime + "}";
    }
}
