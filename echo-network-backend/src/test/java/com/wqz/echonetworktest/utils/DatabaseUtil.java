package com.wqz.echonetworktest.utils;

import com.wqz.echonetwork.utils.LogUtil;
import com.wqz.echonetwork.utils.expr.TransactionalOperation;
import com.wqz.echonetwork.utils.YamlLoader;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class DatabaseUtil {

    private String driver;
    private String url;
    private String username;
    private String password;
    private Connection connection;
    private boolean enableTransaction = false;

    public DatabaseUtil() {
        loadDefaultConfig();
        loadDriver();
    }

    public DatabaseUtil(String driver, String url, String username, String password) {
        this.driver = driver;
        this.url = url;
        this.username = username;
        this.password = password;
        loadDriver();
    }

    private void loadDefaultConfig() {
        com.wqz.echonetwork.utils.YamlLoader yamlLoader = new YamlLoader("application.yml");
        driver = yamlLoader.getString("app.database.driver");
        url = yamlLoader.getString("app.database.url");
        username = yamlLoader.getString("app.database.username");
        password = yamlLoader.getString("app.database.password");
    }

    private void loadDriver() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("数据库驱动加载失败：" + driver, e);
        }
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
        }
        return connection;
    }

    public void beginTransaction() throws SQLException {
        if (enableTransaction) {
            throw new SQLException("事务已开启");
        }
        Connection conn = getConnection();
        conn.setAutoCommit(false);
        enableTransaction = true;
    }

    public void commitTransaction() throws SQLException {
        if (!enableTransaction) {
            throw new SQLException("事务未开启");
        }
        connection.commit();
        connection.setAutoCommit(true);
        enableTransaction = false;
    }

    public void rollbackTransaction() {
        if (connection != null && enableTransaction) {
            try {
                connection.rollback();
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                // 忽略
            } finally {
                enableTransaction = false;
            }
        }
    }

    public void doTransaction(TransactionalOperation operation) throws SQLException {
        try {
            beginTransaction();
            operation.execute();
            commitTransaction();
            // System.out.println("事务提交成功！");
        } catch (SQLException e) {
            rollbackTransaction();
            // System.out.println("事务回滚：" + e.getMessage());
            throw e;
        }
    }

    public int update(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            return ps.executeUpdate();
        }
    }

    public int[] updateBatch(String sql, List<Object[]> paramList) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Object[] params : paramList) {
                setParameters(ps, params);
                ps.addBatch();
            }
            return ps.executeBatch();
        }
    }

    public <T> List<T> query(String sql, Class<T> clazz, Object... params) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return mapResultSetToList(rs, clazz);
            }
        }
    }

    public <T> T queryObject(String sql, Class<T> clazz, Object... params) throws SQLException {
        List<T> list = query(sql, clazz, params);
        return list.isEmpty() ? null : list.get(0);
    }

    public <T> T queryScalar(String sql, Class<T> clazz, Object... params) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject(1, clazz);
                }
                return null;
            }
        }
    }

    private void setParameters(PreparedStatement ps, Object... params) throws SQLException {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
    }

    private <T> List<T> mapResultSetToList(ResultSet rs, Class<T> clazz) throws SQLException {
        List<T> resultList = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        try {
            while (rs.next()) {
                T obj = clazz.getDeclaredConstructor().newInstance();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    Object value = rs.getObject(i);
                    setFieldValue(obj, columnName, value);
                }

                resultList.add(obj);
            }
        } catch (Exception e) {
            LogUtil.error("对象映射失败：" + e.getMessage());
            throw new RuntimeException("对象映射失败：", e);
        }

        return resultList;
    }

    private <T> void setFieldValue(T obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (NoSuchFieldException e1) {
            // 忽略大小写和下划线
            try {
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase(fieldName.replace("_", ""))) {
                        field.setAccessible(true);
                        field.set(obj, value);
                        break;
                    }
                }
            } catch (IllegalAccessException e2) {
                // 忽略
            }
        } catch (IllegalAccessException e) {
            // 忽略
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                if (enableTransaction) {
                    rollbackTransaction();
                }
                connection.close();
            } catch (SQLException e) {
                // 忽略
            }
        }
    }
}
