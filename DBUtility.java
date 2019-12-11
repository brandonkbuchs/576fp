package org.webproject.servlet;

import org.json.JSONArray;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBUtility {
    private static final String Driver = "org.postgresql.Driver";
    private static final String ConnUrl = "jdbc:postgresql://geog576.cutvxrpoyqql.us-east-2.rds.amazonaws.com:5431/trailupdate";
    private static final String Username = "postgres";
    private static final String Password = "postgres-admin";

    // This is a constructor
    public DBUtility() {
    }

    // create a Connection to the database
    private Connection connectDB() {
        Connection conn = null;
        try {
            Class.forName(Driver);
            conn = DriverManager.getConnection(ConnUrl, Username, Password);
            System.out.println(conn.toString());
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    // execute a sql query (e.g. SELECT) and return a ResultSet
    public ResultSet queryDB(String sql) {
        Connection conn = connectDB();
        ResultSet res = null;
        try {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                res = stmt.executeQuery(sql);
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    // execute a sql query (e.g. INSERT) to modify the database;
    // no return value needed
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

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        // You can test the methods you created here
        JSONArray list = new JSONArray();
        DBUtility dbutil = new DBUtility();

        String sql = "SELECT report.id, report_type, damage_type, " +
                "obstruction_type, ST_X(geom) as longitude, ST_Y(geom) as latitude, " +
                "report.add_msg FROM report, damage_report, obstruction_report WHERE " +
                "is_resolved = 'n'";

        ResultSet res = dbutil.queryDB(sql);

        while(res.next()) {
            HashMap<String, String> m = new HashMap<String, String>();
            m.put("report_id", res.getString("id"));
            m.put("report_type", res.getString("report_type"));
            if (res.getString("report_type").equalsIgnoreCase("damage")) {
                m.put("damage_type", res.getString("damage_type"));
            } else if (res.getString("report_type").equalsIgnoreCase("obstruction")) {
                m.put("obstruction_type", res.getString("obstruction_type"));
            }
            m.put("latitude", res.getString("latitude"));
            m.put("longitude", res.getString("longitude"));
            m.put("add_msg", res.getString("add_msg"));
            list.put(m);
        }
        String jsonString = list.toString();
        System.out.println(jsonString);
    }
 }
