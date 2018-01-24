package servlet;

import java.io.*;
import java.nio.file.Paths;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class ImageServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String type = request.getParameter("type");
    if (type == null) {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    } else if (type.equals("userinfo")) {
      String uid = request.getParameter("uid");
      String username = request.getParameter("username");
      String query = request.getParameter("q");
      if (query == null) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      } else {
        if (query.equals("profile") || query.equals("profile-cover")) {
          if (uid == null || username == null) {
            uid = "default";
          }
          String imgLocation =
              Paths.get("/data", "user", uid, query + ".jpg").toString();
          request.getRequestDispatcher(imgLocation).forward(request, response);
          return;
        }
      }
    }
    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
  }
}

