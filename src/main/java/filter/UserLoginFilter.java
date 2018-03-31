package filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserLoginFilter implements Filter {
  private static final Set<String> ALLOWED_URIS = new HashSet<>(Arrays.asList(
    "/",
    "/index.html",
    "/login",
    "/signup.html",
    "/signup",
    "/test.html",
    "/heyatest"
  ));

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    ServletContext context = request.getServletContext();
    String appRoot = context.getInitParameter("app-root");

    // Remove the app root prefix "/Heya" for URI.
    String uri = ((HttpServletRequest)request).getRequestURI();
    if (uri.startsWith(appRoot)) {
      uri = uri.substring(appRoot.length());
    }

    if (ALLOWED_URIS.contains(uri) || uri.startsWith("/dist")) {
      chain.doFilter(request, response);
      return;
    }

    // Get User object and forward to home.jsp.
    HttpSession session = ((HttpServletRequest)request).getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      // If no session or session expird, redirect to login page.
      ((HttpServletResponse)response).sendRedirect(appRoot);
      return;
    }

    chain.doFilter(request, response);
  }

  @Override
  public void init(FilterConfig config) throws ServletException {}

  @Override
  public void destroy() {}
}