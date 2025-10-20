package com.wqz.echonetwork.entity;

import com.wqz.echonetwork.entity.enums.UserRole;
import com.wqz.echonetwork.entity.enums.UserStatus;

import java.time.LocalDateTime;

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

    public User() {
    }

    public User(Long id, String username, String email, String phone, String password, String nickname, String bio, String avatarUrl, LocalDateTime createTime, LocalDateTime lastLoginTime, Integer status, Integer role, Integer followerCount) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.nickname = nickname;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        this.status = status;
        this.role = role;
        this.followerCount = followerCount;
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
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取
     *
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取
     *
     * @return phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置
     *
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 获取
     *
     * @return bio
     */
    public String getBio() {
        return bio;
    }

    /**
     * 设置
     *
     * @param bio
     */
    public void setBio(String bio) {
        this.bio = bio;
    }

    /**
     * 获取
     *
     * @return avatarUrl
     */
    public String getAvatarUrl() {
        return avatarUrl;
    }

    /**
     * 设置
     *
     * @param avatarUrl
     */
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
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
     * @return lastLoginTime
     */
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    /**
     * 设置
     *
     * @param lastLoginTime
     */
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
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

    /**
     * 获取
     *
     * @return followerCount
     */
    public Integer getFollowerCount() {
        return followerCount;
    }

    /**
     * 设置
     *
     * @param followerCount
     */
    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }

    public String toString() {
        return "User{id = " + id + ", username = " + username + ", email = " + email + ", phone = " + phone + ", password = " + password + ", nickname = " + nickname + ", bio = " + bio + ", avatarUrl = " + avatarUrl + ", createTime = " + createTime + ", lastLoginTime = " + lastLoginTime + ", status = " + status + ", role = " + role + ", followerCount = " + followerCount + "}";
    }

    /* private List<Long> starArticleIds = new ArrayList<>(); // 收藏的文章 ID */
}
