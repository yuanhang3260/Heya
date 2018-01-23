package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.util.ArrayList;
import org.json.simple.JSONObject;

import bean.User;

public class LoginServlet extends HttpServlet {
  @Override
  @SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    JSONObject json_obj = new JSONObject();

    User user = User.GetUserByUsername(request.getParameter("username"));
    if (user == null) {
      json_obj.put("success", false);
      json_obj.put("error", "user cannot be found");
      WriteResponse(response, json_obj);
      return;
    } else {
      if (!user.getPassword().equals(request.getParameter("password"))) {
        json_obj.put("success", false);
        json_obj.put("error", "user/password mismatch");
        WriteResponse(response, json_obj);
        return;
      }

      // Login successfully, create session and return.
      HttpSession session = request.getSession(false);
      if (session != null) {
        // Remove current session.
        session.invalidate();
      }
      session = request.getSession();
      session.setAttribute("user", user);

      json_obj.put("success", true);
      WriteResponse(response, json_obj);
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

