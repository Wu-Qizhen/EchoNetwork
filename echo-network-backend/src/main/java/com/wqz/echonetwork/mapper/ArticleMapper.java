package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.Article;
import com.wqz.echonetwork.entity.po.ArticleTag;
import com.wqz.echonetwork.entity.po.Tag;
import com.wqz.echonetwork.utils.JsonUtil;
import com.wqz.echonetwork.utils.SqlUtil;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.22
 */
public class ArticleMapper {

    public long insert(Article article) {
        return SqlUtil.insert(
                "INSERT INTO article (title, " +
                        "content, " +
                        "create_time, " +
                        "update_time, " +
                        "publish_time, " +
                        "status, " +
                        "view_count, " +
                        "like_count, " +
                        "star_count, " +
                        "comment_count, " +
                        "author_id, " +
                        "circle_id, " +
                        "tag_ids) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                article.getTitle(),
                article.getContent(),
                article.getCreateTime(),
                article.getUpdateTime(),
                article.getPublishTime(),
                article.getStatus(),
                article.getViewCount(),
                article.getLikeCount(),
                article.getStarCount(),
                article.getCommentCount(),
                article.getAuthorId(),
                article.getCircleId(),
                JsonUtil.toJsonString(article.getTagIds())
        );
    }

    public int update(Article article) {
        return SqlUtil.update(
                "UPDATE article SET title = ?, " +
                        "content = ?, " +
                        "update_time = ?, " +
                        "publish_time = ?, " +
                        "status = ?, " +
                        "view_count = ?, " +
                        "like_count = ?, " +
                        "star_count = ?, " +
                        "comment_count = ?, " +
                        "circle_id = ?, " +
                        "tag_ids = ? WHERE id = ?",
                article.getTitle(),
                article.getContent(),
                article.getUpdateTime(),
                article.getPublishTime(),
                article.getStatus(),
                article.getViewCount(),
                article.getLikeCount(),
                article.getStarCount(),
                article.getCommentCount(),
                article.getCircleId(),
                JsonUtil.toJsonString(article.getTagIds()),
                article.getId()
        );
    }

    public Article findById(Long id) {
        return SqlUtil.queryObject(
                "SELECT * FROM article WHERE id = ?",
                Article.class,
                id
        );
    }

    public Article findByIdPublished(Long id) {
        return SqlUtil.queryObject(
                "SELECT * FROM article WHERE id = ? AND status = 1",
                Article.class,
                id
        );
    }

    public List<Article> findByAuthorId(Long authorId) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE author_id = ?",
                Article.class,
                authorId
        );
    }

    public List<Article> findByAuthorIdPublished(Long authorId) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE author_id = ? AND status = 1",
                Article.class,
                authorId
        );
    }

    public List<Article> findByAuthorIdPublishedWithPagination(Long authorId, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE author_id = ? AND status = 1 LIMIT ? OFFSET ?",
                Article.class,
                authorId,
                limit,
                offset
        );
    }

    public List<Article> findByCircleId(Long circleId) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE circle_id = ? AND status = 1",
                Article.class,
                circleId
        );
    }

    public List<Article> findByTagId(Long tagId) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE JSON_CONTAINS(tag_ids, ?) AND status = 1",
                Article.class,
                tagId.toString()  // 要转为字符串
        );
        /* return SqlUtil.queryList(
                "SELECT * FROM article WHERE tag_ids LIKE ? AND status = 1",
                Article.class,
                "%" + tagId + "%"
        ); */
    }

    public List<Article> search(String keyword) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE (title LIKE ? OR content LIKE ?) AND status = 1",
                Article.class,
                "%" + keyword + "%",
                "%" + keyword + "%"
        );
    }

    public List<Article> searchWithPagination(String keyword, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE (title LIKE ? OR content LIKE ?) AND status = 1 LIMIT ? OFFSET ?",
                Article.class,
                "%" + keyword + "%",
                "%" + keyword + "%",
                limit,
                offset
        );
    }

    public int delete(Long id) {
        return SqlUtil.update(
                "UPDATE article SET status = 2 WHERE id = ?"
                , id);
    }

    public void updateUpdateTime(Long id, LocalDateTime lastLoginTime) {
        SqlUtil.update("UPDATE article SET update_time = ? WHERE id = ?", lastLoginTime, id);
    }

    public int countByUserId(Long userId) {
        return SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM article WHERE author_id = ?",
                Integer.class,
                userId
        );
    }

    /**
     * 根据条件查询文章
     */
    public List<Article> findByConditions(Map<String, Object> conditions, int offset, int limit) {
        StringBuilder sql = new StringBuilder(
                "SELECT DISTINCT a.* FROM article a " +
                        "LEFT JOIN article_tag at ON a.id = at.article_id " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();

        // 构建 WHERE 条件
        buildWhereClause(conditions, sql, params);

        // 构建排序
        buildOrderClause(conditions, sql);

        // 添加分页
        sql.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);

        return SqlUtil.queryList(sql.toString(), Article.class, params.toArray());
    }

    /**
     * 统计符合条件的文章数量
     */
    public int countByConditions(Map<String, Object> conditions) {
        StringBuilder sql = new StringBuilder(
                "SELECT COUNT(DISTINCT a.id) FROM article a " +
                        "LEFT JOIN article_tag at ON a.id = at.article_id " +
                        "WHERE 1=1"
        );

        List<Object> params = new ArrayList<>();
        buildWhereClause(conditions, sql, params);

        Integer count = SqlUtil.queryScalar(sql.toString(), Integer.class, params.toArray());
        return count != null ? count : 0;
    }

    /* public int countByConditions(Map<String, Object> conditions) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM article a WHERE 1=1");

        List<Object> params = new ArrayList<>();

        // 使用相同的条件构建逻辑
        buildWhereClause(conditions, sql, params);

        Integer count = SqlUtil.queryScalar(sql.toString(), Integer.class, params.toArray());
        return count != null ? count : 0;
    } */

    /**
     * 构建 WHERE 子句
     */
    private void buildWhereClause(Map<String, Object> conditions, StringBuilder sql, List<Object> params) {
        if (conditions.containsKey("authorId")) {
            sql.append(" AND a.author_id = ?");
            params.add(conditions.get("authorId"));
        }

        if (conditions.containsKey("circleId")) {
            sql.append(" AND a.circle_id = ?");
            params.add(conditions.get("circleId"));
        }

        if (conditions.containsKey("tagId")) {
            // sql.append(" AND at.tag_id = ?");
            // params.add(conditions.get("tagId"));
            // 使用 JSON_CONTAINS 或 JSON_SEARCH 来查询 JSON 数组
            sql.append(" AND JSON_CONTAINS(a.tag_ids, ?)");
            params.add(conditions.get("tagId").toString()); // tagId 需要转为字符串
        }

        if (conditions.containsKey("status")) {
            sql.append(" AND a.status = ?");
            params.add(conditions.get("status"));
        }

        if (conditions.containsKey("keyword")) {
            sql.append(" AND (a.title LIKE ? OR a.content LIKE ?)");
            params.add(conditions.get("keyword"));
            params.add(conditions.get("keyword"));
        }
    }

    /**
     * 构建排序子句
     */
    private void buildOrderClause(Map<String, Object> conditions, StringBuilder sql) {
        String sortBy = (String) conditions.getOrDefault("sortBy", "publish_time");
        String sortOrder = (String) conditions.getOrDefault("sortOrder", "DESC");

        // 驼峰转下划线
        sortBy = camelToUnderscore(sortBy);

        // 防止 SQL 注入
        Set<String> allowedSortFields = Set.of("create_time", "publish_time", "update_time", "view_count", "like_count", "star_count", "comment_count");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "publish_time";
        }

        sql.append(" ORDER BY a.").append(sortBy).append(" ").append(sortOrder);
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

    /* private void buildOrderClause(Map<String, Object> conditions, StringBuilder sql) {
        String sortBy = (String) conditions.getOrDefault("sortBy", "publish_time");
        String sortOrder = (String) conditions.getOrDefault("sortOrder", "DESC");

        // 防止 SQL 注入
        Set<String> allowedSortFields = Set.of("create_time", "publish_time", "update_time", "view_count", "like_count", "star_count", "comment_count");
        if (!allowedSortFields.contains(sortBy)) {
            sortBy = "publish_time";
        }

        sql.append(" ORDER BY a.").append(sortBy).append(" ").append(sortOrder);
    } */

    /* public List<Article> findRecommend() {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE status = 1 ORDER BY RAND() LIMIT 5",
                Article.class
        );
    } */

    /**
     * 改进的推荐算法：基于热度 + 时效性
     */
    public List<Article> findRecommend(int limit) {
        return SqlUtil.queryList(
                "SELECT a.* FROM article a " +
                        "WHERE a.status = 1 " +
                        "AND a.publish_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " + // 最近 30 天的文章
                        "ORDER BY " +
                        "(a.view_count * 0.3 + a.like_count * 0.4 + a.star_count * 0.2 + a.comment_count * 0.1) * " + // 热度权重
                        "EXP(-0.1 * DATEDIFF(NOW(), a.publish_time)) DESC " + // 时间衰减
                        "LIMIT ?",
                Article.class,
                limit
        );
    }

    /**
     * 根据用户兴趣推荐（需要用户行为数据）
     */
    public List<Article> findRecommendByUserInterest(Long userId, int limit) { // TODO
        return SqlUtil.queryList(
                "SELECT a.* FROM article a " +
                        "WHERE a.status = 1 AND a.author_id != ? " +
                        "AND EXISTS (" +
                        "   SELECT 1 FROM article user_articles " +
                        "   WHERE user_articles.author_id = ? " +
                        "   AND JSON_OVERLAPS(user_articles.tag_ids, a.tag_ids)" +
                        ") " +
                        "ORDER BY a.view_count DESC " +
                        "LIMIT ?",
                Article.class,
                userId, userId, limit
        );
    }

    /* public List<Article> findRecommendByUserInterest(Long userId, int limit) {
        return SqlUtil.queryList(
                "SELECT a.* FROM article a " +
                        "LEFT JOIN (" +
                        "   SELECT at.tag_id, COUNT(*) as tag_count " +
                        "   FROM article_tag at " +
                        "   INNER JOIN article a2 ON at.article_id = a2.id " +
                        "   WHERE a2.author_id = ? OR a2.id IN (" +
                        "       SELECT article_id FROM user_like WHERE user_id = ?" +
                        "   ) " +
                        "   GROUP BY at.tag_id" +
                        ") user_tags ON a.tag_ids LIKE CONCAT('%', user_tags.tag_id, '%') " +
                        "WHERE a.status = 1 AND a.author_id != ? " +
                        "ORDER BY user_tags.tag_count DESC, a.view_count DESC " +
                        "LIMIT ?",
                Article.class,
                userId, userId, userId, limit
        );
    } */

    /* 关联表操作 */
    public int insertArticleTag(ArticleTag articleTag) {
        return SqlUtil.update(
                "INSERT INTO article_tag (article_id, tag_id, create_time) VALUES (?, ?, ?)",
                articleTag.getArticleId(),
                articleTag.getTagId(),
                articleTag.getCreateTime()
        );
    }

    public int deleteArticleTag(Long id) {
        return SqlUtil.update("DELETE FROM article_tag WHERE id = ?", id);
    }

    public int deleteArticleTagsByArticleId(Long articleId) {
        return SqlUtil.update("DELETE FROM article_tag WHERE article_id = ?", articleId);
    }

    public int deleteArticleTagsByTagId(Long tagId) {
        return SqlUtil.update("DELETE FROM article_tag WHERE tag_id = ?", tagId);
    }

    public boolean existsArticleTag(Long articleId, Long tagId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM article_tag WHERE article_id = ? AND tag_id = ?",
                Integer.class,
                articleId,
                tagId
        );
        return count != null && count > 0;
    }

    public List<Tag> findTagsByArticleId(Long articleId) {
        return SqlUtil.queryList(
                "SELECT t.* FROM tag t " +
                        "INNER JOIN article_tag at ON t.id = at.tag_id " +
                        "WHERE at.article_id = ? " +
                        "ORDER BY t.name",
                Tag.class,
                articleId
        );
    }

    public List<Article> findArticlesByTagId(Long tagId) {
        return SqlUtil.queryList(
                "SELECT a.* FROM article a " +
                        "INNER JOIN article_tag at ON a.id = at.article_id " +
                        "WHERE at.tag_id = ? " +
                        "ORDER BY a.create_time DESC",
                Article.class,
                tagId
        );
    }

    public List<Article> findArticlesByTagName(String tagName) {
        return SqlUtil.queryList(
                "SELECT a.* FROM article a " +
                        "INNER JOIN article_tag at ON a.id = at.article_id " +
                        "INNER JOIN tag t ON at.tag_id = t.id " +
                        "WHERE t.name = ? " +
                        "ORDER BY a.create_time DESC",
                Article.class,
                tagName
        );
    }

    public List<ArticleTag> findArticleTagsByArticleId(Long articleId) {
        return SqlUtil.queryList(
                "SELECT * FROM article_tag WHERE article_id = ?",
                ArticleTag.class,
                articleId
        );
    }

    public int batchInsertArticleTags(List<ArticleTag> articleTags) {
        StringBuilder sql = new StringBuilder("INSERT INTO article_tag (article_id, tag_id, create_time) VALUES ");
        Object[] params = new Object[articleTags.size() * 3];

        for (int i = 0; i < articleTags.size(); i++) {
            ArticleTag articleTag = articleTags.get(i);
            sql.append("(?, ?, ?)");
            if (i < articleTags.size() - 1) {
                sql.append(", ");
            }
            params[i * 3] = articleTag.getArticleId();
            params[i * 3 + 1] = articleTag.getTagId();
            params[i * 3 + 2] = articleTag.getCreateTime();
        }

        return SqlUtil.update(sql.toString(), params);
    }

    public int countByTagId(Long tagId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM article_tag WHERE tag_id = ?",
                Integer.class,
                tagId
        );
        return count != null ? count : 0;
    }

    public List<Tag> findPopularTags(int limit) {
        return SqlUtil.queryList(
                "SELECT t.*, COUNT(at.article_id) as article_count " +
                        "FROM tag t " +
                        "INNER JOIN article_tag at ON t.id = at.tag_id " +
                        "GROUP BY t.id, t.name " +
                        "ORDER BY article_count DESC " +
                        "LIMIT ?",
                Tag.class,
                limit
        );
    }

    /**
     * 更新点赞计数
     */
    public int updateLikeCount(Long articleId, int delta) {
        String sql = delta > 0 ?
                "UPDATE article SET like_count = like_count + 1 WHERE id = ?" :
                "UPDATE article SET like_count = GREATEST(0, like_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, articleId);
    }

    /**
     * 更新收藏计数
     */
    public int updateStarCount(Long articleId, int delta) {
        String sql = delta > 0 ?
                "UPDATE article SET star_count = star_count + 1 WHERE id = ?" :
                "UPDATE article SET star_count = GREATEST(0, star_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, articleId);
    }

    /**
     * 批量根据 ID 查询文章
     */
    public List<Article> findByIds(List<Long> articleIds) {
        if (articleIds == null || articleIds.isEmpty()) {
            return new ArrayList<>();
        }

        String placeholders = String.join(",", Collections.nCopies(articleIds.size(), "?"));
        String sql = "SELECT * FROM article WHERE id IN (" + placeholders + ") AND status = 1";

        return SqlUtil.queryList(sql, Article.class, articleIds.toArray());
    }

    /**
     * 获取用户点赞的文章 ID 列表
     */
    public List<Long> findLikedArticleIds(Long userId) {
        return SqlUtil.queryList(
                "SELECT article_id FROM article_like WHERE user_id = ? ORDER BY create_time DESC",
                Long.class,
                userId
        );
    }

    /**
     * 获取用户收藏的文章 ID 列表
     */
    public List<Long> findStarredArticleIds(Long userId) {
        return SqlUtil.queryList(
                "SELECT article_id FROM article_star WHERE user_id = ? ORDER BY create_time DESC",
                Long.class,
                userId
        );
    }

    /**
     * 更新评论计数
     */
    public int updateCommentCount(Long articleId, int delta) {
        String sql = delta > 0 ?
                "UPDATE article SET comment_count = comment_count + 1 WHERE id = ?" :
                "UPDATE article SET comment_count = GREATEST(0, comment_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, articleId);
    }

    /**
     * 统计圈子文章数量
     */
    public int countByCircleId(Long circleId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM article WHERE circle_id = ? AND status = 1",
                Integer.class,
                circleId
        );
        return count != null ? count : 0;
    }

    /**
     * 根据圈子 ID 查询文章
     */
    public List<Article> findByCircleIdWithPagination(Long circleId, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE circle_id = ? AND status = 1 ORDER BY publish_time DESC LIMIT ? OFFSET ?",
                Article.class,
                circleId, limit, offset
        );
    }

    public int updateViewCount(Long articleId, int delta) {
        String sql = delta > 0 ?
                "UPDATE article SET view_count = view_count + 1 WHERE id = ?" :
                "UPDATE article SET view_count = GREATEST(0, view_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, articleId);
    }
}
