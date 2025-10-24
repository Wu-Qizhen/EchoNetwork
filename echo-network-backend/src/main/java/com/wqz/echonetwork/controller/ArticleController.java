package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.vo.ArticleVO;
import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.ArticleService;
import com.wqz.echonetwork.service.impl.ArticleServiceImpl;
import com.wqz.echonetwork.utils.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path == null || path.equals("/")) {
            handleGetArticleList(request, response);
        } else {
            handleGetArticleDetail(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        handleCreateArticle(request, response);
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LogUtil.info("HandleDeleteArticle");
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        handleDeleteArticle(request, response);
    }

    private void handleGetArticleList(HttpServletRequest request, HttpServletResponse response) {

    }

    private void handleGetArticleDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // String idStr = request.getPathInfo().substring(1);
        // Long articleId = Long.parseLong(idStr);
        Long articleId = PathUtil.getIdFromPath(request);
        if (articleId == null) {
            WriterUtil.paramsError(response);
            return;
        }
        ArticleVO article = articleService.getArticle(articleId);
        if (article == null) {
            Result<Object> result = Result.error("文章不存在");
            WriterUtil.writeJson(response, result);
            return;
        }
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
}
