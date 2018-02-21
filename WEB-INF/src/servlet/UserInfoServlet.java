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
        json_obj = updateBasicInfo(user, request, response);
      } else if (section != null && section.equals("education")) {
        json_obj = updateEducationInfo(user, request, response);
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

  private JSONObject updateBasicInfo(User user,
                                     HttpServletRequest request,
                                     HttpServletResponse response) 
      throws JSONException {
    JSONObject json_obj = new JSONObject();

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

    if (!user.updateUserBasicInfo(name, email, phone, birth)) {
      json_obj.put("success", false);
      json_obj.put("reason", "internal database error");
      return json_obj;
    }

    json_obj.put("success", true);
    return json_obj;
  }

  private JSONObject updateEducationInfo(User user,
                                         HttpServletRequest request,
                                         HttpServletResponse response)
      throws JSONException {
    JSONObject json_obj = new JSONObject();

    String school = request.getParameter("school");
    String major = request.getParameter("major");

    JSONObject yearJSON;
    yearJSON = new JSONObject(request.getParameter("year"));
    Integer startYear = null, endYear = null;
    try {
      startYear = new Integer(yearJSON.getInt("start"));
      endYear = new Integer(yearJSON.getInt("end"));
    } catch (JSONException e) {
      startYear = null;
      endYear = null;
      e.printStackTrace();
    }

    String action = request.getParameter("action");
    json_obj.put("action", action);

    boolean success = false;
    if (action.equals("add")) {
      int sid = user.addSchoolInfo(school, major, startYear, endYear);
      if (sid >= 0) {
        success = true;
        json_obj.put("schoolId", sid);
      } else {
        success = false;
        json_obj.put("reason", "internal database error");
      }
    } else {
      int sid = -1;
      if (request.getParameter("sid") != null) {
        try {
          sid = Integer.parseInt(request.getParameter("sid"));
        } catch (NumberFormatException e) {}
      }

      if (sid > 0) {
        if (action.equals("update")) {
          if (!user.updateSchoolInfo(sid, school, major, startYear, endYear)) {
            success = false;
            json_obj.put("reason", "internal database error");
          }
        } else if (action.equals("delete")) {

        }
      } else {
        success = false;
        json_obj.put("reason", "school id not specified");
      }
    }

    json_obj.put("success", success);
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

