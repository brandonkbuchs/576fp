//change package name
//package org.webproject.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webproject.servlet.DBUtility;

/**
 * Servlet implementation class HttpServlet
 */
@WebServlet("/HttpServlet")
public class HttpServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see javax.servlet.http.HttpServlet#javax.servlet.http.HttpServlet()
     */
    public HttpServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }


    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String tab_id = request.getParameter("tab_id");

        // create a report
        if (tab_id.equals("0")) {
            System.out.println("A report is submitted via HttpServlet");
            try {
                createReport(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // query reports
        else if (tab_id.equals("1")) {
            try {
                queryReport(request, response);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private void createReport(HttpServletRequest request, HttpServletResponse
            response) throws SQLException, IOException {
        DBUtility dbutil = new DBUtility();
        String sql;


        // 2. create user
        int user_id = 0;
        String first_name = request.getParameter("first_name");
        String last_name = request.getParameter("last_name");
        String is_male = request.getParameter("is_male");
        String age = request.getParameter("age");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");
        if (first_name != null) {first_name = "'" + first_name + "'";}
        if (last_name != null) {last_name = "'" + last_name + "'";}
        if (is_male != null) {is_male = "'" + is_male + "'";}
        if (age != null) {age = "'" + age + "'";}
        if (tel != null) {tel = "'" + tel + "'";}
        if (email != null) {email = "'" + email + "'";}

        sql = "insert into users (first_name, last_name, is_male, age, telephone, email) values (" + first_name +
                "," + last_name + "," + is_male + "," + age + "," + tel +
                "," + email + ");";
 
        dbutil.modifyDB(sql);

        // record user_id
        ResultSet res_userID = dbutil.queryDB("select last_value from users_id_seq");
        res_userID.next();
        user_id = res_userID.getInt(1);

        // create trail report
        int report_id = 0;
        String report_type = request.getParameter("report_type");
        String damage_type = request.getParameter("damage_type");
        String obstruction_type = request.getParameter("obstruction_type");
        String timestamp = request.getParameter("timestamp");
        String lon = request.getParameter("longitude");
        String lat = request.getParameter("latitude");
        String message = request.getParameter("message");
		String is_resolved = "false";
        if (report_type != null) {report_type = "'" + report_type + "'";}
        if (damage_type != null) {damage_type = "'" + damage_type + "'";}
        if (obstruction_type != null) {obstruction_type = "'" + obstruction_type + "'";}
        if (timestamp != null) {timestamp = "'" + timestamp + "'";}
        if (message != null) {message = "'" + message + "'";}
		
		// record report_id
        ResultSet res_reportID = dbutil.queryDB("select last_value from report_id_seq");
        res_reportID.next();
        report_id = res_reportID.getInt(1);
 
		sql = "insert into report (reporter_id, report_type, timestamp, geom," +
                " message) values (" + user_id + "," + report_type + ","
                + ", ST_GeomFromText('POINT(" + lon + " " + lat + ")', 4326)" + "," +
                message + is_resolved +")";
				
        dbutil.modifyDB(sql);

        // create damage or obstruction report
        if (report_type.equalsIgnoreCase("'damage'")) {
            sql = "insert into damage_report (report_id, damage_type) values ('"
                    + report_id + "'," + damage_type + ")";
            System.out.println("Success! Damage report created.");
        } else if (report_type.equalsIgnoreCase("'obstruction'")) {
            sql = "insert into obstruction_report (report_id, obstruction_type) values ('"
                    + report_id + "'," + obstruction_type + ")";
            System.out.println("Success! Obstruction report created.");
        } else {
            return;
        }
        dbutil.modifyDB(sql);
		

        // response that the report submission is successful
        JSONObject data = new JSONObject();
        try {
            data.put("status", "success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        response.getWriter().write(data.toString());
    }

    private void queryReport(HttpServletRequest request, HttpServletResponse
            response) throws JSONException, SQLException, IOException {
        JSONArray list = new JSONArray();
        String timestamp =
                new java.text.SimpleDateFormat("MM/dd/yyyy h:mm:ss a").format(new Date());

        String report_type = request.getParameter("report_type");
        String damage_or_obstruction = request.getParameter("damage_or_obstruction");
		String is_resolved = request.getParameter("is_resolved");

        // damage or obstruction report
        if (report_type == null || report_type.equalsIgnoreCase("damage")) {
            String sql = "select report.id, report_type, damage_type, " +
                    "first_name, last_name, timestamp, ST_X(geom) as longitude," +
                    "ST_Y(geom) as latitude, message from report, user, " +
                    "where reporter_id = user.id and report.id = " +
                    "report_id";
            queryReportHelper(sql,list,"damage",damage_or_obstruction);
        }

        // donation report
        if (report_type.equalsIgnoreCase("obstruction")) {
            String sql = "select report.id, report_type, obstruction_type, " +
                    "disaster_type, first_name, last_name, timestamp, ST_X(geom) as " +
                    "longitude, ST_Y(geom) as latitude, message from report, user, " +
                    "obstruction_report where reporter_id = user.id and report.id = " +
                    "report_id";
            queryReportHelper(sql,list,"obstruction",damage_or_obstruction);
        }

        response.getWriter().write(list.toString());
    }

    private void queryReportHelper(String sql, JSONArray list, String report_type,
                                   String damage_or_obstruction) throws SQLException {
        DBUtility dbutil = new DBUtility();

        if (damage_or_obstruction != null) {
            if (report_type.equalsIgnoreCase("damage")) {
                sql += " and damage_type = '" + damage_or_obstruction + "'";
            } else {
                sql += " and obstruction_type = '" + damage_or_obstruction + "'";
            }
        }
        ResultSet res = dbutil.queryDB(sql);
        while (res.next()) {
            // add to response
            HashMap<String, String> m = new HashMap<String,String>();
            m.put("report_id", res.getString("id"));
            m.put("report_type", res.getString("report_type"));
            if (report_type.equalsIgnoreCase("damage") ||
                    report_type.equalsIgnoreCase("request")) {
                m.put("damage_type", res.getString("damage_type"));
            }
            else if (report_type.equalsIgnoreCase("obstruction")) {
                m.put("obstruction_type", res.getString("obstruction_type"));
            }
            m.put("report_type", res.getString("report_type"));
            m.put("first_name", res.getString("first_name"));
            m.put("last_name", res.getString("last_name"));
            m.put("timestamp", res.getString("timestamp"));
            m.put("longitude", res.getString("longitude"));
            m.put("latitude", res.getString("latitude"));
            m.put("message", res.getString("message"));
            list.put(m);
        }
    }

    public void main() throws JSONException {
    }
}