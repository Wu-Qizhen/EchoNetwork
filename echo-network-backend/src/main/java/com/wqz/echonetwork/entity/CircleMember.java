package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class CircleMember { // 圈子成员

    private Long id; // 圈子成员 ID

    private Long circleId; // 圈子 ID

    private Long userId; // 成员用户 ID

    private LocalDateTime joinTime; // 加入时间

    private Integer role; // 成员角色

    public CircleMember() {
    }

    public CircleMember(Long id, Long circleId, Long userId, LocalDateTime joinTime, Integer role) {
        this.id = id;
        this.circleId = circleId;
        this.userId = userId;
        this.joinTime = joinTime;
        this.role = role;
    }

    protected void onCreate() {
        joinTime = LocalDateTime.now();
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
     * @return joinTime
     */
    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    /**
     * 设置
     *
     * @param joinTime
     */
    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }

    /**
     * 获取
     *
     * @return role
     */
    public Integer getRole() {
        return role;
    }

    /**
     * 设置
     *
     * @param role
     */
    public void setRole(Integer role) {
        this.role = role;
    }

    public String toString() {
        return "CircleMember{id = " + id + ", circleId = " + circleId + ", userId = " + userId + ", joinTime = " + joinTime + ", role = " + role + "}";
    }
}
