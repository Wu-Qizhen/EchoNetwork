package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.Circle;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public List<Circle> findByConditions(Map<String, Object> conditions, int offset, int limit) {
        StringBuilder sql = new StringBuilder("SELECT c.* FROM circle c WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // 构建 WHERE 条件
        buildWhereClause(conditions, sql, params);

        // 构建排序
        buildOrderClause(conditions, sql);

        // 添加分页
        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        return SqlUtil.queryList(sql.toString(), Circle.class, params.toArray());
    }

    public int countByConditions(Map<String, Object> conditions) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM circle c WHERE 1=1");
        List<Object> params = new ArrayList<>();

        buildWhereClause(conditions, sql, params);

        Integer count = SqlUtil.queryScalar(sql.toString(), Integer.class, params.toArray());
        return count != null ? count : 0;
    }

    // TODO 提取工具类
    /**
     * 构建 WHERE 子句
     */
    private void buildWhereClause(Map<String, Object> conditions, StringBuilder sql, List<Object> params) {
        if (conditions.containsKey("keyword")) {
            sql.append(" AND (c.name LIKE ? OR c.description LIKE ?)");
            params.add(conditions.get("keyword"));
            params.add(conditions.get("keyword"));
        }

        if (conditions.containsKey("creatorId")) {
            sql.append(" AND c.creator_id = ?");
            params.add(conditions.get("creatorId"));
        }

        if (conditions.containsKey("memberId")) {
            // 查询用户加入的圈子
            sql.append(" AND c.id IN (SELECT circle_id FROM circle_member WHERE user_id = ?)");
            params.add(conditions.get("memberId"));
        }
    }

    /**
     * 构建排序子句
     */
    private void buildOrderClause(Map<String, Object> conditions, StringBuilder sql) {
        String sortBy = (String) conditions.getOrDefault("sortBy", "create_time");
        String sortOrder = (String) conditions.getOrDefault("sortOrder", "DESC");

        // 驼峰转下划线
        sortBy = camelToUnderscore(sortBy);

        // 防止 SQL 注入
        Set<String> allowedSortFields = Set.of("create_time", "name", "member_count", "article_count");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "create_time";
        }

        sql.append(" ORDER BY c.").append(sortBy).append(" ").append(sortOrder);
    }

    /**
     * 驼峰命名转下划线命名
     */
    private String camelToUnderscore(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < camelCase.length(); i++) {
            char c = camelCase.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) {
                    result.append("_");
                }
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }
}
