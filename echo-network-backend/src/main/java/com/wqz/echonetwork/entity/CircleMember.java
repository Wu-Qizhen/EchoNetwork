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

    protected void onCreate() {
        joinTime = LocalDateTime.now();
    }
}
