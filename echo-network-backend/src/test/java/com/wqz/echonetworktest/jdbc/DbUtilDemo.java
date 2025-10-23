package com.wqz.echonetworktest.jdbc;

import com.wqz.echonetworktest.pojo.User;
import com.wqz.echonetworktest.utils.DatabaseUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.18
 */
public class DbUtilDemo {

    public static void main(String[] args) {
        DatabaseUtil databaseUtil = new DatabaseUtil();

        try {
            // testBasicOperations(databaseUtil);
            // testTransactionOperations(databaseUtil);
            testTransactionOperationsPlus(databaseUtil);
        }/* catch (SQLException e) {
            System.out.println("错误：" + e.getMessage());
        } */ finally {
            databaseUtil.closeConnection();
        }
    }

    public static void testBasicOperations(DatabaseUtil databaseUtil) throws SQLException {
        System.out.println("基本操作测试：");

        // 插入
        String insertSql = "INSERT INTO users (name, email) VALUES (?, ?)";
        int insertCount = databaseUtil.update(insertSql, "张三", "zhangsan@example.com");
        System.out.println("插入记录数：" + insertCount);

        // 查询单个
        String queryOneSql = "SELECT * FROM users WHERE name = ?";
        User user = databaseUtil.queryObject(queryOneSql, User.class, "张三");
        System.out.println("查询单个用户：" + user);

        // 查询列表
        String queryAllSql = "SELECT * FROM users";
        List<User> users = databaseUtil.query(queryAllSql, User.class);
        System.out.println("查询所有用户：");
        users.forEach(System.out::println);

        // 更新
        String updateSql = "UPDATE users SET email = ? WHERE name = ?";
        int updateCount = databaseUtil.update(updateSql, "new_email@example.com", "张三");
        System.out.println("更新记录数：" + updateCount);

        // 统计
        String countSql = "SELECT COUNT(*) FROM users";
        Long count = databaseUtil.queryScalar(countSql, Long.class);
        System.out.println("用户总数：" + count);
    }

    public static void testTransactionOperations(DatabaseUtil databaseUtil) {
        System.out.println("\n事务操作测试：");

        try {
            databaseUtil.beginTransaction();

            databaseUtil.update("INSERT INTO users (name, email) VALUES (?, ?)",
                    "李四", "lisi@example.com");
            databaseUtil.update("INSERT INTO users (name, email) VALUES (?, ?)",
                    "王五", "wangwu@example.com");

            // 故意制造错误
            // databaseUtil.update("INSERT INTO invalid_table (name) VALUES (?)", "测试");

            databaseUtil.commitTransaction();
            System.out.println("事务提交成功！");
        } catch (SQLException e) {
            databaseUtil.rollbackTransaction();
            System.out.println("事务回滚：" + e.getMessage());
        }
    }

    public static void testTransactionOperationsPlus(DatabaseUtil databaseUtil) {
        System.out.println("\n事务操作测试：");

        try {
            databaseUtil.doTransaction(() -> {
                databaseUtil.update("INSERT INTO users (name, email) VALUES (?, ?)",
                        "李四", "lisi@example.com");
                databaseUtil.update("INSERT INTO users (name, email) VALUES (?, ?)",
                        "王五", "wangwu@example.com");
                // 故意制造错误
                // databaseUtil.update("INSERT INTO invalid_table (name) VALUES (?)", "测试");
            });
            System.out.println("事务提交成功！");
        } catch (SQLException e) {
            databaseUtil.rollbackTransaction();
            System.out.println("事务回滚：" + e.getMessage());
        }
    }

    public static void testBatchOperations(DatabaseUtil databaseUtil) throws SQLException {
        System.out.println("\n批量操作测试：");

        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        List<Object[]> paramList = new ArrayList<>();

        paramList.add(new Object[]{"赵六", "zhaoliu@example.com"});
        paramList.add(new Object[]{"孙七", "sunqi@example.com"});
        paramList.add(new Object[]{"周八", "zhouba@example.com"});

        int[] results = databaseUtil.updateBatch(sql, paramList);
        System.out.println("批量插入完成，影响行数：" + results.length);
    }
}
