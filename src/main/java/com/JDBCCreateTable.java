package com;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCCreateTable {

    private static final String DBURL =
            "jdbc:mysql://localhost:3306/mentoring_8";
    private static final String DBDRIVER = "com.mysql.cj.jdbc.Driver";

    static {
        try {
            Class.forName(DBDRIVER).newInstance();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static Connection getConnection()
    {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBURL, "root", "1111");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void testCreating(Connection connection) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.execute("CREATE PROCEDURE `WhoAreThey`(" +
                    "OUT error VARCHAR(128)," +
                    "IN office VARCHAR(10)) " +
                    "BEGIN "+
                    "SET error = NULL; "+
                    "IF office IS NULL THEN "+
                    "SET error = 'You need to pass in an office number'; "+
                    "ELSE "+
                    "  SELECT EmployeeID, Name FROM " +
                    " employees WHERE office = office; "+
                    "END IF; "+
                    "END");

        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        Connection con = getConnection();
        Statement stmt =null;
        try {
            stmt = con.createStatement();
            stmt.execute("CREATE PROCEDURE `WhoAreThey`(" +
                    "OUT error VARCHAR(128)," +
                    "IN office VARCHAR(10)) " +
                    "BEGIN "+
                    "SET error = NULL; "+
                    "IF office IS NULL THEN "+
                    "SET error = 'You need to pass in an office number'; "+
                    "ELSE "+
                    "  SELECT EmployeeID, Name FROM " +
                    " employees WHERE office = office; "+
                    "END IF; "+
                    "END");

        } catch(SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
        finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    System.err.println("SQLException: " + e.getMessage());
                }
            }
        }
    }

}
