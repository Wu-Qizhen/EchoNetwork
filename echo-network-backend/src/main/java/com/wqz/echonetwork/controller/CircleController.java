package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.CircleCreateRequest;
import com.wqz.echonetwork.entity.dto.CircleJoinResponse;
import com.wqz.echonetwork.entity.dto.CircleQueryRequest;
import com.wqz.echonetwork.entity.vo.*;
import com.wqz.echonetwork.service.CircleService;
import com.wqz.echonetwork.service.impl.CircleServiceImpl;
import com.wqz.echonetwork.utils.*;
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
        try {
            // 解析查询参数
            CircleQueryRequest queryRequest = parseQueryParameters(request);

            // 获取当前用户 ID
            Long currentUserId = JwtUtil.getCurrentUserId(request);

            // 多条件查询
            PageResult<CircleListItemVO> pageResult = circleService.getCirclesByConditions(queryRequest, currentUserId);

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
            LogUtil.error("获取圈子列表失败：" + e.getMessage());
            Result<Object> result = Result.error("获取圈子列表失败");
            WriterUtil.writeJson(response, result);
        }
    }

    private CircleQueryRequest parseQueryParameters(HttpServletRequest request) {
        CircleQueryRequest queryRequest = new CircleQueryRequest();

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        String keyword = request.getParameter("keyword");
        String creatorIdStr = request.getParameter("creatorId");
        String memberIdStr = request.getParameter("memberId");
        String sortBy = request.getParameter("sortBy");
        String sortOrder = request.getParameter("sortOrder");

        if (pageStr != null) queryRequest.setPage(Integer.parseInt(pageStr));
        if (sizeStr != null) queryRequest.setSize(Integer.parseInt(sizeStr));
        if (keyword != null && !keyword.trim().isEmpty()) {
            queryRequest.setKeyword("%" + keyword.trim() + "%");
        }
        if (creatorIdStr != null) queryRequest.setCreatorId(Long.parseLong(creatorIdStr));
        if (memberIdStr != null) queryRequest.setMemberId(Long.parseLong(memberIdStr));
        if (sortBy != null) queryRequest.setSortBy(sortBy);
        if (sortOrder != null) queryRequest.setSortOrder(sortOrder);

        return queryRequest;
    }

    /* private void handleGetCircles(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 解析查询参数
        int page = 1;
        int size = 10;
        String keyword = null;
        Long userId = null;

        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        String keywordParam = request.getParameter("keyword");
        String userIdStr = request.getParameter("userId");

        if (pageStr != null) page = Integer.parseInt(pageStr);
        if (sizeStr != null) size = Integer.parseInt(sizeStr);
        if (keywordParam != null && !keywordParam.trim().isEmpty()) {
            keyword = keywordParam.trim();
        }

        if (userIdStr != null && !userIdStr.trim().isEmpty()) {
            try {
                userId = Long.parseLong(userIdStr.trim());
            } catch (NumberFormatException e) {
                WriterUtil.paramsError(response);
                return;
            }
        }

        // 获取当前用户 ID
        Long currentUserId = JwtUtil.getCurrentUserId(request);

        PageResult<CircleListItemVO> pageResult;

        // 如果提供了 userId 参数，获取该用户加入的圈子列表
        if (userId != null) {
            List<CircleListItemVO> userCircles = circleService.getUserCircles(userId);
            // 模拟分页处理
            int total = userCircles.size();
            int fromIndex = (page - 1) * size;
            int toIndex = Math.min(fromIndex + size, total);
            List<CircleListItemVO> pagedCircles = fromIndex < total ? userCircles.subList(fromIndex, toIndex) : new ArrayList<>();

            pageResult = new PageResult<>(pagedCircles, total, page, size);
        } else {
            // 否则获取所有圈子列表
            pageResult = circleService.getCircles(page, size, keyword, currentUserId);
        }

        // 构建响应
        Map<String, Object> data = new HashMap<>();
        data.put("list", pageResult.getList());
        data.put("total", pageResult.getTotal());
        data.put("page", pageResult.getPage());
        data.put("size", pageResult.getSize());
        data.put("totalPages", pageResult.getTotalPages());

        Result<Map<String, Object>> result = Result.success("成功", data);
        WriterUtil.writeJson(response, result);
    } */

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
        int size = 10;

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
