package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.CircleCreateRequest;
import com.wqz.echonetwork.entity.dto.CircleJoinResponse;
import com.wqz.echonetwork.entity.vo.*;
import com.wqz.echonetwork.service.CircleService;
import com.wqz.echonetwork.service.impl.CircleServiceImpl;
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
        "/api/circles",
        "/api/circles/*"
})
public class CircleController extends HttpServlet {

    private final CircleService circleService = new CircleServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path == null || path.equals("/")) {
            handleGetCircles(request, response);
        } else if (path.matches("/\\d+")) {
            handleGetCircleDetail(request, response);
        } else if (path.matches("/\\d+/members")) {
            handleGetCircleMembers(request, response);
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
            handleCreateCircle(request, response);
        } else if (path.matches("/\\d+/join")) {
            handleJoinCircle(request, response);
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

        if (path.matches("/\\d+/exit")) {
            handleExitCircle(request, response);
        } else {
            WriterUtil.paramsError(response);
        }
    }

    /**
     * 创建圈子
     */
    private void handleCreateCircle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long currentUserId = JwtUtil.getCurrentUserId(request);
        if (currentUserId == null) {
            Result<Object> result = Result.error("未提供有效的授权信息");
            WriterUtil.writeJson(response, result);
            return;
        }

        CircleCreateRequest createRequest = JsonUtil.parseJson(request, CircleCreateRequest.class);
        if (createRequest == null || createRequest.getName() == null || createRequest.getName().trim().isEmpty()) {
            WriterUtil.paramsError(response);
            return;
        }

        CircleVO circle = circleService.createCircle(createRequest, currentUserId);
        if (circle == null) {
            Result<Object> result = Result.error("圈子创建失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<CircleVO> result = Result.success("圈子创建成功", circle);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 获取圈子列表
     */
    private void handleGetCircles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 解析查询参数
        int page = 1;
        int size = 20;
        String keyword = null;

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        String keywordParam = request.getParameter("keyword");

        if (pageStr != null) page = Integer.parseInt(pageStr);
        if (sizeStr != null) size = Integer.parseInt(sizeStr);
        if (keywordParam != null && !keywordParam.trim().isEmpty()) {
            keyword = keywordParam.trim();
        }

        // 获取当前用户ID
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        PageResult<CircleListItemVO> pageResult = circleService.getCircles(page, size, keyword, currentUserId);

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
     * 获取圈子详情
     */
    private void handleGetCircleDetail(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long circleId = PathUtil.getIdFromPath(request);
        if (circleId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        Long currentUserId = JwtUtil.getCurrentUserId(request);

        CircleVO circle = circleService.getCircleDetail(circleId, currentUserId);
        if (circle == null) {
            Result<Object> result = Result.error("圈子不存在");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<CircleVO> result = Result.success("成功", circle);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 加入圈子
     */
    private void handleJoinCircle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long circleId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (circleId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        CircleJoinResponse joinResponse = circleService.joinCircle(circleId, currentUserId);
        if (joinResponse == null) {
            Result<Object> result = Result.error("加入圈子失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<CircleJoinResponse> result = Result.success("成功加入圈子", joinResponse);
        WriterUtil.writeJson(response, result);
    }

    /**
     * 退出圈子
     */
    private void handleExitCircle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long circleId = PathUtil.getIdFromPath(request);
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        if (circleId == null || currentUserId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        boolean success = circleService.exitCircle(circleId, currentUserId);
        if (!success) {
            Result<Object> result = Result.error("退出圈子失败");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<Object> result = Result.success("退出圈子成功");
        WriterUtil.writeJson(response, result);
    }

    /**
     * 获取圈子成员列表
     */
    private void handleGetCircleMembers(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long circleId = PathUtil.getIdFromPath(request);
        if (circleId == null) {
            WriterUtil.paramsError(response);
            return;
        }

        // 解析分页参数
        int page = 1;
        int size = 20;

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");

        if (pageStr != null) page = Integer.parseInt(pageStr);
        if (sizeStr != null) size = Integer.parseInt(sizeStr);

        Long currentUserId = JwtUtil.getCurrentUserId(request);

        PageResult<CircleMemberVO> pageResult = circleService.getCircleMembers(circleId, page, size, currentUserId);

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
