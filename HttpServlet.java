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
    }

    private void createReport(HttpServletRequest request, HttpServletResponse
            response) throws SQLException, IOException {
        DBUtility dbutil = new DBUtility();
        String sql;


        // create trail report
        int report_id = 0;
        String reporter_id = request.getParameter("reporter_id");
        String report_type = request.getParameter("report_type");
        String damage_type = request.getParameter("damage_type");
        String obstruction_type = request.getParameter("obstruction_type");
        String timestamp = request.getParameter("timestamp");
        String lon = request.getParameter("longitude");
        String lat = request.getParameter("latitude");
        String add_msg = request.getParameter("add_msg");

        if (reporter_id != null) {reporter_id = "'" + reporter_id + "'";}
        if (report_type != null) {report_type = "'" + report_type + "'";}
        if (damage_type != null) {damage_type = "'" + damage_type + "'";}
        if (obstruction_type != null) {obstruction_type = "'" + obstruction_type + "'";}
        if (timestamp != null) {timestamp = "'" + timestamp + "'";}
        if (add_msg != null) {add_msg = "'" + add_msg + "'";}

        // record report_id
        ResultSet res_reportID = dbutil.queryDB("select last_value from report_id_seq");
        res_reportID.next();
        report_id = res_reportID.getInt(1);
        System.out.println(report_id);


        // create damage or obstruction report
        if (report_type.equalsIgnoreCase("'damage'")) {
            sql = "insert into report (reporter_id, report_type, timestamp, geom," +
                    " add_msg, damage_type) values (" + reporter_id + "," + report_type + ","
                    + timestamp + ", ST_GeomFromText('POINT(" + lon + " " + lat + ")', 4326)" + "," +
                    add_msg + "," + damage_type + ")";

            dbutil.modifyDB(sql);
            System.out.println("Success! Damage report created.");
        } else if (report_type.equalsIgnoreCase("'obstruction'")) {
            sql = "insert into report (reporter_id, report_type, timestamp, geom," +
                    " add_msg, obstruction_type) values (" + reporter_id + "," + report_type + ","
                    + timestamp + ", ST_GeomFromText('POINT(" + lon + " " + lat + ")', 4326)" + "," +
                    add_msg + "," + obstruction_type + ")";
            dbutil.modifyDB(sql);
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

    private void queryReport(HttpServletRequest request, HttpServletResponse response) throws JSONException, SQLException {
        JSONArray list = new JSONArray();
        DBUtility dbutil = new DBUtility();
        ResultSet res = null;

        String sql = "SELECT id, report_type, report.damage_type, report.obstruction_type, ST_X(geom) as longitude, ST_Y(geom) as latitude, add_msg FROM report WHERE is_resolved = 'n' ORDER BY id";
        try {
            res = dbutil.queryDB(sql);

            while (res.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String jsonString = list.toString();
        System.out.println(jsonString);
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

    public void main() throws JSONException {
    }
}