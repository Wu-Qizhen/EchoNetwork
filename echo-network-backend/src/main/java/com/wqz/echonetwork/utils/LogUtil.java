package com.wqz.echonetwork.utils;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.21
 */
public class LogUtil {
    public static void log(String message) {
        System.out.println("[LOG] " + message);
    }

    public static void success(String message) {
        System.out.println("[SUCCESS] " + message);
    }

    public static void error(String message) {
        System.out.println("[ERROR] " + message);
    }

    public static void warn(String message) {
        System.out.println("[WARN] " + message);
    }

    public static void info(String message) {
        System.out.println("[INFO] " + message);
    }
}
