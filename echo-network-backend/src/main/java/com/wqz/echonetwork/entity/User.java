package com.wqz.echonetwork.entity;

import com.wqz.echonetwork.entity.enums.UserRole;
import com.wqz.echonetwork.entity.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class User {
    private Long id; // 用户 ID

    private String username; // 用户名

    private String email; // 邮箱

    private String phone; // 手机号（以后再搞）

    private String password; // 密码

    private String displayName; // 昵称

    private String bio; // 个人简介或签名

    private String avatarUrl; // 头像

    private LocalDateTime createdTime; // 创建时间

    private LocalDateTime lastLoginTime; // 最后登录时间

    private UserStatus status = UserStatus.NORMAL; // 账号状态

    private UserRole userRole = UserRole.USER; // 账号角色

    private List<Long> articleIds = new ArrayList<>();

    private List<Long> commentIds = new ArrayList<>();

    private List<Long> followingIds = new ArrayList<>();

    private List<Long> followerIds = new ArrayList<>();

    private List<Long> likeObjIds = new ArrayList<>();

}
