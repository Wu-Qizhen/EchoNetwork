package com.wqz.echonetwork.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class Circle { // 圈子

    private Long id; // 圈子 ID

    private String name; // 圈子名称

    private String description; // 圈子描述

    private Long creatorId; // 创建者 ID

    private LocalDateTime createTime; // 创建时间

    public Circle() {
    }

    public Circle(Long id, String name, String description, Long creatorId, LocalDateTime createTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creatorId = creatorId;
        this.createTime = createTime;
    }

    /* private Set<Long> memberIds = new HashSet<>(); // 用户成员 ID */

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
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取
     *
     * @return creatorId
     */
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置
     *
     * @param creatorId
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
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
        return "Circle{id = " + id + ", name = " + name + ", description = " + description + ", creatorId = " + creatorId + ", createTime = " + createTime + "}";
    }
}
