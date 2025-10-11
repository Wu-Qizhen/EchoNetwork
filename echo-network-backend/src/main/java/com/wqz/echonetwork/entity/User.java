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
public class User { // 用户
    private Long id; // 用户 ID

    private String username; // 用户名

    private String email; // 邮箱

    private String phone; // 手机号 TODO

    private String password; // 密码

    private String nickname; // 昵称

    private String bio; // 个人简介或签名

    private String avatarUrl; // 头像

    private LocalDateTime createTime; // 创建时间

    private LocalDateTime lastLoginTime; // 最后登录时间

    private Integer status = UserStatus.NORMAL.getId(); // 账号状态 0 正常 1 禁用 2 删除

    private Integer role = UserRole.USER.getId(); // 账号角色 0 普通用户 1 超级用户 2 管理员

    /* private List<Long> articleIds = new ArrayList<>(); // 发布文章 ID

    private List<Long> commentIds = new ArrayList<>(); // 发布评论 ID

    private List<Long> followingIds = new ArrayList<>(); // 关注用户 ID

    private List<Long> followerIds = new ArrayList<>(); // 粉丝用户 ID

    private List<Long> likeObjIds = new ArrayList<>(); // 点赞对象 ID */

    private Integer followerCount = 0; // 粉丝数

    /* private List<Long> starArticleIds = new ArrayList<>(); // 收藏的文章 ID */
}
