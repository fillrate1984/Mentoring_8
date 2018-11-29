package com;

import java.sql.*;

public class MyFirstConnection {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mentoring_8";
    static final String USER = "root";
    static final String PASS = "1111";

    public static void main(String[] args) {
        MyFirstConnection myFirstConnection = new MyFirstConnection();
        Connection connection = myFirstConnection.getConnection();

        myFirstConnection.executeViaPreparedStatement(connection);

    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void executeViaStatement(Connection connection) {
        try {
            Statement statement = connection.createStatement();
            String sql = "SELECT id, name FROM User";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.printf("User: %d %s\n", id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeViaPreparedStatement(Connection connection) {
        String sql = "SELECT id, name FROM User WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            // parameneteIndex: порядковый номер знака ? в запросе
            preparedStatement.setInt(1, 1);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                System.out.printf("'Prepared statement' User: %d %s\n", id, name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
