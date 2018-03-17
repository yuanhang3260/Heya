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
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");
    String uid = request.getParameter("uid");
    String type = request.getParameter("type");
    if (type.equals("profile")) {
      String path = Paths.get(
          baseDir, "user/", uid, "/profile.jpg").toString();
      File file = new File(path);
      String imgLocation;
      if (file.exists()) {
        imgLocation = Paths.get("/data", "user", uid, "profile.jpg").toString();
      } else {
        imgLocation = Paths.get(
            "/data", "user", "default", "profile.jpg").toString();
      }
      // Forward to FileSevlet for /data/* requests.
      request.getRequestDispatcher(imgLocation).forward(request, response);
    } else if (type.equals("cover")) {
      String path = Paths.get(
          baseDir, "user/", uid, "/cover.jpg").toString();
      File file = new File(path);
      String imgLocation;
      if (file.exists()) {
        imgLocation = Paths.get("/data", "user", uid, "cover.jpg").toString();
      } else {
        imgLocation = Paths.get(
            "/data", "user", "default", "cover.jpg").toString();
      }
      // Forward to FileSevlet for /data/* requests.
      request.getRequestDispatcher(imgLocation).forward(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    response.setContentType("application/json");
    JSONObject json_obj = new JSONObject();

    if (!request.getContentType().split(";")[0].equals("multipart/form-data")) {
      return;
    }

    try {
      Collection<Part> parts = request.getParts();

      // Get uid.
      int uid = -1;
      for (Part part : parts){
        if (part.getName().equals("uid")){
          if ((uid = ProcessUid(part)) > 0) {
            break;
          }
        }
      }

      if (uid < 0) {
        json_obj.put("success", false);
        json_obj.put("err", "uid not given");
        return;
      }

      // Process image data.
      boolean success = false;
      for (Part part : parts) {
        if (part.getName().equals("imagedata")){
          if (UpdateProfileImageFile(uid, part)) {
            success = true;
            break;
          }
        }
      }
      json_obj.put("success", success);
      if (!success) {
        json_obj.put("err", "update profile image failed");
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } finally {
      WriteResponse(response, json_obj);
    }
  }

  private int ProcessUid(Part part) {
    try {
      byte[] data = new byte[32];
      int length = part.getInputStream().read(data);
      int uid = Integer.parseInt(new String(data, 0, length));
      return uid;
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    } catch (NumberFormatException e) {
      e.printStackTrace();
      return -1;
    }
  }

  private boolean UpdateProfileImageFile(int uid, Part part)
      throws IOException {
    String cd = part.getHeader("Content-Disposition");

    String type = part.getHeader("Content-Type");
    String[] re = type.split("/");
    if (re.length != 2) {
      return false;
    }
    String ext = getImageFileExt(re[1]);
    if (ext == null) {
      return false;
    }
    if (!ext.equals("jpg")) {
      return false;
    }

    InputStream is = part.getInputStream();
    FileOutputStream fos = null;
    try {
      String imageFile = Paths.get(
          "/home/hy/WebStorage/Heya",
          "user", Integer.toString(uid),  "profile." + ext).toString();
      fos = new FileOutputStream(imageFile);
      int b = 0;
      while((b = is.read()) != -1) {
        fos.write(b);
      }
      return true;
    } catch(IOException e) {
      e.printStackTrace();
      return false;
    } finally{
      try {
        fos.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  private String getImageFileExt(String type) {
    if (type.equals("png")) {
      return "png";
    } else if (type.equals("jpg") || type.equals("jpeg")) {
      return "jpg";
    } else if (type.equals("bmp")) {
      return "bmp";
    } else {
      return null;
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

