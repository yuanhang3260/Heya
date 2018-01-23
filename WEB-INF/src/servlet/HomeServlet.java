package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import bean.User; 

public class HomeServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // Get User object and forward to home.jsp.
    HttpSession session = request.getSession(false);
    if (session == null) {
      // If no session or session expird, redirect to login page.
      response.sendRedirect("index.html");
      return;
    }
    
    User user = (User)session.getAttribute("user");
    if (user == null) {
      // Is it not possible?
      response.sendRedirect("index.html");
      return;
    }

    RequestDispatcher d = request.getRequestDispatcher("home.jsp");
    d.forward(request, response);
  }
}

