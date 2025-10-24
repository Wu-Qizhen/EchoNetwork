package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.*;
import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.UserService;
import com.wqz.echonetwork.service.impl.UserServiceImpl;
import com.wqz.echonetwork.utils.JsonUtil;
import com.wqz.echonetwork.utils.JwtUtil;
import com.wqz.echonetwork.utils.LogUtil;
import com.wqz.echonetwork.utils.WriterUtil;
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
        "/api/users/login",
        "/api/users/register",
        "/api/users/logout",
        "/api/users/reset-verify",
        "/api/users/reset-password",
        "/api/users/*"
})
public class UserController extends HttpServlet {

    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        switch (path) {
            case "/api/users/login":
                handleLogin(request, response);
                break;
            case "/api/users/register":
                handleRegister(request, response);
                break;
            case "/api/users/reset-verify":
                handleResetVerify(request, response);
                break;
            case "/api/users/reset-password":
                handleResetPassword(request, response);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        if (path.equals("/api/users/logout")) {
            handleLogout(request, response);
        } else {
            processUserResourceRequest(request, response, "GET");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        processUserResourceRequest(request, response, "PUT");
    }

    private void processUserResourceRequest(HttpServletRequest request, HttpServletResponse response, String method) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.matches("^/\\d+$")) {
            Long userId = Long.parseLong(pathInfo.substring(1));
            // LogUtil.info("用户 ID：" + userId);

            Long currentUserId = JwtUtil.getCurrentUserId(request);
            if (currentUserId == null) {
                Result<Object> result = Result.error("未提供有效的授权信息");
                WriterUtil.writeJson(response, result);
                return;
            }

            // LogUtil.info("当前用户 ID：" + currentUserId);

            switch (method) {
                case "GET":
                    handleGetUserProfile(userId, currentUserId, response);
                    break;
                case "PUT":
                    handleUpdateUser(userId, currentUserId, request, response);
                    break;
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "资源未找到");
        }
    }

    private void handleGetUserProfile(Long userId, Long currentUserId, HttpServletResponse response) throws IOException {
        try {
            UserProfileResponse profile = userService.getProfile(userId, currentUserId);

            Result<Object> result;
            if (profile != null) {
                result = Result.success("获取成功", profile);
            } else {
                result = Result.error("用户不存在");
            }
            WriterUtil.writeJson(response, result);
        } catch (Exception e) {
            LogUtil.error("获取失败：" + e.getMessage());
            Result<Object> result = Result.error("获取失败");
            WriterUtil.writeJson(response, result);
        }
    }

    private void handleUpdateUser(Long userId, Long currentUserId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (!userId.equals(currentUserId)) {
                Result<Object> result = Result.error("无权限更新用户信息");
                WriterUtil.writeJson(response, result);
                return;
            }

            UserProfileRequest userProfileRequest = JsonUtil.parseJson(request, UserProfileRequest.class);
            String message = userService.updateProfile(userProfileRequest, userId);

            if (message == null) {
                UserProfileResponse profile = userService.getProfile(userId, userId);
                Result<Object> result = Result.success("更新成功", profile);

                WriterUtil.writeJson(response, result);
            } else {
                Result<Object> result = Result.error(message);
                WriterUtil.writeJson(response, result);
            }
        } catch (Exception e) {
            LogUtil.error("更新失败：" + e.getMessage());
            Result<Object> result = Result.error("更新失败");
            WriterUtil.writeJson(response, result);
        }
    }

    private void handleResetPassword(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResetPasswordRequest resetPasswordRequest = JsonUtil.parseJson(request, ResetPasswordRequest.class);
        String message = userService.resetPassword(resetPasswordRequest);
        Result<Object> result = message == null ? Result.success() : Result.error(message);
        WriterUtil.writeJson(response, result);
    }

    private void handleResetVerify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ResetVerifyRequest resetVerifyRequest = JsonUtil.parseJson(request, ResetVerifyRequest.class);
        String message = userService.resetVerify(resetVerifyRequest);
        Result<Object> result = message == null ? Result.success() : Result.error(message);
        WriterUtil.writeJson(response, result);
    }

    private void handleLogout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authHeader = request.getHeader("Authorization");
        // LogUtil.info(“Header: ” + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // LogUtil.error("未提供有效的 Token");
            Result<Object> result = Result.success("登出成功");
            WriterUtil.writeJson(response, result);
            return;
        }

        String token = authHeader.substring(7);
        try {
            if (JwtUtil.validateToken(token)) {
                JwtUtil.addToBlacklist(token);

                // String username = JwtUtil.getUsernameFromToken(token);
                // LogUtil.log("用户 " + username + " 已登出，Token 已加入黑名单");
            }

            // LogUtil.log("登出成功");
            Result<Object> result = Result.success("登出成功");
            WriterUtil.writeJson(response, result);
        } catch (Exception e) {
            // LogUtil.error("登出失败：" + e.getMessage());
            Result<Object> result = Result.success("登出成功");
            WriterUtil.writeJson(response, result);
        }
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserLoginRequest loginRequest = JsonUtil.parseJson(request, UserLoginRequest.class);

        if (!loginRequest.isParamsValid()) {
            WriterUtil.paramsError(response);
            return;
        }

        UserLoginResponse loginResponse = userService.login(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        if (loginResponse == null) {
            Result<Object> result = Result.error("用户名或密码错误");
            WriterUtil.writeJson(response, result);
            return;
        }

        Result<UserLoginResponse> result = Result.success("登录成功", loginResponse);
        WriterUtil.writeJson(response, result);
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRegisterRequest userRegisterRequest = JsonUtil.parseJson(request, UserRegisterRequest.class);
        // LogUtil.info("收到请求：" + userRegisterRequest);

        if (!userRegisterRequest.isParamsValid()) {
            WriterUtil.paramsError(response);
            return;
        }

        String message = userService.register(userRegisterRequest);

        Result<Object> result = message == null ? Result.success("注册成功") : Result.error(message);

        WriterUtil.writeJson(response, result);
    }
}
