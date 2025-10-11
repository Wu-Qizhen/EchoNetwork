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

    /* private Set<Long> memberIds = new HashSet<>(); // 用户成员 ID */

    protected void onCreate() {
        createTime = LocalDateTime.now();
    }
}
