package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.util.ArrayList;
import org.json.simple.JSONObject;
 
public class SignupServlet extends HttpServlet {
  @Override
  @SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    Connection conn = null;
    Statement stmt = null;
    JSONObject json_obj = new JSONObject();
    int re = 0;
    try {
      try {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }

      // Connect to databse
      Properties p = new Properties();
      p.put("user", "hy");
      p.put("password", "123456");
      conn = DriverManager.getConnection("jdbc:mysql://localhost/Heya", p);
      stmt = conn.createStatement();

      // Database query
      String sqlStr = "SELECT * from Users where userName = \"" +
                      request.getParameter("username") + "\"";
      ResultSet rset = stmt.executeQuery(sqlStr);
      if (rset.next()) {
        // User already exists, return error.
        json_obj.put("success", false);
        json_obj.put("error", "Username already used");
        WriteResponse(response, json_obj);
        return;
      } else {
        sqlStr = "SELECT MAX(uid) as max_uid FROM Users";
        rset = stmt.executeQuery(sqlStr);
        int uid = 0;
        if (rset.next()) {
          uid = rset.getInt("max_uid") + 1;
          if (rset.wasNull()) {
            uid = 0;
          }
        }

        // TODO: Encrypt password!
        sqlStr = "INSERT INTO Users (uid, username, email, password) VALUES (" +
                 Integer.toString(uid) + ", " +
                 buildSqlStringValue(request.getParameter("username")) + ", " +
                 buildSqlStringValue(request.getParameter("email")) + ", " +
                 buildSqlStringValue(request.getParameter("password")) + ")";
        int new_records = stmt.executeUpdate(sqlStr);
        if (new_records != 1) {
          json_obj.put("success", false);
          json_obj.put("error", "Server error, failed to create new user");
          WriteResponse(response, json_obj);
          return;
        }

        // Create new user successfully. Create session and return.
        HttpSession session = request.getSession();
        session.setAttribute("username", request.getParameter("username"));

        json_obj.put("success", true);
        WriteResponse(response, json_obj);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      json_obj.put("success", false);
      json_obj.put("error", "Database error, failed to create new user");    
      json_obj.put("success", true);
      WriteResponse(response, json_obj);
    } finally {
      try {
        // Close the sql connection.
        if (stmt != null) {
          stmt.close();
        }
        if (conn != null) {
          conn.close();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
    return;
  }

  private String buildSqlStringValue(String str) {
    return "\"" + str + "\"";
  }

  private void WriteResponse(HttpServletResponse response,
                             JSONObject json_obj) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      out.write(json_obj.toString());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }
}

