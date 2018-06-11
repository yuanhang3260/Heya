package filter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class UserLoginFilter implements Filter {
  private static final Set<String> ALLOWED_URIS_STATIC =
      new HashSet<>(Arrays.asList(
        "/",
        "/index.html",
        "/signup.html",
        "/test.html"
      ));

  private static final Set<String> ALLOWED_URIS = new HashSet<>(Arrays.asList(
      "/login",
      "/signup",
      "/dist",
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

    if (allowURI(uri)) {
      chain.doFilter(request, response);
      return;
    }

    HttpSession session = ((HttpServletRequest)request).getSession(false);
    if (session == null || session.getAttribute("user") == null) {
      // If no session or session expird, redirect to login page.
      ((HttpServletResponse)response).sendRedirect(appRoot);
      return;
    }

    chain.doFilter(request, response);
  }

  private boolean allowURI(String uri) {
    for (String allow : ALLOWED_URIS) {
      if (uri.startsWith(allow)) {
        return true;
      }
    }
    if (ALLOWED_URIS_STATIC.contains(uri)) {
      return true;
    }
    return false;
  }

  @Override
  public void init(FilterConfig config) throws ServletException {}

  @Override
  public void destroy() {}
}