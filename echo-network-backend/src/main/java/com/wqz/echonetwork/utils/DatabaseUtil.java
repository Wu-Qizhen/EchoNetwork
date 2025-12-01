package com.wqz.echonetwork.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wqz.echonetwork.utils.expr.TransactionalOperation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;

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
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
        YamlLoader yamlLoader = new YamlLoader("application.yml");
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
            // LogUtil.log("事务提交成功！");
        } catch (SQLException e) {
            rollbackTransaction();
            // LogUtil.error("事务回滚：" + e.getMessage());
            throw e;
        }
    }

    public Long insert(String sql, Object... params) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            setParameters(ps, params);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("插入失败，没有行被影响");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                } else {
                    throw new SQLException("插入失败，无法获取自增 ID");
                }
            }
        }
    }

    public List<Long> insertBatch(String sql, List<Object[]> paramList) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            List<Long> generatedIds = new ArrayList<>();

            for (Object[] params : paramList) {
                setParameters(ps, params);
                ps.addBatch();
            }

            int[] affectedRows = ps.executeBatch();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                while (generatedKeys.next()) {
                    generatedIds.add(generatedKeys.getLong(1));
                }
            }

            return generatedIds;
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

    public <T> List<T> queryList(String sql, Class<T> clazz, Object... params) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                return mapResultSetToList(rs, clazz);
            }
        }
    }

    public <T> T queryObject(String sql, Class<T> clazz, Object... params) throws SQLException {
        List<T> list = queryList(sql, clazz, params);
        return list.isEmpty() ? null : list.get(0);
    }

    /* public <T> T queryScalar(String sql, Class<T> clazz, Object... params) throws SQLException {
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
    } */

    public <T> T queryScalar(String sql, Class<T> clazz, Object... params) throws SQLException {
        Connection conn = getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, params);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // 直接获取结果，不进行对象映射
                    Object value = rs.getObject(1);

                    // 处理基本类型和包装类的转换
                    if (value == null) {
                        return null;
                    }

                    // 如果类型匹配，直接返回
                    if (clazz.isInstance(value)) {
                        return clazz.cast(value);
                    }

                    // 处理数字类型的转换
                    if (value instanceof Number) {
                        Number number = (Number) value;
                        if (clazz == Integer.class || clazz == int.class) {
                            return clazz.cast(number.intValue());
                        } else if (clazz == Long.class || clazz == long.class) {
                            return clazz.cast(number.longValue());
                        } else if (clazz == Double.class || clazz == double.class) {
                            return clazz.cast(number.doubleValue());
                        } else if (clazz == Float.class || clazz == float.class) {
                            return clazz.cast(number.floatValue());
                        } else if (clazz == Short.class || clazz == short.class) {
                            return clazz.cast(number.shortValue());
                        } else if (clazz == Byte.class || clazz == byte.class) {
                            return clazz.cast(number.byteValue());
                        }
                    }

                    // 其他类型尝试转换
                    return clazz.cast(value);
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

    /* private <T> void setFieldValue(T obj, String fieldName, Object value) {
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
    } */

    private <T> void setFieldValue(T obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            // 如果字段值是字符串类型，且目标字段是 Collection 类型，尝试 JSON 反序列化
            if (value instanceof String && Collection.class.isAssignableFrom(field.getType())) {
                try {
                    value = deserializeJsonToCollection((String) value, field);
                } catch (JsonProcessingException e) {
                    LogUtil.warn("JSON反序列化失败，字段：" + fieldName + "，值：" + value + "，错误：" + e.getMessage());
                    // 如果反序列化失败，保持原值
                }
            }

            field.set(obj, value);
        } catch (NoSuchFieldException e1) {
            // 忽略大小写和下划线
            try {
                Field[] fields = obj.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (field.getName().equalsIgnoreCase(fieldName.replace("_", ""))) {
                        field.setAccessible(true);

                        // 如果字段值是字符串类型，且目标字段是 Collection 类型，尝试 JSON 反序列化
                        if (value instanceof String && Collection.class.isAssignableFrom(field.getType())) {
                            try {
                                value = deserializeJsonToCollection((String) value, field);
                            } catch (JsonProcessingException e) {
                                LogUtil.warn("JSON反序列化失败，字段：" + fieldName + "，值：" + value + "，错误：" + e.getMessage());
                                // 如果反序列化失败，保持原值
                            }
                        }

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

    private Object deserializeJsonToCollection(String json, Field field) throws JsonProcessingException {
        if (json == null || json.trim().isEmpty()) {
            return createEmptyCollection(field.getType());
        }

        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericType;
            Type[] typeArguments = paramType.getActualTypeArguments();

            if (typeArguments.length > 0) {
                Class<?> elementType = (Class<?>) typeArguments[0];

                if (Set.class.isAssignableFrom(field.getType())) {
                    return objectMapper.readValue(json,
                            objectMapper.getTypeFactory().constructCollectionType(Set.class, elementType));
                } else if (List.class.isAssignableFrom(field.getType())) {
                    return objectMapper.readValue(json,
                            objectMapper.getTypeFactory().constructCollectionType(List.class, elementType));
                }
            }
        }

        // 默认情况：没有泛型信息时，反序列化为 List<Object>
        return objectMapper.readValue(json, List.class);
    }

    private Collection<?> createEmptyCollection(Class<?> collectionType) {
        if (Set.class.isAssignableFrom(collectionType)) {
            return new HashSet<>();
        } else if (List.class.isAssignableFrom(collectionType)) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>();
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
