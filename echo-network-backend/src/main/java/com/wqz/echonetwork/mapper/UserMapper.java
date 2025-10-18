package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.User;
import com.wqz.echonetwork.utils.SqlUtil;

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

    public User findById(Integer id) {
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

    public int delete(Integer id) {
        return SqlUtil.update("DELETE FROM user WHERE id = ?", id);
    }
}
