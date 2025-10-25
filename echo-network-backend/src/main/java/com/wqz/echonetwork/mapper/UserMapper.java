package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.User;
import com.wqz.echonetwork.utils.SqlUtil;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class UserMapper {

    public User findByUserNameOrEmail(String text) {
        return SqlUtil.queryObject(
                "SELECT * FROM user WHERE username = ? OR email = ?",
                User.class,
                text,
                text
        );
    }

    public User findById(Long id) {
        return SqlUtil.queryObject(
                "SELECT * FROM user WHERE id = ?",
                User.class,
                id
        );
    }

    public User findByUsername(String username) {
        return SqlUtil.queryObject(
                "SELECT * FROM user WHERE username = ?",
                User.class,
                username
        );
    }

    public User findByEmail(String email) {
        return SqlUtil.queryObject(
                "SELECT * FROM user WHERE email = ?",
                User.class,
                email
        );
    }

    public List<User> search(String keyword) {
        return SqlUtil.queryList(
                "SELECT * FROM user WHERE username LIKE ? OR email LIKE ?",
                User.class,
                "%" + keyword + "%",
                "%" + keyword + "%"
        );
    }

    public int insert(User user) {
        return SqlUtil.update(
                "INSERT INTO user (username, email, password, nickname, create_time, last_login_time, status, role, follower_count) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getCreateTime(),
                user.getLastLoginTime(),
                user.getStatus(),
                user.getRole(),
                user.getFollowerCount()
        );
    }

    public int update(User user) {
        return SqlUtil.update(
                "UPDATE user SET username = ?, " +
                        "email = ?, " +
                        // "phone = ?, " +
                        "password = ?, " +
                        "nickname = ?, " +
                        "bio = ?, " +
                        "avatar_url = ?, " +
                        "create_time = ?, " +
                        "last_login_time = ?, " +
                        "follower_count = ? WHERE id = ?",
                user.getUsername(),
                user.getEmail(),
                // user.getPhone(),
                user.getPassword(),
                user.getNickname(),
                user.getBio(),
                user.getAvatarUrl(),
                user.getCreateTime(),
                user.getLastLoginTime(),
                user.getFollowerCount(),
                user.getId()
        );
    }

    public int delete(Long id) {
        return SqlUtil.update(
                "UPDATE user SET status = 2 WHERE id = ?",
                id
        );
    }

    public int updateProfile(Long id, String nickname, String bio, String avatarUrl) {
        return SqlUtil.update("UPDATE user SET nickname = ?, bio = ?, avatar_url = ? WHERE id = ?", nickname, bio, avatarUrl, id);
    }

    public void updateLastLoginTime(Long id, LocalDateTime lastLoginTime) {
        SqlUtil.update("UPDATE user SET last_login_time = ? WHERE id = ?", lastLoginTime, id);
    }

    public int updatePasswordByEmail(String email, String password) {
        return SqlUtil.update("UPDATE user SET password = ? WHERE email = ?", password, email);
    }

    /**
     * 更新粉丝数
     */
    public int updateFollowerCount(Long userId, int delta) {
        String sql = delta > 0 ?
                "UPDATE user SET follower_count = follower_count + 1 WHERE id = ?" :
                "UPDATE user SET follower_count = GREATEST(0, follower_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, userId);
    }

    /* public static void main(String[] args) {
        UserMapper userMapper = new UserMapper();
        List<User> search = userMapper.search("1");
        System.out.println(search);
    } */
}
