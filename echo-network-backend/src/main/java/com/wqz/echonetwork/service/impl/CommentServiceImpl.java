package com.wqz.echonetwork.service.impl;

import com.wqz.echonetwork.entity.dto.CommentCreateRequest;
import com.wqz.echonetwork.entity.dto.CommentInteractionResponse;
import com.wqz.echonetwork.entity.dto.UserProfileResponse;
import com.wqz.echonetwork.entity.po.Article;
import com.wqz.echonetwork.entity.po.Comment;
import com.wqz.echonetwork.entity.po.CommentLike;
import com.wqz.echonetwork.entity.vo.CommentVO;
import com.wqz.echonetwork.entity.vo.PageResult;
import com.wqz.echonetwork.entity.vo.UserVO;
import com.wqz.echonetwork.mapper.ArticleMapper;
import com.wqz.echonetwork.mapper.CommentLikeMapper;
import com.wqz.echonetwork.mapper.CommentMapper;
import com.wqz.echonetwork.service.CommentService;
import com.wqz.echonetwork.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
public class CommentServiceImpl implements CommentService {

    private final CommentMapper commentMapper = new CommentMapper();
    private final CommentLikeMapper commentLikeMapper = new CommentLikeMapper();
    private final ArticleMapper articleMapper = new ArticleMapper();
    private final UserService userService = new UserServiceImpl();

    @Override
    public CommentVO createComment(CommentCreateRequest request, Long userId) {
        // 验证文章是否存在
        Article article = articleMapper.findById(request.getArticleId());
        if (article == null || article.getStatus() != 1) {
            return null;
        }

        // 创建评论
        Comment comment = new Comment();
        comment.setContent(Objects.requireNonNull(request.getContent()));
        comment.setArticleId(request.getArticleId());
        comment.setUserId(userId);
        comment.setLikeCount(0);

        long commentId = commentMapper.insert(comment);
        if (commentId > 0) {
            // 更新文章的评论计数
            articleMapper.updateCommentCount(request.getArticleId(), 1);

            // 返回评论详情
            return getCommentVO(commentId, userId);
        }

        return null;
    }

    @Override
    public PageResult<CommentVO> getCommentsByArticleId(Long articleId, int page, int size, Long currentUserId) {
        // 验证文章是否存在
        Article article = articleMapper.findById(articleId);
        if (article == null || article.getStatus() != 1) {
            return new PageResult<>(new ArrayList<>(), 0, page, size);
        }

        // 计算分页
        int offset = (page - 1) * size;

        // 查询评论列表
        List<Comment> comments = commentMapper.findByArticleId(articleId, offset, size);
        int total = commentMapper.countByArticleId(articleId);

        // 转换为 VO
        List<CommentVO> commentVOs = convertToCommentVOList(comments, currentUserId);

        return new PageResult<>(commentVOs, total, page, size);
    }

    @Override
    public boolean deleteComment(Long commentId, Long userId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            return false;
        }

        // 检查权限：只有评论作者可以删除
        if (!Objects.equals(comment.getUserId(), userId)) {
            return false;
        }

        // 删除评论点赞记录
        int likeDeleted = commentLikeMapper.deleteByCommentId(commentId);

        // 删除评论
        int deleted = commentMapper.delete(commentId);
        if (likeDeleted > 0 && deleted > 0) {
            // 更新文章的评论计数
            articleMapper.updateCommentCount(comment.getArticleId(), -1);
            return true;
        }

        return false;
    }

    @Override
    public CommentInteractionResponse likeComment(Long commentId, Long userId) {
        // 检查评论是否存在
        if (!commentMapper.exists(commentId)) {
            return null;
        }

        // 检查是否已经点赞
        boolean alreadyLiked = commentLikeMapper.exists(userId, commentId);
        if (alreadyLiked) {
            return getCommentInteractionStatus(commentId, userId);
        }

        // 创建点赞记录
        CommentLike commentLike = new CommentLike();
        commentLike.setUserId(userId);
        commentLike.setCommentId(commentId);

        long likeId = commentLikeMapper.insert(commentLike);
        if (likeId > 0) {
            // 更新评论的点赞计数
            int result = commentMapper.updateLikeCount(commentId, 1);
            if (result > 0) {
                return getCommentInteractionStatus(commentId, userId);
            }
            return null;
        }

        return null;
    }

    @Override
    public CommentInteractionResponse unlikeComment(Long commentId, Long userId) {
        // 检查是否已经点赞
        boolean alreadyLiked = commentLikeMapper.exists(userId, commentId);
        if (!alreadyLiked) {
            return getCommentInteractionStatus(commentId, userId);
        }

        // 删除点赞记录
        int deleted = commentLikeMapper.delete(userId, commentId);
        if (deleted > 0) {
            // 更新评论的点赞计数
            int result = commentMapper.updateLikeCount(commentId, -1);
            if (result > 0) {
                return getCommentInteractionStatus(commentId, userId);
            }
            return null;
        }

        return null;
    }

    @Override
    public CommentInteractionResponse getCommentInteractionStatus(Long commentId, Long userId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            return null;
        }

        boolean isLiked = commentLikeMapper.exists(userId, commentId);

        return new CommentInteractionResponse(
                commentId,
                comment.getLikeCount(),
                isLiked
        );
    }

    @Override
    public List<CommentVO> getCommentsByUserId(Long userId) {
        List<Comment> comments = commentMapper.findByUserId(userId);
        return convertToCommentVOList(comments, userId);
    }

    private CommentVO getCommentVO(Long commentId, Long currentUserId) {
        Comment comment = commentMapper.findById(commentId);
        if (comment == null) {
            return null;
        }

        UserProfileResponse profile = userService.getProfile(comment.getUserId(), comment.getUserId());
        if (profile == null) {
            return null;
        }

        UserVO userVO = profile.getUser();
        boolean isLiked = currentUserId != null && commentLikeMapper.exists(currentUserId, commentId);

        return new CommentVO(
                commentId,
                comment.getContent(),
                comment.getCreateTime(),
                comment.getLikeCount(),
                userVO,
                comment.getArticleId(),
                isLiked
        );
    }

    private List<CommentVO> convertToCommentVOList(List<Comment> comments, Long currentUserId) {
        if (comments == null || comments.isEmpty()) {
            return new ArrayList<>();
        }

        // 批量获取用户信息
        Set<Long> userIds = comments.stream()
                .map(Comment::getUserId)
                .collect(Collectors.toSet());

        Map<Long, UserProfileResponse> userProfiles = new HashMap<>();
        for (Long userId : userIds) {
            UserProfileResponse profile = userService.getProfile(userId, userId);
            if (profile != null) {
                userProfiles.put(userId, profile);
            }
        }

        // 批量获取点赞状态
        Set<Long> commentIds = comments.stream()
                .map(Comment::getId)
                .collect(Collectors.toSet());

        Map<Long, Boolean> likeStatusMap = new HashMap<>();
        if (currentUserId != null) {
            for (Long commentId : commentIds) {
                boolean isLiked = commentLikeMapper.exists(currentUserId, commentId);
                likeStatusMap.put(commentId, isLiked);
            }
        }

        // 构建 VO 列表
        return comments.stream().map(comment -> {
            UserProfileResponse profile = userProfiles.get(comment.getUserId());
            if (profile == null) {
                return null;
            }

            UserVO userVO = profile.getUser();
            boolean isLiked = likeStatusMap.getOrDefault(comment.getId(), false);

            return new CommentVO(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreateTime(),
                    comment.getLikeCount(),
                    userVO,
                    comment.getArticleId(),
                    isLiked
            );
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
