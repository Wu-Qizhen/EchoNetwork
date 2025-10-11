package com.wqz.echonetworktest.jdbc;

import com.wqz.echonetworktest.utils.YamlLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.10
 */
public class JDBCDemo {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        String driverClass = yamlLoader.getString("app.database.driver");
        String url = yamlLoader.getString("app.database.url");
        String username = yamlLoader.getString("app.database.username");
        String password = yamlLoader.getString("app.database.password");

        Class.forName(driverClass);

        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();

        String sql = "update account set money = 2000 where id = 1";

        int count = statement.executeUpdate(sql);

        System.out.println(count);

        statement.close();
        connection.close();
    }
}
