package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.json.simple.JSONObject;
 
public class LoginServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // response.setContentType("application/json; charset=utf-8");
    // String username = request.getParameter("username");
    // String password = request.getParameter("password");
    // String rememberme = request.getParameter("rememberme");
    // JSONObject obj = new JSONObject();
    // obj.put("username", username);
    // obj.put("password", password);
    // obj.put("rememberme", rememberme);

    // PrintWriter out = null;
    // try {
    //   out = response.getWriter();
    //   out.write(obj.toString());
    // } catch (IOException e) {
    //   e.printStackTrace();
    // } finally {
    //   if (out != null) {
    //     out.close();
    //   }
    // }
    request.getSession();

    String redirect_to = "test.html";
    RequestDispatcher d = request.getRequestDispatcher(redirect_to);
    d.forward(request, response);
    return;
  }
}

