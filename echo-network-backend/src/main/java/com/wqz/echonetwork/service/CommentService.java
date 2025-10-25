package com.wqz.echonetwork.service;

import com.wqz.echonetwork.entity.dto.CommentCreateRequest;
import com.wqz.echonetwork.entity.dto.CommentInteractionResponse;
import com.wqz.echonetwork.entity.vo.CommentVO;
import com.wqz.echonetwork.entity.vo.PageResult;

import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
public interface CommentService {

    CommentVO createComment(CommentCreateRequest request, Long userId);

    PageResult<CommentVO> getCommentsByArticleId(Long articleId, int page, int size, Long currentUserId);

    boolean deleteComment(Long commentId, Long userId);

    CommentInteractionResponse likeComment(Long commentId, Long userId);

    CommentInteractionResponse unlikeComment(Long commentId, Long userId);

    CommentInteractionResponse getCommentInteractionStatus(Long commentId, Long userId);

    List<CommentVO> getCommentsByUserId(Long userId);
}
