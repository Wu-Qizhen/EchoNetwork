package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.Circle;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.22
 */
public class CircleMapper {

    public long insert(Circle circle) {
        return SqlUtil.insert(
                "INSERT INTO circle (name, description, creator_id, create_time) VALUES (?, ?, ?, ?)",
                circle.getName(),
                circle.getDescription(),
                circle.getCreatorId(),
                circle.getCreateTime()
        );
    }

    public Circle findById(Long circleId) {
        return SqlUtil.queryObject(
                "SELECT * FROM circle WHERE id = ?",
                Circle.class,
                circleId
        );
    }

    public List<Circle> findAll() {
        return SqlUtil.queryList(
                "SELECT * FROM circle ORDER BY create_time DESC",
                Circle.class
        );
    }

    public List<Circle> findAll(int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM circle ORDER BY create_time DESC LIMIT ? OFFSET ?",
                Circle.class,
                limit, offset
        );
    }

    public Circle findByName(String name) {
        return SqlUtil.queryObject(
                "SELECT * FROM circle WHERE name = ?",
                Circle.class,
                name
        );
    }

    public List<Circle> findByName(String keyword, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM circle WHERE name LIKE ? OR description LIKE ? ORDER BY create_time DESC LIMIT ? OFFSET ?",
                Circle.class,
                "%" + keyword + "%", "%" + keyword + "%", limit, offset
        );
    }

    public int countAll() {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM circle",
                Integer.class
        );
        return count != null ? count : 0;
    }

    public int countByName(String keyword) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM circle WHERE name LIKE ? OR description LIKE ?",
                Integer.class,
                "%" + keyword + "%", "%" + keyword + "%"
        );
        return count != null ? count : 0;
    }

    public int updateMemberCount(Long circleId, int delta) {
        String sql = delta > 0 ?
                "UPDATE circle SET member_count = member_count + 1 WHERE id = ?" :
                "UPDATE circle SET member_count = GREATEST(0, member_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, circleId);
    }

    public int updateArticleCount(Long circleId, int delta) {
        String sql = delta > 0 ?
                "UPDATE circle SET article_count = article_count + 1 WHERE id = ?" :
                "UPDATE circle SET article_count = GREATEST(0, article_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, circleId);
    }

    public int delete(Long circleId) {
        return SqlUtil.update("DELETE FROM circle WHERE id = ?", circleId);
    }

    public List<Circle> findByCreatorId(Long creatorId) {
        return SqlUtil.queryList(
                "SELECT * FROM circle WHERE creator_id = ?",
                Circle.class,
                creatorId
        );
    }

    public List<Circle> search(String keyword) {
        return SqlUtil.queryList(
                "SELECT * FROM circle WHERE name LIKE ? OR description LIKE ?",
                Circle.class,
                "%" + keyword + "%",
                "%" + keyword + "%"
        );
    }

    public List<Circle> findLatest(int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM circle ORDER BY create_time DESC LIMIT ?",
                Circle.class,
                limit
        );
    }

    public int update(Circle circle) {
        return SqlUtil.update(
                "UPDATE circle SET name = ?, description = ?, creator_id = ? WHERE id = ?",
                circle.getName(),
                circle.getDescription(),
                circle.getCreatorId(),
                circle.getId()
        );
    }

    public boolean existsByName(String name) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM circle WHERE name = ?",
                Integer.class,
                name
        );
        return count != null && count > 0;
    }

    public List<Circle> findByCreatorIdWithPagination(Long creatorId, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM circle WHERE creator_id = ? ORDER BY create_time DESC LIMIT ? OFFSET ?",
                Circle.class,
                creatorId,
                limit,
                offset
        );
    }

    public List<Circle> searchWithPagination(String keyword, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM circle WHERE name LIKE ? OR description LIKE ? ORDER BY create_time DESC LIMIT ? OFFSET ?",
                Circle.class,
                "%" + keyword + "%",
                "%" + keyword + "%",
                limit,
                offset
        );
    }
}
