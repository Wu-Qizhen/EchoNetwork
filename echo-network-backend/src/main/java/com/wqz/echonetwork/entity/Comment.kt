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

    public Comment() {
    }

    public Comment(Long id, String content, LocalDateTime createTime, Integer likeCount, Long articleId, Long userId) {
        this.id = id;
        this.content = content;
        this.createTime = createTime;
        this.likeCount = likeCount;
        this.articleId = articleId;
        this.userId = userId;
    }

    /* private Long parentId; // 父级评论 ID

    private List<Long> replyIds = new ArrayList<>(); // 子评论 ID

    private List<Long> likeUserIds = new ArrayList<>(); // 点赞用户 ID */

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

    public String toString() {
        return "Comment{id = " + id + ", content = " + content + ", createTime = " + createTime + ", likeCount = " + likeCount + ", articleId = " + articleId + ", userId = " + userId + "}";
    }
}
