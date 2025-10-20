package com.wqz.echonetwork.filter;

import com.wqz.echonetwork.utils.JwtUtil;
import com.wqz.echonetwork.utils.WriterUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.20
 */
@WebFilter("/*")
public class JwtAuthenticationFilter implements Filter {

    private static final List<String> EXCLUDED_PATHS = Arrays.asList(
            "/api/users/login",
            "/api/users/register",
            "/api/auth/ask-code"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(httpRequest);

        if (token == null || !JwtUtil.validateToken(token)) {
            WriterUtil.unauthorized(httpResponse);
            return;
        }

        String username = JwtUtil.getUsernameFromToken(token);
        Long userId = JwtUtil.getUserIdFromToken(token);
        Integer role = JwtUtil.getRoleFromToken(token);

        httpRequest.setAttribute("username", username);
        httpRequest.setAttribute("userId", userId);
        httpRequest.setAttribute("role", role);

        chain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
