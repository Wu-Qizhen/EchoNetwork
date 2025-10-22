package com.wqz.echonetwork.controller;

import com.wqz.echonetwork.entity.vo.Result;
import com.wqz.echonetwork.service.AuthService;
import com.wqz.echonetwork.service.impl.AuthServiceImpl;
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
 * Created by Wu Qizhen on 2025.10.19
 */
@WebServlet({"/api/auth/ask-code"})
public class AuthController extends HttpServlet {

    private final AuthService authService = new AuthServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // String path = request.getServletPath();
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String type = request.getParameter("type");
        String email = request.getParameter("email");

        if (type == null || email == null) {
            WriterUtil.paramsError(response);
            return;
        }

        String message = authService.askEmailVerifyCode(type, email);

        Result<Object> result = message == null ? Result.success("验证码已发送，请查收") : Result.error(message);

        WriterUtil.writeJson(response, result);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }
}
