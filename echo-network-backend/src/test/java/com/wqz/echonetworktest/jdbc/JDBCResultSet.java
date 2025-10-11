package com.wqz.echonetworktest.jdbc;

import com.wqz.echonetworktest.utils.YamlLoader;

import java.sql.*;

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2025.10.11
 */
public class JDBCResultSet {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        YamlLoader yamlLoader = new YamlLoader("application.yml");
        String driverClass = yamlLoader.getString("app.database.driver");
        String url = yamlLoader.getString("app.database.url");
        String username = yamlLoader.getString("app.database.username");
        String password = yamlLoader.getString("app.database.password");

        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(url, username, password);
        Statement statement = connection.createStatement();
        String sql = "select * from account";

        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            int money = resultSet.getInt(3);
            System.out.println(id + " " + name + " " + money);
        }

        resultSet.close();
        statement.close();
        connection.close();
    }
}
