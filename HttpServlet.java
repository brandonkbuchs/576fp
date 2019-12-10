package org.webproject.servlet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Servlet implementation class HttpServlet
 */
@WebServlet("/HttpServlet")
public class HttpServlet extends javax.servlet.http.HttpServlet {
    private static final long serialVersionUID = 1L;

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
            System.out.println("Query");
            try {
                System.out.println("Query Running...");
                queryReport(request, response);
                System.out.println("Query Success");
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // resolve reports that have been closed
        else if (tab_id.equals("2")) {
            System.out.println("Updated report table");
            try {
                resolveReport(request, response);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        else if (tab_id.equals("3")) {
            System.out.println("Report selected for resolution");
            try {
                queryResolveReport(request, response);
            } catch (SQLException e) {
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
        String tel = request.getParameter("telephone");
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
        String add_msg = request.getParameter("add_msg");
        String is_resolved = "false";
        if (report_type != null) {report_type = "'" + report_type + "'";}
        if (damage_type != null) {damage_type = "'" + damage_type + "'";}
        if (obstruction_type != null) {obstruction_type = "'" + obstruction_type + "'";}
        if (timestamp != null) {timestamp = "'" + timestamp + "'";}
        if (add_msg != null) {add_msg = "'" + add_msg + "'";}

        // record report_id
        ResultSet res_reportID = dbutil.queryDB("select last_value from report_id_seq");
        res_reportID.next();
        report_id = res_reportID.getInt(1);

        sql = "insert into report (reporter_id, report_type, timestamp, geom," +
                "add_msg) values (" + user_id + "," + report_type + ","
                + ", ST_GeomFromText('POINT(" + lon + " " + lat + ")', 4326)" + "," +
                add_msg + ")";

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
        String report_type = request.getParameter("report_type");
        String damage_type = request.getParameter("damage_type");
        String obstruction_type = request.getParameter("obstruction_type");

        // damage or obstruction report
        if (report_type == null || report_type.equalsIgnoreCase("damage")) {
            String sql = "select report.id, report_type, damage_type, is_resolved, " +
                    "first_name, last_name, timestamp, ST_X(geom) as longitude, " +
                    "ST_Y(geom) as latitude, report.add_msg from report, users, damage_report" +
                    " where reporter_id = users.id and report.id = " +
                    "report_id";
            queryReportHelper(sql,list,"damage", damage_type);
          }

        // donation report
        if (report_type == null || report_type.equalsIgnoreCase("obstruction")) {
            String sql = "select report.id, report_type, obstruction_type, is_resolved, " +
                    "obstruction_type, first_name, last_name, timestamp, ST_X(geom) as " +
                    "longitude, ST_Y(geom) as latitude, report.add_msg from report, users, " +
                    "obstruction_report where reporter_id = users.id and report.id = " +
                    "report_id";
            queryReportHelper(sql,list,"obstruction",obstruction_type);
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
            if (report_type.equalsIgnoreCase("damage")) {
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
            m.put("add_msg", res.getString("add_msg"));
            m.put("is_resolved", res.getString("is_resolved"));
            list.put(m);
        }
    }

    private void resolveReport(HttpServletRequest request, HttpServletResponse
            response) throws SQLException, IOException {
        DBUtility dbutil = new DBUtility();
        String sql;

        //Identify report ID for updating
        String report_id;
        report_id = request.getParameter("report_id");

        sql = "UPDATE report SET is_resolved = 'y' WHERE id =  "+ report_id;

        dbutil.modifyDB(sql);

    }

    private void queryResolveReport(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        DBUtility dbutil = new DBUtility();
        String sql;

        String report_id;
        report_id = request.getParameter("report_id");

        sql = "SELECT FROM report WHERE report_id = " + report_id;

        dbutil.modifyDB(sql);
    }

    public void main() throws JSONException {
    }
}