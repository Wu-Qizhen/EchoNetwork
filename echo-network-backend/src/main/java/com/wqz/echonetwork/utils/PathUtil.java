package com.wqz.echonetwork.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.23
 */
public class PathUtil {

    public static Long getIdFromPath(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && pathInfo.matches("^/\\d+$")) {
            return Long.parseLong(pathInfo.substring(1));
        }
        return null;
    }
}
