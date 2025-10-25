package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.Follow;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.22
 */
public class FollowMapper {

    public long insert(Follow follow) {
        return SqlUtil.insert(
                "INSERT INTO follow (follower_user_id, following_user_id, create_time) VALUES (?, ?, ?)",
                follow.getFollowerUserId(),
                follow.getFollowingUserId(),
                follow.getCreateTime()
        );
    }

    public int delete(Long followerUserId, Long followingUserId) {
        return SqlUtil.update(
                "DELETE FROM follow WHERE follower_user_id = ? AND following_user_id = ?",
                followerUserId, followingUserId
        );
    }

    public boolean existsByUserIds(Long followerUserId, Long followingUserId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM follow WHERE follower_user_id = ? AND following_user_id = ?",
                Integer.class,
                followerUserId, followingUserId
        );
        return count != null && count > 0;
    }

    public int countFollowers(Long userId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM follow WHERE following_user_id = ?",
                Integer.class,
                userId
        );
        return count != null ? count : 0;
    }

    public int countFollowing(Long userId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM follow WHERE follower_user_id = ?",
                Integer.class,
                userId
        );
        return count != null ? count : 0;
    }

    public List<Follow> findFollowers(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM follow WHERE following_user_id = ? ORDER BY create_time DESC",
                Follow.class,
                userId
        );
    }

    public List<Follow> findFollowing(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM follow WHERE follower_user_id = ? ORDER BY create_time DESC",
                Follow.class,
                userId
        );
    }

    public List<Long> findFollowerIds(Long userId) {
        return SqlUtil.queryList(
                "SELECT follower_user_id FROM follow WHERE following_user_id = ?",
                Long.class,
                userId
        );
    }

    public List<Long> findFollowingIds(Long userId) {
        return SqlUtil.queryList(
                "SELECT following_user_id FROM follow WHERE follower_user_id = ?",
                Long.class,
                userId
        );
    }
}
