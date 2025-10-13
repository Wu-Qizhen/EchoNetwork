package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class Follow { // 关注
    private Long id; // 关注 ID

    private LocalDateTime createTime; // 创建时间

    private Long followerUserId; // 关注者 ID

    private Long followingUserId; // 被关注者 ID

    public Follow() {
    }

    public Follow(Long id, LocalDateTime createTime, Long followerUserId, Long followingUserId) {
        this.id = id;
        this.createTime = createTime;
        this.followerUserId = followerUserId;
        this.followingUserId = followingUserId;
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
     * @return followerUserId
     */
    public Long getFollowerUserId() {
        return followerUserId;
    }

    /**
     * 设置
     *
     * @param followerUserId
     */
    public void setFollowerUserId(Long followerUserId) {
        this.followerUserId = followerUserId;
    }

    /**
     * 获取
     *
     * @return followingUserId
     */
    public Long getFollowingUserId() {
        return followingUserId;
    }

    /**
     * 设置
     *
     * @param followingUserId
     */
    public void setFollowingUserId(Long followingUserId) {
        this.followingUserId = followingUserId;
    }

    public String toString() {
        return "Follow{id = " + id + ", createTime = " + createTime + ", followerUserId = " + followerUserId + ", followingUserId = " + followingUserId + "}";
    }
}
