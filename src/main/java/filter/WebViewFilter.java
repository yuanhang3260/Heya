package filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import application.SpringUtils;
import bean.User;
import dao.UserDAO;

public class WebViewFilter implements Filter {
  private UserDAO userDAO;

  @Override
  public void init(FilterConfig config) throws ServletException {
    this.userDAO = (UserDAO)SpringUtils.getBean("userDAO");
  }

  @Override
  public void destroy() {}

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    HttpSession session = ((HttpServletRequest)request).getSession(false);

    User viewer = (User)session.getAttribute("user");
    String viewerUsername = viewer.getUsername();
    String viewerUid = viewer.getUid();

    // Find owner of this page.
    String uid = viewerUid;
    String username = viewerUsername;

    String ownerUsername = request.getParameter("username");
    if (ownerUsername != null && !ownerUsername.equals(viewerUsername)) {
      User owner = this.userDAO.GetUserByUsername(ownerUsername);
      if (owner != null) {
        uid = owner.getUid();
        username = owner.getUsername();
      }
    }

    request.setAttribute("viewerUid", viewerUid);
    request.setAttribute("viewerUsername", viewerUsername);
    request.setAttribute("uid", uid);
    request.setAttribute("username", username);

    chain.doFilter(request, response);
  }
}
