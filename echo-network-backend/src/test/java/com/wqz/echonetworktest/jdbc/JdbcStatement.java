package com.wqz.echonetworktest.jdbc;

import com.wqz.echonetworktest.utils.YamlLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class JdbcStatement {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        String driverClass = yamlLoader.getString("app.database.driver");
        String url = yamlLoader.getString("app.database.url");
        String username = yamlLoader.getString("app.database.username");
        String password = yamlLoader.getString("app.database.password");

        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql1 = "update account set money = 3000 where id = 3";
        String sql2 = "update account set money = 3000 where id = 2";

        try {
            connection.setAutoCommit(false);
            int i = statement.executeUpdate(sql1);
            int j = statement.executeUpdate(sql2);
            if (i > 0 && j > 0) {
                System.out.println("修改成功");
            } else {
                System.out.println("修改失败");
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            System.out.println("修改失败");
            throw new RuntimeException(e);
        }

        statement.close();
        connection.close();
    }
}
