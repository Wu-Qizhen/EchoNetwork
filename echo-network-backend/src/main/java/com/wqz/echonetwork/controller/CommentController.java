package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.CommentCreateRequest;
import com.wqz.echonetwork.entity.dto.CommentInteractionResponse;
import com.wqz.echonetwork.entity.vo.CommentVO;
import com.wqz.echonetwork.entity.vo.PageResult;
import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.CommentService;
import com.wqz.echonetwork.service.impl.CommentServiceImpl;
import com.wqz.echonetwork.utils.JsonUtil;
import com.wqz.echonetwork.utils.JwtUtil;
import com.wqz.echonetwork.utils.PathUtil;
import com.wqz.echonetwork.utils.WriterUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.25
 */
@WebServlet({
        "/api/comments",
        "/api/comments/*",
        "/api/articles/*/comments"
})
public class CommentController extends HttpServlet {

    private final CommentService commentService = new CommentServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path == null || path.equals("/")) {
            WriterUtil.paramsError(response);
        } else if (path.matches("/\\d+/comments")) {
            handleGetArticleComments(request, response);
        } else {
            WriterUtil.paramsError(response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path == null || path.equals("/")) {
            handleCreateComment(request, response);
        } else if (path.matches("/\\d+/like")) {
            handleLikeComment(request, response);
        } else {
            WriterUtil.paramsError(response);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path.matches("/\\d+")) {
            handleDeleteComment(request, response);
        } else if (path.matches("/\\d+/like")) {
            handleUnlikeComment(request, response);
        } else {
            WriterUtil.paramsError(response);
        }
    }

    /**
     * 发表评论
     */
    private void handleCreateComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long currentUserId = JwtUtil.getCurrentUserId(request);
        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        CommentCreateRequest createRequest = JsonUtil.parseJson(request, CommentCreateRequest.class);
        if (createRequest == null || createRequest.getArticleId() == null ||
                createRequest.getContent() == null || createRequest.getContent().trim().isEmpty()) {
            WriterUtil.paramsError(response);
            return;
        }

        CommentVO comment = commentService.createComment(createRequest, currentUserId);
        if (comment == null) {
            Result<Object> result = Result.error("评论失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<CommentVO> result = Result.success("评论成功", comment);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 获取文章评论列表
     */
    private void handleGetArticleComments(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long articleId = PathUtil.getIdFromPath(request);
        if (articleId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        // 解析分页参数
        int page = 1;
        int size = 20;

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");

        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        if (sizeStr != null) {
            size = Integer.parseInt(sizeStr);
        }

        // 获取当前用户 ID（用于判断点赞状态）
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        PageResult<CommentVO> pageResult = commentService.getCommentsByArticleId(articleId, page, size, currentUserId);

        // 构建响应
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getList());
        data.put("total", pageResult.getTotal());
        data.put("page", pageResult.getPage());
        data.put("size", pageResult.getSize());
        data.put("totalPages", pageResult.getTotalPages());

        Result<Map<String, Object>> result = Result.success("成功", data);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 删除评论
     */
    private void handleDeleteComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long commentId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (commentId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        boolean success = commentService.deleteComment(commentId, currentUserId);
        if (!success) {
            Result<Object> result = Result.error("删除评论失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<Object> result = Result.success("删除评论成功");
        WriterUtil.writeJson(response, result);
    }

    /**
     * 点赞评论
     */
    private void handleLikeComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long commentId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (commentId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        CommentInteractionResponse interaction = commentService.likeComment(commentId, currentUserId);
        if (interaction == null) {
            Result<Object> result = Result.error("点赞评论失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<CommentInteractionResponse> result = Result.success("点赞评论成功", interaction);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 取消点赞评论
     */
    private void handleUnlikeComment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long commentId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (commentId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        CommentInteractionResponse interaction = commentService.unlikeComment(commentId, currentUserId);
        if (interaction == null) {
            Result<Object> result = Result.error("取消点赞评论失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<CommentInteractionResponse> result = Result.success("取消点赞评论成功", interaction);
        WriterUtil.writeJson(response, result);
    }
}
