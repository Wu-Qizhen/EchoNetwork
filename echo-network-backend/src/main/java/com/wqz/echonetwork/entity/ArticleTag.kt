package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class ArticleTag {

    private Long id;
    private Long articleId;
    private Long tagId;
    private LocalDateTime createTime;

    public ArticleTag() {
    }

    public ArticleTag(Long id, Long articleId, Long tagId, LocalDateTime createTime) {
        this.id = id;
        this.articleId = articleId;
        this.tagId = tagId;
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
     * @return tagId
     */
    public Long getTagId() {
        return tagId;
    }

    /**
     * 设置
     *
     * @param tagId
     */
    public void setTagId(Long tagId) {
        this.tagId = tagId;
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
        return "ArticleTag{id = " + id + ", articleId = " + articleId + ", tagId = " + tagId + ", createTime = " + createTime + "}";
    }
}
