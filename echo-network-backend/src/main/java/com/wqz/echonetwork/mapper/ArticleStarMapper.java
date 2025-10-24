package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.ArticleStar;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.24
 */
public class ArticleStarMapper {

    public long insert(ArticleStar articleStar) {
        return SqlUtil.insert(
                "INSERT INTO article_star (user_id, article_id, create_time) VALUES (?, ?, ?)",
                articleStar.getUserId(),
                articleStar.getArticleId(),
                articleStar.getCreateTime()
        );
    }

    public int delete(Long userId, Long articleId) {
        return SqlUtil.update(
                "DELETE FROM article_star WHERE user_id = ? AND article_id = ?",
                userId, articleId
        );
    }

    public boolean exists(Long userId, Long articleId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM article_star WHERE user_id = ? AND article_id = ?",
                Integer.class,
                userId, articleId
        );
        return count != null && count > 0;
    }

    public int countByArticleId(Long articleId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM article_star WHERE article_id = ?",
                Integer.class,
                articleId
        );
        return count != null ? count : 0;
    }

    public List<ArticleStar> findByUserId(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM article_star WHERE user_id = ? ORDER BY create_time DESC",
                ArticleStar.class,
                userId
        );
    }

    public int deleteByArticleId(Long articleId) {
        return SqlUtil.update(
                "DELETE FROM article_star WHERE article_id = ?",
                articleId
        );
    }
}
