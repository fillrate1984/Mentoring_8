package com;

import org.junit.*;

import java.sql.*;

import static org.junit.Assert.*;

public class MyFirstConnectionTest {

    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/mentoring_8";
    static final String USER = "root";
    static final String PASS = "1111";
    private Connection connection;

    @Before
    public void openConnection() {
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connection Created");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void connectionTest() {
        assertTrue(connection != null);
    }


    @Test
    public void mustDropAllStoredProcedures() {
        try (Statement statement = connection.createStatement()){

            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getProcedures("Mentoring_8", null, null);
            while(resultSet.next()) {
                String name = resultSet.getString("PROCEDURE_NAME");
                String SQL = String.format("DROP PROCEDURE `%s`;", name);
                statement.executeUpdate(SQL);
                System.out.println(name);
            }
        } catch (SQLException e) {
            fail("Exception is not exprected");
        }
    }

    @Test
    public void mustCreateStoredProcedure() {
        try (Statement statement = connection.createStatement()){
            String[] procedureNames = {"getName", "setName", "showName"};
            for (String procName : procedureNames) {
                statement.execute("CREATE PROCEDURE `" + procName + "`(" +
                        "OUT USR_NAME VARCHAR(128)," +
                        "IN USR_ID INT ) " +
                        "BEGIN "+
                        "SELECT name INTO USR_NAME FROM User WHERE ID = USR_ID;"+
                        "END");
            }
        } catch(SQLException ex) {
            ex.printStackTrace();
            fail("Exception is not expected");
        }
    }


    @Test
    public void mustThrowSQLSyntaxException() {
        try (Statement statement = connection.createStatement()){
            statement.execute("CREATE PROCEDURE `getName`(" +
                    "OUT USR_NAME VARCHAR(128)," +
                    "IN USR_ID IN ) " +
                    "BEGIN "+
                    "SELECT name INTO USR_NAME FROM User WHERE ID = USR_ID;"+
                    "END");
            fail("Exception must be thrown");
        } catch(SQLException ex) {
        }
    }

    @After
    public void afterTest() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}