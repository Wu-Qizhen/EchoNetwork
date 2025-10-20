package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.dto.UserLoginRequest;
import com.wqz.echonetwork.entity.dto.UserLoginResponse;
import com.wqz.echonetwork.entity.dto.UserRegisterRequest;
import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.impl.UserServiceImpl;
import com.wqz.echonetwork.utils.JsonUtil;
import com.wqz.echonetwork.utils.WriterUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
@WebServlet({"/api/users/login", "/api/users/register"})
public class UserController extends HttpServlet {

    private final UserServiceImpl userService = new UserServiceImpl();

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
            /* default:
                response.sendError(404); */
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getServletPath();

        /* if ("/api/users/profile".equals(path)) {
            handleGetProfile(request, response);
        } */
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
        // System.out.println(userRegisterRequest);

        if (!userRegisterRequest.isParamsValid()) {
            WriterUtil.paramsError(response);
            return;
        }

        String message = userService.register(userRegisterRequest);

        Result<Object> result = message == null ? Result.success("注册成功") : Result.error(message);

        PrintWriter out = response.getWriter();
        out.write(result.asJsonString());
        out.flush();
    }

    /* private void handleGetProfile(HttpServletRequest request, HttpServletResponse response) {
    } */
}
