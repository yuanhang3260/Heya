package servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import bean.User; 

public class HomeServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    RequestDispatcher d = request.getRequestDispatcher("home.jsp");
    d.forward(request, response);
  }
}

