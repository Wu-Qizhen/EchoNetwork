package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.Comment;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
public class CommentMapper {

    public long insert(Comment comment) {
        return SqlUtil.insert(
                "INSERT INTO comment (content, create_time, like_count, article_id, user_id) VALUES (?, ?, ?, ?, ?)",
                comment.getContent(),
                comment.getCreateTime(),
                comment.getLikeCount(),
                comment.getArticleId(),
                comment.getUserId()
        );
    }

    public int delete(Long commentId) {
        return SqlUtil.update("DELETE FROM comment WHERE id = ?", commentId);
    }

    public Comment findById(Long commentId) {
        return SqlUtil.queryObject(
                "SELECT * FROM comment WHERE id = ?",
                Comment.class,
                commentId
        );
    }

    public List<Comment> findByArticleId(Long articleId, int offset, int limit) {
        return SqlUtil.queryList(
                "SELECT * FROM comment WHERE article_id = ? ORDER BY create_time DESC LIMIT ? OFFSET ?",
                Comment.class,
                articleId, limit, offset
        );
    }

    public int countByArticleId(Long articleId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM comment WHERE article_id = ?",
                Integer.class,
                articleId
        );
        return count != null ? count : 0;
    }

    public List<Comment> findByUserId(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM comment WHERE user_id = ? ORDER BY create_time DESC",
                Comment.class,
                userId
        );
    }

    public int updateLikeCount(Long commentId, int delta) {
        String sql = delta > 0 ?
                "UPDATE comment SET like_count = like_count + 1 WHERE id = ?" :
                "UPDATE comment SET like_count = GREATEST(0, like_count - 1) WHERE id = ?";

        return SqlUtil.update(sql, commentId);
    }

    public boolean exists(Long commentId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM comment WHERE id = ?",
                Integer.class,
                commentId
        );
        return count != null && count > 0;
    }
}
