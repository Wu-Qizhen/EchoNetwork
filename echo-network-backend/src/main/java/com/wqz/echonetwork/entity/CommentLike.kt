package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class CommentLike { // 评论点赞

    private Long id; // 点赞 ID

    private LocalDateTime createTime; // 点赞时间

    private Long userId; // 点赞用户 ID

    private Long commentId; // 点赞评论 ID

    public CommentLike() {
    }

    public CommentLike(Long id, LocalDateTime createTime, Long userId, Long commentId) {
        this.id = id;
        this.createTime = createTime;
        this.userId = userId;
        this.commentId = commentId;
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
     * @return commentId
     */
    public Long getCommentId() {
        return commentId;
    }

    /**
     * 设置
     *
     * @param commentId
     */
    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String toString() {
        return "CommentLike{id = " + id + ", createTime = " + createTime + ", userId = " + userId + ", commentId = " + commentId + "}";
    }
}
