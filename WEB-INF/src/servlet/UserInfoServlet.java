package servlet;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Properties;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONException;

import bean.User;
 
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

      User user = (User)session.getAttribute("user");
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

