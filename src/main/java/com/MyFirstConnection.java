package com;

import java.sql.*;

public class MyFirstConnection {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mentoring_8";
    static final String USER = "root";
    static final String PASS = "1111";

    public static void main(String[] args) {
        MyFirstConnection mfc = new MyFirstConnection();

        try(Connection connection = mfc.getConnection()) {

        } catch (SQLException e) {
            e.printStackTrace();
        }


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
        try(Statement statement = connection.createStatement()){
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
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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

    //not done
    public void printAllTables(Connection connection) {
        try (Statement statement = connection.createStatement()){
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet resultSet = databaseMetaData.getTables("mentoring_8", null, "%", types);
            while (resultSet.next()) {
                System.out.println(resultSet.getString("TABLE_NAME"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void executeViaCallableStatement(Connection connection) {
        String SQL = "{call getName(?, ?)}";
        try (CallableStatement callableStatement = connection.prepareCall(SQL)){
            callableStatement.setInt(2, 1);

            callableStatement.executeQuery();
            String name = callableStatement.getString("USR_NAME");
            System.out.println(name);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createStoredProcedure(Connection connection) {
        try (Statement statement = connection.createStatement()){

            statement.execute("CREATE PROCEDURE `getName`(" +
                    "OUT USR_NAME VARCHAR(128)," +
                    "IN USR_ID INT ) " +
                    "BEGIN "+
                    "SELECT name INTO USR_NAME FROM User WHERE ID = USR_ID;"+
                    "END");

        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }



}



