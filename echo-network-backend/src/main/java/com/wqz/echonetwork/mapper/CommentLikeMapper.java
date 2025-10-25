package com.wqz.echonetwork.mapper;

import com.wqz.echonetwork.entity.po.CommentLike;
import com.wqz.echonetwork.utils.SqlUtil;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
public class CommentLikeMapper {

    public long insert(CommentLike commentLike) {
        return SqlUtil.insert(
                "INSERT INTO comment_like (user_id, comment_id, create_time) VALUES (?, ?, ?)",
                commentLike.getUserId(),
                commentLike.getCommentId(),
                commentLike.getCreateTime()
        );
    }

    public int delete(Long userId, Long commentId) {
        return SqlUtil.update(
                "DELETE FROM comment_like WHERE user_id = ? AND comment_id = ?",
                userId, commentId
        );
    }

    public boolean exists(Long userId, Long commentId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM comment_like WHERE user_id = ? AND comment_id = ?",
                Integer.class,
                userId, commentId
        );
        return count != null && count > 0;
    }

    public int countByCommentId(Long commentId) {
        Integer count = SqlUtil.queryScalar(
                "SELECT COUNT(*) FROM comment_like WHERE comment_id = ?",
                Integer.class,
                commentId
        );
        return count != null ? count : 0;
    }

    public List<CommentLike> findByUserId(Long userId) {
        return SqlUtil.queryList(
                "SELECT * FROM comment_like WHERE user_id = ? ORDER BY create_time DESC",
                CommentLike.class,
                userId
        );
    }

    public int deleteByCommentId(Long commentId) {
        return SqlUtil.update(
                "DELETE FROM comment_like WHERE comment_id = ?",
                commentId
        );
    }
}
