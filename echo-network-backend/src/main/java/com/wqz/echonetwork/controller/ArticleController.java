package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.ArticleInteractionResponse;
import com.wqz.echonetwork.entity.dto.ArticleQueryRequest;
import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.vo.ArticleVO;
import com.wqz.echonetwork.entity.vo.CommentVO;
import com.wqz.echonetwork.entity.vo.PageResult;
import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.ArticleService;
import com.wqz.echonetwork.service.CommentService;
import com.wqz.echonetwork.service.impl.ArticleServiceImpl;
import com.wqz.echonetwork.service.impl.CommentServiceImpl;
import com.wqz.echonetwork.utils.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
@WebServlet({
        "/api/articles/*"
})
public class ArticleController extends HttpServlet {

    ArticleService articleService = new ArticleServiceImpl();
    CommentService commentService = new CommentServiceImpl();

    /* @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path == null || path.equals("/")) {
            handleGetArticleList(request, response);
        } else if (path.matches("/\\d+")) {
            handleGetArticleDetail(request, response);
        } else if (path.equals("/liked")) {
            handleGetLikedArticles(request, response);
        } else if (path.equals("/starred")) {
            handleGetStarredArticles(request, response);
        } else {
            WriterUtil.paramsError(response);
        }
    } */

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path == null || path.equals("/")) {
            handleGetArticleList(request, response);
        } else if (path.matches("/\\d+")) {
            handleGetArticleDetail(request, response);
        } else if (path.matches("/\\d+/comments")) {
            handleGetArticleComments(request, response);
        } else if (path.equals("/liked")) {
            handleGetLikedArticles(request, response);
        } else if (path.equals("/starred")) {
            handleGetStarredArticles(request, response);
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
            handleCreateArticle(request, response);
        } else if (path.matches("/\\d+/like")) {
            handleLikeArticle(request, response);
        } else if (path.matches("/\\d+/star")) {
            handleStarArticle(request, response);
        } else {
            WriterUtil.paramsError(response);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        /* String path = request.getPathInfo();
        if (path != null && path.matches("^/\\d+$")) {
            handleUpdateArticle(request, response);
        } else if (path != null && path.matches("^/delete/\\d+$")) {
            handleDeleteArticle(request, response);
        } else {
            WriterUtil.paramsError(response);
        } */
        handleUpdateArticle(request, response);
    }

    /* @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // LogUtil.info("HandleDeleteArticle");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        handleDeleteArticle(request, response);
    } */

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path.matches("/\\d+/like")) {
            handleUnlikeArticle(request, response);
        } else if (path.matches("/\\d+/star")) {
            handleUnstarArticle(request, response);
        } else if (path.matches("/\\d+")) {
            handleDeleteArticle(request, response);
        } else {
            WriterUtil.paramsError(response);
        }
    }

    private void handleGetArticleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // LogUtil.info("HandleGetArticleList");

            // 检查是否是获取圈子文章的请求
            String circleIdStr = request.getParameter("circleId");
            if (circleIdStr != null) {
                handleGetCircleArticles(request, response);
                return;
            }

            // 解析查询参数
            ArticleQueryRequest queryRequest = parseQueryParameters(request);
            // LogUtil.info("QueryParameters: " + queryRequest);

            // 判断是否是推荐请求
            String recommend = request.getParameter("recommend");
            // LogUtil.info("Recommend: " + recommend);
            if ("true".equalsIgnoreCase(recommend)) {
                handleGetRecommendArticles(request, response);
                return;
            }

            // 多条件查询
            PageResult<ArticleVO> pageResult = articleService.getArticlesByConditions(queryRequest);
            // LogUtil.info("ArticleList: " + pageResult);

            // 构建响应
            Map<String, Object> data = new HashMap<>();
            data.put("list", pageResult.getList());
            data.put("total", pageResult.getTotal());
            data.put("page", pageResult.getPage());
            data.put("size", pageResult.getSize());
            data.put("totalPages", pageResult.getTotalPages());

            Result<Map<String, Object>> result = Result.success("成功", data);
            WriterUtil.writeJson(response, result);

        } catch (Exception e) {
            LogUtil.error("获取文章列表失败：" + e.getMessage());
            Result<Object> result = Result.error("获取文章列表失败");
            WriterUtil.writeJson(response, result);
        }
    }

    private void handleGetRecommendArticles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String limitStr = request.getParameter("limit");
        int limit = limitStr != null ? Integer.parseInt(limitStr) : 10;

        // 尝试根据用户 ID 进行个性化推荐
        // Long currentUserId = JwtUtil.getCurrentUserId(request);
        List<ArticleVO> recommendArticles;

        /* if (currentUserId != null) {
            // TODO 个性化推荐（实现 findRecommendByUserInterest）
            recommendArticles = articleService.getRecommendByUserInterest(currentUserId, limit);
        } else { */
        // 通用推荐
        recommendArticles = articleService.getRecommend(limit);
        /* } */

        Result<List<ArticleVO>> result = Result.success("推荐文章获取成功", recommendArticles);
        WriterUtil.writeJson(response, result);
    }

    private ArticleQueryRequest parseQueryParameters(HttpServletRequest request) {
        ArticleQueryRequest queryRequest = new ArticleQueryRequest();

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        String authorIdStr = request.getParameter("authorId");
        String circleIdStr = request.getParameter("circleId");
        String tagIdStr = request.getParameter("tagId");
        String statusStr = request.getParameter("status");

        if (pageStr != null) queryRequest.setPage(Integer.parseInt(pageStr));
        if (sizeStr != null) queryRequest.setSize(Integer.parseInt(sizeStr));
        if (authorIdStr != null) queryRequest.setAuthorId(Long.parseLong(authorIdStr));
        if (circleIdStr != null) queryRequest.setCircleId(Long.parseLong(circleIdStr));
        if (tagIdStr != null) queryRequest.setTagId(Long.parseLong(tagIdStr));
        if (statusStr != null) queryRequest.setStatus(Integer.parseInt(statusStr));

        queryRequest.setKeyword(request.getParameter("keyword"));
        queryRequest.setSortBy(request.getParameter("sortBy"));
        queryRequest.setSortOrder(request.getParameter("sortOrder"));

        return queryRequest;
    }

    private void handleGetArticleDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // String idStr = request.getPathInfo().substring(1);
        // Long articleId = Long.parseLong(idStr);
        Long articleId = PathUtil.getIdFromPath(request);
        if (articleId == null) {
            WriterUtil.paramsError(response);
            return;
        }
        ArticleVO article = articleService.getArticle(articleId, JwtUtil.getCurrentUserId(request));
        if (article == null) {
            Result<Object> result = Result.error("文章不存在");
            WriterUtil.writeJson(response, result);
            return;
        }

        String increase = articleService.increaseViewCount(articleId);
        if (increase != null) {
            LogUtil.error("文章浏览数增加失败：" + increase);
            article.setViewCount(article.getViewCount() + 1);
        }

        /* ArticleInteractionResponse articleInteractionStatus = articleService.getArticleInteractionStatus(articleId, JwtUtil.getCurrentUserId(request));
        article.setLiked(articleInteractionStatus.isLiked());
        article.setStarred(articleInteractionStatus.isStarred()); */

        Result<ArticleVO> result = Result.success("文章获取成功", article);
        WriterUtil.writeJson(response, result);
    }

    private void handleCreateArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long currentUserId = JwtUtil.getCurrentUserId(request);
        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        ArticleUpdateRequest articleUpdateRequest = JsonUtil.parseJson(request, ArticleUpdateRequest.class);

        ArticleVO article = articleService.create(articleUpdateRequest, currentUserId);

        if (article == null) {
            Result<Object> result = Result.error("文章创建失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<ArticleVO> result = Result.success("文章创建成功", article);
        WriterUtil.writeJson(response, result);
    }

    private void handleUpdateArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long articleId = PathUtil.getIdFromPath(request);
        // LogUtil.info("ArticleId: " + articleId);
        if (articleId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        Long currentUserId = JwtUtil.getCurrentUserId(request);
        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        // LogUtil.info("HandleUpdateArticle");
        ArticleUpdateRequest articleUpdateRequest = JsonUtil.parseJson(request, ArticleUpdateRequest.class);
        // LogUtil.info("ArticleUpdateRequest: " + articleUpdateRequest);
        ArticleVO update = articleService.update(articleUpdateRequest, currentUserId, articleId);
        // LogUtil.info("Update: " + update);

        if (update == null) {
            Result<Object> result = Result.error("文章更新失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<ArticleVO> result = Result.success("文章更新成功", update);
        WriterUtil.writeJson(response, result);
    }

    private void handleDeleteArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long articleId = PathUtil.getIdFromPath(request);
        // LogUtil.info("ArticleId: " + articleId);

        if (articleId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        String message = articleService.delete(articleId, currentUserId);

        if (message != null) {
            Result<Object> result = Result.error(message);
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<Object> result = Result.success("文章删除成功");
        WriterUtil.writeJson(response, result);
    }

    /**
     * 点赞文章
     */
    private void handleLikeArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // LogUtil.info("HandleLikeArticle");
        Long articleId = PathUtil.getIdFromPath(request);
        // LogUtil.info("ArticleId: " + articleId);
        Long currentUserId = JwtUtil.getCurrentUserId(request);
        // LogUtil.info("CurrentUserId: " + currentUserId);

        if (articleId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        ArticleInteractionResponse interaction = articleService.likeArticle(articleId, currentUserId);
        if (interaction == null) {
            Result<Object> result = Result.error("点赞失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<ArticleInteractionResponse> result = Result.success("点赞成功", interaction);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 取消点赞
     */
    private void handleUnlikeArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long articleId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (articleId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        ArticleInteractionResponse interaction = articleService.unlikeArticle(articleId, currentUserId);
        if (interaction == null) {
            Result<Object> result = Result.error("取消点赞失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<ArticleInteractionResponse> result = Result.success("取消点赞成功", interaction);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 收藏文章
     */
    private void handleStarArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long articleId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (articleId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        ArticleInteractionResponse interaction = articleService.starArticle(articleId, currentUserId);
        if (interaction == null) {
            Result<Object> result = Result.error("收藏失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<ArticleInteractionResponse> result = Result.success("收藏成功", interaction);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 取消收藏
     */
    private void handleUnstarArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long articleId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (articleId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        ArticleInteractionResponse interaction = articleService.unstarArticle(articleId, currentUserId);
        if (interaction == null) {
            Result<Object> result = Result.error("取消收藏失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<ArticleInteractionResponse> result = Result.success("取消收藏成功", interaction);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 获取用户点赞的文章列表
     */
    private void handleGetLikedArticles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long currentUserId = JwtUtil.getCurrentUserId(request);
        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        List<ArticleVO> likedArticles = articleService.getLikedArticles(currentUserId);
        Result<List<ArticleVO>> result = Result.success("获取点赞文章成功", likedArticles);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 获取用户收藏的文章列表
     */
    private void handleGetStarredArticles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long currentUserId = JwtUtil.getCurrentUserId(request);
        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        List<ArticleVO> starredArticles = articleService.getStarredArticles(currentUserId);
        Result<List<ArticleVO>> result = Result.success("获取收藏文章成功", starredArticles);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 获取文章评论列表（转发到 CommentController）
     */
    private void handleGetArticleComments(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long articleId = PathUtil.getIdFromPath(request);
        if (articleId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        int page = 1;
        int size = 10;

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");

        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        if (sizeStr != null) {
            size = Integer.parseInt(sizeStr);
        }

        Long currentUserId = JwtUtil.getCurrentUserId(request);

        PageResult<CommentVO> pageResult = commentService.getCommentsByArticleId(articleId, page, size, currentUserId);

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
     * 获取圈子文章列表
     */
    private void handleGetCircleArticles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long circleId = Long.parseLong(request.getParameter("circleId"));
        int page = 1;
        int size = 10;

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");

        if (pageStr != null) page = Integer.parseInt(pageStr);
        if (sizeStr != null) size = Integer.parseInt(sizeStr);

        ArticleQueryRequest queryRequest = new ArticleQueryRequest();
        queryRequest.setPage(page);
        queryRequest.setSize(size);
        queryRequest.setCircleId(circleId);
        queryRequest.setStatus(1);

        PageResult<ArticleVO> pageResult = articleService.getArticlesByConditions(queryRequest);

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
}
