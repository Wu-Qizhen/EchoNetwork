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

    public int insert(Follow follow) {
        return SqlUtil.update(
                "INSERT INTO follow (create_time, follower_user_id, following_user_id) VALUES (?, ?, ?)",
                follow.getCreateTime(),
                follow.getFollowerUserId(),
                follow.getFollowingUserId()
        );
    }

    public int delete(Long id) {
        return SqlUtil.update("DELETE FROM follow WHERE id = ?", id);
    }

    public int deleteByUserIds(Long followerUserId, Long followingUserId) {
        return SqlUtil.update(
                "DELETE FROM follow WHERE follower_user_id = ? AND following_user_id = ?",
                followerUserId,
                followingUserId
        );
    }

    public Follow findById(Long id) {
        return SqlUtil.queryObject(
                "SELECT * FROM follow WHERE id = ?",
                Follow.class,
                id
        );
    }

    public Follow findByUserIds(Long followerUserId, Long followingUserId) {
        return SqlUtil.queryObject(
                "SELECT * FROM follow WHERE follower_user_id = ? AND following_user_id = ?",
                Follow.class,
                followerUserId,
                followingUserId
        );
    }

    public boolean existsByUserIds(Long followerUserId, Long followingUserId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM follow WHERE follower_user_id = ? AND following_user_id = ?",
                Integer.class,
                followerUserId,
                followingUserId
        );
        return count != null && count > 0;
    }

    // 统计某博主的粉丝数（被关注数）
    public int countFollowers(Long userId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM follow WHERE following_user_id = ?",
                Integer.class,
                userId
        );
        return count != null ? count : 0;
    }

    // 统计某用户的关注数（关注他人数）
    public int countFollowing(Long userId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM follow WHERE follower_user_id = ?",
                Integer.class,
                userId
        );
        return count != null ? count : 0;
    }

    // 查询某博主的粉丝列表（关注该博主的人）
    public List<Follow> findFollowers(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM follow WHERE following_user_id = ? ORDER BY create_time DESC",
                Follow.class,
                userId
        );
    }

    // 查询某用户的关注列表（该用户关注的人）
    public List<Follow> findFollowing(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM follow WHERE follower_user_id = ? ORDER BY create_time DESC",
                Follow.class,
                userId
        );
    }

    // 分页查询粉丝列表
    public List<Follow> findFollowersWithPagination(Long userId, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM follow WHERE following_user_id = ? ORDER BY create_time DESC LIMIT ? OFFSET ?",
                Follow.class,
                userId,
                limit,
                offset
        );
    }

    // 分页查询关注列表
    public List<Follow> findFollowingWithPagination(Long userId, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM follow WHERE follower_user_id = ? ORDER BY create_time DESC LIMIT ? OFFSET ?",
                Follow.class,
                userId,
                limit,
                offset
        );
    }

    // 查询粉丝 ID 列表
    public List<Long> findFollowerIds(Long userId) {
        return SqlUtil.queryList(
                "SELECT follower_user_id FROM follow WHERE following_user_id = ?",
                Long.class,
                userId
        );
    }

    // 查询关注用户 ID 列表
    public List<Long> findFollowingIds(Long userId) {
        return SqlUtil.queryList(
                "SELECT following_user_id FROM follow WHERE follower_user_id = ?",
                Long.class,
                userId
        );
    }

    // 查询共同关注（两人都关注的人）
    public List<Long> findCommonFollowing(Long userId1, Long userId2) {
        return SqlUtil.queryList(
                "SELECT f1.following_user_id " +
                        "FROM follow f1 " +
                        "INNER JOIN follow f2 ON f1.following_user_id = f2.following_user_id " +
                        "WHERE f1.follower_user_id = ? AND f2.follower_user_id = ?",
                Long.class,
                userId1,
                userId2
        );
    }

    // 查询共同粉丝（关注两人的共同用户）
    public List<Long> findCommonFollowers(Long userId1, Long userId2) {
        return SqlUtil.queryList(
                "SELECT f1.follower_user_id " +
                        "FROM follow f1 " +
                        "INNER JOIN follow f2 ON f1.follower_user_id = f2.follower_user_id " +
                        "WHERE f1.following_user_id = ? AND f2.following_user_id = ?",
                Long.class,
                userId1,
                userId2
        );
    }

    // 查询可能认识的人（通过共同关注推荐）
    public List<Long> findRecommendedUsers(Long userId, int limit) {
        return SqlUtil.queryList(
                "SELECT DISTINCT f2.following_user_id " +
                        "FROM follow f1 " +
                        "INNER JOIN follow f2 ON f1.following_user_id = f2.follower_user_id " +
                        "WHERE f1.follower_user_id = ? " +
                        "AND f2.following_user_id != ? " +
                        "AND f2.following_user_id NOT IN (" +
                        "    SELECT following_user_id FROM follow WHERE follower_user_id = ?" +
                        ") " +
                        "LIMIT ?",
                Long.class,
                userId,
                userId,
                userId,
                limit
        );
    }

    // 批量删除关注关系（用于用户注销等场景）
    public int deleteAllByUserId(Long userId) {
        return SqlUtil.update(
                "DELETE FROM follow WHERE follower_user_id = ? OR following_user_id = ?",
                userId,
                userId
        );
    }

    // 获取热门博主（粉丝数最多的用户）
    public List<Long> findPopularUsers(int limit) {
        return SqlUtil.queryList(
                "SELECT following_user_id, COUNT(*) as follower_count " +
                        "FROM follow " +
                        "GROUP BY following_user_id " +
                        "ORDER BY follower_count DESC " +
                        "LIMIT ?",
                Long.class,
                limit
        );
    }

    // 统计每日新增关注数
    public int countDailyNewFollows(String date) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM follow WHERE DATE(create_time) = ?",
                Integer.class,
                date
        );
        return count != null ? count : 0;
    }
}
