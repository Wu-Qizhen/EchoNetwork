package com.wqz.echonetwork.utils;

import com.wqz.echonetwork.entity.vo.Result;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.20
 */
public class WriterUtil {

    public static void writeJson(HttpServletResponse response, Result<?> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.write(result.asJsonString());
        out.flush();
    }

    public static void paramsError(HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(Result.paramsError().asJsonString());
        writer.flush();
    }

    public static void unauthorized(HttpServletResponse response) throws IOException {
        Result<Object> result = Result.unauthorized();
        writeJson(response, result);
    }

    public static void forbidden(HttpServletResponse response) throws IOException {
        Result<Object> result = Result.forbidden();
        writeJson(response, result);
    }
}
