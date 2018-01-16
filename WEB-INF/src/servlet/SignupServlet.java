package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.util.ArrayList;
import org.json.simple.JSONObject;

import bean.User;
 
public class SignupServlet extends HttpServlet {
  @Override
  @SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    JSONObject json_obj = new JSONObject();

    User user = User.GetUserByUsername(request.getParameter("username"));
    if (user != null) {
      // User already exists, return error.
      json_obj.put("success", false);
      json_obj.put("error", "Username already used");
      WriteResponse(response, json_obj);
      return;
    } else {
      int uid = User.getNextUid();
      if (uid < 0) {
        json_obj.put("success", false);
        json_obj.put("error", "Server error, failed to create new user");    
        WriteResponse(response, json_obj);
      }

      // TODO: Encrypt password!
      if (!User.addNewUser(uid,
                           request.getParameter("username"),
                           request.getParameter("email"),
                           request.getParameter("password"))) {
        json_obj.put("success", false);
        json_obj.put("error", "Server error, failed to create new user");
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

