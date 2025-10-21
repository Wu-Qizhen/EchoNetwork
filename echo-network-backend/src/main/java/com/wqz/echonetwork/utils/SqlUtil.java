package com.wqz.echonetwork.utils;

import com.wqz.echonetwork.utils.expr.ExceptionOperation;

import java.sql.SQLException;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class SqlUtil {

    private static void exceptionHandler(SQLException e) {
        LogUtil.error("连接失败：" + e.getMessage());
    }

    public static <T> T queryObject(String sql, Class<T> clazz, Object... params) {
        DatabaseUtil databaseUtil = new DatabaseUtil();
        T t = null;
        try {
            t = databaseUtil.queryObject(sql, clazz, params);
        } catch (SQLException e) {
            exceptionHandler(e);
        } finally {
            databaseUtil.closeConnection();
        }
        return t;
    }

    public static int update(String sql, Object... params) {
        DatabaseUtil databaseUtil = new DatabaseUtil();
        int result = 0;
        try {
            result = databaseUtil.update(sql, params);
        } catch (SQLException e) {
            exceptionHandler(e);
        } finally {
            databaseUtil.closeConnection();
        }
        return result;
    }

    public static <T> T queryObject(ExceptionOperation operation, String sql, Class<T> clazz, Object... params) {
        DatabaseUtil databaseUtil = new DatabaseUtil();
        T t = null;
        try {
            t = databaseUtil.queryObject(sql, clazz, params);
        } catch (SQLException e) {
            operation.handle(e);
        } finally {
            databaseUtil.closeConnection();
        }
        return t;
    }

    public static int update(ExceptionOperation operation, String sql, Object... params) {
        DatabaseUtil databaseUtil = new DatabaseUtil();
        int result = 0;
        try {
            result = databaseUtil.update(sql, params);
        } catch (SQLException e) {
            operation.handle(e);
        } finally {
            databaseUtil.closeConnection();
        }
        return result;
    }
}
