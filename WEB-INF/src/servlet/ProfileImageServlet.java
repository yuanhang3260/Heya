package servlet;

import javax.imageio.ImageIO;  
import javax.imageio.ImageReadParam;  
import javax.imageio.ImageReader;
import java.io.*;
import java.nio.file.Paths;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Collection;

import javax.servlet.http.Part;
import org.json.JSONException;
import org.json.JSONObject;
 
public class ProfileImageServlet extends HttpServlet {
  @Override
  @SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("application/json");
    if (!request.getContentType().split(";")[0].equals("multipart/form-data")) {
      return;
    }

    Collection<Part> parts = request.getParts();
    for (Part part : parts){
      ProcessPart(part);
    }
  }

  private void ProcessPart(Part part) throws IOException {  
    if (part.getName().equals("imagedata")){
      String cd = part.getHeader("Content-Disposition");

      InputStream is = part.getInputStream();
      FileOutputStream fos = null;  
      try {
        // TODO(hangyuan): Write png file to user directory.
        fos = new FileOutputStream("/home/hy/Desktop/snoopy.png");
        int b = 0;
        while((b = is.read()) != -1) {
          fos.write(b);
        }
      } catch(Exception e){
        e.printStackTrace();
      } finally{
        try {
          fos.close();
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    }  
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

