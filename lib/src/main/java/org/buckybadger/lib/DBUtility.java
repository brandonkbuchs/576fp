package org.buckybadger.lib;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtility {
    private static final String Driver = "org.postgresql.Driver";
    private static final String ConnUrl = "jdbc:postgresql://geog576.cutvxrpoyqql.us-east-2.rds.amazonaws.com:5431/trailupdate";
    private static final String Username = "postgres";
    private static final String Password = "postgres-admin";

    //Constructor
    public DBUtility() {}

    //Connect to the db
    private Connection connectDB() {
        Connection conn = null;
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(ConnUrl, Username, Password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    //Query Database; return results
    public ResultSet queryDB(String sql) {
        Connection conn = connectDB();
        ResultSet rs = null;

        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                conn.close();
            }
        } catch (Exception e) {
                e.printStackTrace();
            }
        return rs;
    }

    //Perform DB Modifications (INSERT, UPDATE)
    public void modifyDB(String sql) {
        Connection conn = connectDB();

        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                stmt.execute(sql);
                stmt.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {

        String sql = "SELECT * FROM report";

        DBUtility util = new DBUtility();

        ResultSet rs = util.queryDB(sql);

        while (rs.next()) {
            System.out.println(rs.getString("message"));
        }



    }

}
