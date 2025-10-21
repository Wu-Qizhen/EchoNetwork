package com.wqz.echonetwork.filter;

import com.wqz.echonetwork.utils.LogUtil;
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
public class CorsFilter implements Filter {

    // 允许的域名列表
    private final List<String> allowedOrigins = Arrays.asList(
            "http://localhost:8081",
            "http://127.0.0.1:8081",
            "http://localhost:8080",
            "http://127.0.0.1:8080"
    );

    // 允许的 HTTP 方法
    private final List<String> allowedMethods = Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
    );

    // 允许的请求头
    private final List<String> allowedHeaders = Arrays.asList(
            "Content-Type",
            "Authorization",
            "X-Requested-With",
            "Accept",
            "Origin",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers",
            "X-CSRF-TOKEN"
    );

    // 暴露的响应头
    private final List<String> exposedHeaders = Arrays.asList(
            "Authorization",
            "Content-Disposition"
    );

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // LogUtil.log("CORS Filter 初始化完成");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 获取请求的 Origin 头
        String origin = httpRequest.getHeader("Origin");

        // 检查 Origin 是否在允许的列表中
        if (origin != null && allowedOrigins.contains(origin)) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        } else if (origin != null && !allowedOrigins.contains(origin)) {
            // 如果是未知源，可以选择拒绝或允许（根据安全要求）
            LogUtil.error("未知源请求：" + origin);
            // httpResponse.setHeader("Access-Control-Allow-Origin", "null");
        }

        // 设置 CORS 其他头信息
        httpResponse.setHeader("Access-Control-Allow-Methods", String.join(", ", allowedMethods));
        httpResponse.setHeader("Access-Control-Allow-Headers", String.join(", ", allowedHeaders));
        httpResponse.setHeader("Access-Control-Expose-Headers", String.join(", ", exposedHeaders));
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Max-Age", "3600"); // 1 小时缓存

        // 添加调试信息头
        httpResponse.setHeader("X-CORS-Status", "Enabled");
        httpResponse.setHeader("X-Requested-Path", httpRequest.getRequestURI());

        // 记录 CORS 请求信息
        // LogUtil.info("CORS 请求：" + httpRequest.getMethod() + " " + httpRequest.getRequestURI());
        // LogUtil.info("来自源：" + origin);
        // LogUtil.info("User-Agent：" + httpRequest.getHeader("User-Agent"));

        // 处理 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            // LogUtil.log("OPTIONS 预检请求处理完成");
            return;
        }

        // 继续处理其他请求
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // LogUtil.log("CORS Filter 销毁");
    }
}

/* public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 允许的域名，生产环境应该配置为具体的域名
        String origin = httpRequest.getHeader("Origin");
        if (origin != null) {
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        }

        // 允许的 HTTP 方法
        httpResponse.setHeader("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS");

        // 允许的请求头
        httpResponse.setHeader("Access-Control-Allow-Headers",
                "Origin, X-Requested-With, Content-Type, Accept, Authorization");

        // 允许携带凭证（如 Cookies）
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        // 预检请求缓存时间
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        // 处理 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
} */
