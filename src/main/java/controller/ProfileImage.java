package controller;

import java.io.*;
import java.nio.file.Paths;
import java.util.Collection;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bean.User;
import util.JsonUtils;

@Controller
@RequestMapping("/profileimage/{type}/{username}")
public class ProfileImage {

  public static String profileImageURL(String username) {
    return "profileimage/profile/" + username;
  }

  public static String profileCoverImageURL(String username) {
    return "profileimage/cover/" + username;
  }

  private User getAuthorizedUser(HttpSession session, String username) {
    User user = (User)session.getAttribute("user");
    if (user != null && username != null &&
        username.equals(user.getUsername())) {
      return user;
    }
    return null;
  }

  @RequestMapping(value="", method=RequestMethod.GET)
  public void getProfileImage(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable("username") String username,
                              @PathVariable("type") String type)
      throws IOException, ServletException {
    if (username == null || type == null) {
      JsonUtils.WriteJSONResponse(response, false, "Invalid request");
      return;
    }

    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");
    if (type.equals("profile")) {
      String path =
        Paths.get(baseDir, "user/", username, "/profile.jpg").toString();
      File file = new File(path);
      String imgLocation;
      if (file.exists()) {
        imgLocation =
          Paths.get("/data", "user", username, "profile.jpg").toString();
      } else {
        imgLocation = Paths.get(
            "/data", "user", "__default__", "profile.jpg").toString();
      }
      // Forward to FileSevlet for /data/* requests.
      request.getRequestDispatcher(imgLocation).forward(request, response);
    } else if (type.equals("cover")) {
      String path = Paths.get(
          baseDir, "user/", username, "/cover.jpg").toString();
      File file = new File(path);
      String imgLocation;
      if (file.exists()) {
        imgLocation =
            Paths.get("/data", "user", username, "cover.jpg").toString();
      } else {
        imgLocation = Paths.get(
            "/data", "user", "__default__", "cover.jpg").toString();
      }
      // Forward to FileSevlet for /data/* requests.
      request.getRequestDispatcher(imgLocation).forward(request, response);
    } else {
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

  @RequestMapping(value="", method=RequestMethod.POST)
  public void updateProfileImage(
      HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable("username") String username,
      @PathVariable("type") String type,
      @RequestHeader("Content-Type") String contentType)
      throws IOException, ServletException {
    if (username == null || type == null) {
      JsonUtils.WriteJSONResponse(response, false, "Invalid request");
      return;
    }

    if (contentType == null ||
        !contentType.split(";")[0].equals("multipart/form-data")) {
      JsonUtils.WriteJSONResponse(response, false, "Invalid content type");
      return;
    }

    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");

    // Process image data.
    Collection<Part> parts = request.getParts();
    boolean success = false;
    for (Part part : parts) {
      if (part.getName().equals("imagedata")){
        if (UpdateProfileImageFile(username, part, baseDir)) {
          success = true;
          break;
        }
      }
    }

    JsonUtils.WriteJSONResponse(response, success,
                                success ? null : "Update profile image failed");
  }

  private boolean UpdateProfileImageFile(String username, Part part, String baseDir)
      throws IOException {
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
          baseDir, "user", username, "profile.jpg").toString();
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
}
