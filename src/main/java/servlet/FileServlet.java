package servlet;

import java.io.*;
import java.nio.file.Files;
import java.net.URLDecoder;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class FileServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String filename = URLDecoder.decode(
        request.getPathInfo().substring(1), "UTF-8");
    File file = new File("/home/hy/WebStorage/Heya", filename);
    if (!file.exists()) {
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
      return;
    }
    response.setHeader("Content-Type",
                       getServletContext().getMimeType(filename));
    response.setHeader("Content-Length", String.valueOf(file.length()));
    response.setHeader("Content-Disposition",
                       "inline; filename=\"" + file.getName() + "\"");
    Files.copy(file.toPath(), response.getOutputStream());
  }
}

