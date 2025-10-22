package com.wqz.echonetwork.utils;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class JsonUtil {

    public static <T> T parseJson(HttpServletRequest request, Class<T> clazz) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }
        } catch (IOException e) {
            throw new IOException(e);
        }

        return JSON.parseObject(jsonBuilder.toString(), clazz);
    }

    public static String toJsonString(Object obj) {
        return JSON.toJSONString(obj);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return JSON.parseObject(jsonStr, clazz);
    }
}
