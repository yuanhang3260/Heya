package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.*;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

import bean.User;
import db.DBManager;
 
public class UserInfoServlet extends HttpServlet {
  @Override
  @SuppressWarnings("unchecked")
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("application/json");
    JSONObject json_obj = new JSONObject();

    HttpSession session = request.getSession(false);
    try {
      if (session == null) {
        // No session found.
        json_obj.put("success", false);
        WriteResponse(response, json_obj);
        return;
      }

      User user;
      String username = request.getParameter("username");
      if (username == null) {
        user = (User)session.getAttribute("user");
      } else {
        user = User.GetUserByUsername(username);
      }
      if (user == null) {
        json_obj.put("success", false);
        json_obj.put("error", "user cannot be found");
        WriteResponse(response, json_obj);
        return;
      } else {
        // Get user detailed info.
        user.getUserDetailInfo();

        json_obj = user.toJSONObject();
        json_obj.put("success", true);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } finally {
      WriteResponse(response, json_obj);
    }
    return;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("application/json");
    JSONObject json_obj = new JSONObject();

    Logger log = Logger.getLogger("UserInfoServlet");

    HttpSession session = request.getSession(false);
    try {
      if (session == null) {
        // No session found.
        json_obj.put("success", false);
        WriteResponse(response, json_obj);
        return;
      }

      User user = (User)session.getAttribute("user");
      if (user == null ||
          !request.getParameter("username").equals(user.getUsername())) {
        // Requestor is not the user, reject.
        json_obj.put("success", false);
        json_obj.put("reason", "permission denied");
        WriteResponse(response, json_obj);
        return;
      }

      // Update user profile.
      String section = request.getParameter("section");
      if (section != null && section.equals("basic")) {
        json_obj = updateBasicInfo(request, response);
      } else {
        json_obj.put("success", false);
        json_obj.put("reason", "invalid request");
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return;
    } finally {
      WriteResponse(response, json_obj);
    }
  }

  private JSONObject updateBasicInfo(
      HttpServletRequest request, HttpServletResponse response) 
      throws JSONException {
    JSONObject json_obj = new JSONObject();

    int uid = -1;
    try {
      uid = Integer.parseInt(request.getParameter("uid"));
    } catch (NumberFormatException e) {
      e.printStackTrace();
      json_obj.put("success", false);
      json_obj.put("reason", "invalid uid");
      return json_obj;
    }
    String name = request.getParameter("name");
    String email = request.getParameter("email");
    if (email == null || email.isEmpty()) {
      json_obj.put("success", false);
      json_obj.put("reason", "email must be set");
      return json_obj;
    }
    String phone = request.getParameter("phone");

    JSONObject birthJSON;
    Date birth;
    birthJSON = new JSONObject((String)request.getParameter("birth"));

    try {
      birth = new GregorianCalendar(birthJSON.getInt("year"),
                                    birthJSON.getInt("month") - 1,  // hehe
                                    birthJSON.getInt("date")).getTime();
    } catch (JSONException e) {
      birth = null;
    }

    if (!User.updateUserBaiscInfo(uid, name, email, phone, birth)) {
      json_obj.put("success", false);
      json_obj.put("reason", "internal database error");
      return json_obj;
    }

    json_obj.put("success", true);
    return json_obj;
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

