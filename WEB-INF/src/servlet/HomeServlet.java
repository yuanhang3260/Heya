package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class HomeServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // Get User object and forward to home.jsp.
    HttpSession session = request.getSession();
    session = request.getSession();
    // session.setAttribute("username", request.getParameter("username"));

    RequestDispatcher d = request.getRequestDispatcher("home.jsp");
    d.forward(request, response);
  }
}

