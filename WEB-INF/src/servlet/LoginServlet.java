package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.util.ArrayList;
import org.json.simple.JSONObject;
 
public class LoginServlet extends HttpServlet {
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

      // Connect to databse.
      Properties p = new Properties();
      p.put("user", "hy");
      p.put("password", "123456");
      conn = DriverManager.getConnection("jdbc:mysql://localhost/Heya", p);
      stmt = conn.createStatement();

      // Database query.
      String sqlStr = "SELECT * from Users where username = \"" +
                      request.getParameter("username") + "\"";
      ResultSet rset = stmt.executeQuery(sqlStr);
      if (!rset.next()) {
        json_obj.put("success", false);
        json_obj.put("error", "user cannot be found");
        WriteResponse(response, json_obj);
        return;
      } else {
        if (!rset.getString("password").equals(
               request.getParameter("password"))) {
          json_obj.put("success", false);
          json_obj.put("error", "username/password doesn't match");
          WriteResponse(response, json_obj);
          return;
        }
        // Create new user successfully. Create session and return.
        HttpSession session = request.getSession(false);
        if (session != null) {
          // Remove current session.
          session.invalidate();
        }
        session = request.getSession();
        session.setAttribute("username", request.getParameter("username"));

        json_obj.put("success", true);
        WriteResponse(response, json_obj);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      json_obj.put("success", false);
      json_obj.put("error", "Server error, failed to log in");    
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

