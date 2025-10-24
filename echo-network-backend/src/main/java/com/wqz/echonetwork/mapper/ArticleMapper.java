package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.Article;
import com.wqz.echonetwork.entity.po.ArticleTag;
import com.wqz.echonetwork.entity.po.Tag;
import com.wqz.echonetwork.utils.JsonUtil;
import com.wqz.echonetwork.utils.SqlUtil;

import java.time.LocalDateTime;
import java.util.List;

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
                "SELECT * FROM article WHERE tag_ids LIKE ? AND status = 1",
                Article.class,
                "%" + tagId + "%"
        );
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

    public List<Article> findRecommend() {
        return SqlUtil.queryList(
                "SELECT * FROM article WHERE status = 1 ORDER BY RAND() LIMIT 5",
                Article.class
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

    public int countArticlesByUserId(Long userId) {
        return SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM article WHERE author_id = ?",
                Integer.class,
                userId
        );
    }

    // ArticleTag 关联表操作
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

    // 批量操作
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

    // 统计方法
    public int countArticlesByTagId(Long tagId) {
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
}
