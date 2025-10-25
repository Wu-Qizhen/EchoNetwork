package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.CircleMember;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
public class CircleMemberMapper {

    public long insert(CircleMember circleMember) {
        return SqlUtil.insert(
                "INSERT INTO circle_member (circle_id, user_id, join_time, role) VALUES (?, ?, ?, ?)",
                circleMember.getCircleId(),
                circleMember.getUserId(),
                circleMember.getJoinTime(),
                circleMember.getRole()
        );
    }

    public int delete(Long circleId, Long userId) {
        return SqlUtil.update(
                "DELETE FROM circle_member WHERE circle_id = ? AND user_id = ?",
                circleId, userId
        );
    }

    public CircleMember findByCircleAndUser(Long circleId, Long userId) {
        return SqlUtil.queryObject(
                "SELECT * FROM circle_member WHERE circle_id = ? AND user_id = ?",
                CircleMember.class,
                circleId, userId
        );
    }

    public boolean exists(Long circleId, Long userId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM circle_member WHERE circle_id = ? AND user_id = ?",
                Integer.class,
                circleId, userId
        );
        return count != null && count > 0;
    }

    public List<CircleMember> findByCircleId(Long circleId, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM circle_member WHERE circle_id = ? ORDER BY join_time DESC LIMIT ? OFFSET ?",
                CircleMember.class,
                circleId, limit, offset
        );
    }

    public List<CircleMember> findByUserId(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM circle_member WHERE user_id = ? ORDER BY join_time DESC",
                CircleMember.class,
                userId
        );
    }

    public int countByCircleId(Long circleId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM circle_member WHERE circle_id = ?",
                Integer.class,
                circleId
        );
        return count != null ? count : 0;
    }

    public int updateRole(Long circleId, Long userId, Integer role) {
        return SqlUtil.update(
                "UPDATE circle_member SET role = ? WHERE circle_id = ? AND user_id = ?",
                role, circleId, userId
        );
    }

    public int deleteByCircleId(Long circleId) {
        return SqlUtil.update(
                "DELETE FROM circle_member WHERE circle_id = ?",
                circleId
        );
    }
}
