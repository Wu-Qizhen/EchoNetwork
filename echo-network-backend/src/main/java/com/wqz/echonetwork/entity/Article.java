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

    public Article() {
    }

    public Article(Long id, String title, String content, LocalDateTime createTime, LocalDateTime updateTime, LocalDateTime publishTime, Integer status, Integer viewCount, Integer likeCount, Integer starCount, Integer commentCount, Long authorId, Long circleId, Set<Long> tagIds) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.publishTime = publishTime;
        this.status = status;
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.starCount = starCount;
        this.commentCount = commentCount;
        this.authorId = authorId;
        this.circleId = circleId;
        this.tagIds = tagIds;
    }

    protected void onCreate() {
        createTime = LocalDateTime.now();
        if (status == ArticleStatus.PUBLISHED.getId() && publishTime == null) {
            publishTime = LocalDateTime.now();
        }
    }

    protected void onUpdate() {
        updateTime = LocalDateTime.now();
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
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取
     *
     * @return content
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置
     *
     * @param content
     */
    public void setContent(String content) {
        this.content = content;
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

    /**
     * 获取
     *
     * @return updateTime
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置
     *
     * @param updateTime
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取
     *
     * @return publishTime
     */
    public LocalDateTime getPublishTime() {
        return publishTime;
    }

    /**
     * 设置
     *
     * @param publishTime
     */
    public void setPublishTime(LocalDateTime publishTime) {
        this.publishTime = publishTime;
    }

    /**
     * 获取
     *
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置
     *
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取
     *
     * @return viewCount
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * 设置
     *
     * @param viewCount
     */
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    /**
     * 获取
     *
     * @return likeCount
     */
    public Integer getLikeCount() {
        return likeCount;
    }

    /**
     * 设置
     *
     * @param likeCount
     */
    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    /**
     * 获取
     *
     * @return starCount
     */
    public Integer getStarCount() {
        return starCount;
    }

    /**
     * 设置
     *
     * @param starCount
     */
    public void setStarCount(Integer starCount) {
        this.starCount = starCount;
    }

    /**
     * 获取
     *
     * @return commentCount
     */
    public Integer getCommentCount() {
        return commentCount;
    }

    /**
     * 设置
     *
     * @param commentCount
     */
    public void setCommentCount(Integer commentCount) {
        this.commentCount = commentCount;
    }

    /**
     * 获取
     *
     * @return authorId
     */
    public Long getAuthorId() {
        return authorId;
    }

    /**
     * 设置
     *
     * @param authorId
     */
    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    /**
     * 获取
     *
     * @return circleId
     */
    public Long getCircleId() {
        return circleId;
    }

    /**
     * 设置
     *
     * @param circleId
     */
    public void setCircleId(Long circleId) {
        this.circleId = circleId;
    }

    /**
     * 获取
     *
     * @return tagIds
     */
    public Set<Long> getTagIds() {
        return tagIds;
    }

    /**
     * 设置
     *
     * @param tagIds
     */
    public void setTagIds(Set<Long> tagIds) {
        this.tagIds = tagIds;
    }

    public String toString() {
        return "Article{id = " + id + ", title = " + title + ", content = " + content + ", createTime = " + createTime + ", updateTime = " + updateTime + ", publishTime = " + publishTime + ", status = " + status + ", viewCount = " + viewCount + ", likeCount = " + likeCount + ", starCount = " + starCount + ", commentCount = " + commentCount + ", authorId = " + authorId + ", circleId = " + circleId + ", tagIds = " + tagIds + "}";
    }
}
