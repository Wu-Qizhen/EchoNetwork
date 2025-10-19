package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.service.impl.UserServiceImpl;
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
@WebServlet({"/api/users/login", "/api/users/register"})
public class UserController extends HttpServlet {

    private final UserServiceImpl userService = new UserServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        /* if ("/api/users/profile".equals(path)) {
            handleGetProfile(request, response);
        } */
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }

    /* private void handleGetProfile(HttpServletRequest request, HttpServletResponse response) {
    } */
}
