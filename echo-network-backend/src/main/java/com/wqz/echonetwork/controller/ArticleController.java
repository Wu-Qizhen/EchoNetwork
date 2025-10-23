package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.ArticleUpdateRequest;
import com.wqz.echonetwork.entity.po.Article;
import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.ArticleService;
import com.wqz.echonetwork.service.impl.ArticleServiceImpl;
import com.wqz.echonetwork.utils.JsonUtil;
import com.wqz.echonetwork.utils.JwtUtil;
import com.wqz.echonetwork.utils.WriterUtil;
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
@WebServlet("/api/articles/*")
public class ArticleController extends HttpServlet {

    ArticleService articleService = new ArticleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        handleCreateArticle(request, response);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        handleUpdateArticle(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        handleDeleteArticle(request, response);
    }

    private void handleGetArticleList(HttpServletRequest request, HttpServletResponse response) {
        // TODO
        /* int page = Integer.parseInt(request.getParameter("page") != null ? request.getParameter("page") : "1");
        // ... 其他参数解析
        // 调用服务层获取列表
        ArticleListDTO list = articleService.getArticleList(page, 20, ...);
        // 返回JSON响应
        sendJsonResponse(response, 200, "成功", list); */
    }

    private void handleGetArticleDetail(HttpServletRequest request, HttpServletResponse response) {
        // TODO
        /* String idStr = request.getPathInfo().substring(1); // 获取 /api/articles/456 中的 456
        Long articleId = Long.parseLong(idStr);
        ArticleDetailDTO detail = articleService.getArticleDetail(articleId);
        sendJsonResponse(response, 200, "成功", detail); */
    }

    private void handleCreateArticle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long currentUserId = JwtUtil.getCurrentUserId(request);
        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        ArticleUpdateRequest articleUpdateRequest = JsonUtil.parseJson(request, ArticleUpdateRequest.class);

        Article article = articleService.create(articleUpdateRequest, currentUserId);

        if (article == null) {
            Result<Object> result = Result.error("文章创建失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<Article> result = Result.success("文章创建成功", article);
        WriterUtil.writeJson(response, result);
    }

    private void handleUpdateArticle(HttpServletRequest request, HttpServletResponse response) {
        // TODO
        /* String idStr = request.getPathInfo().substring(1);
        Long articleId = Long.parseLong(idStr);
        ArticleUpdateDTO dto = parseJsonRequest(request, ArticleUpdateDTO.class);
        ArticleDTO updated = articleService.updateArticle(articleId, dto);
        sendJsonResponse(response, 200, "文章更新成功", updated); */
    }

    private void handleDeleteArticle(HttpServletRequest request, HttpServletResponse response) {
        // TODO
        /* String idStr = request.getPathInfo().substring(1);
        Long articleId = Long.parseLong(idStr);
        articleService.deleteArticle(articleId);
        sendJsonResponse(response, 200, "文章删除成功", null); */
    }
}
