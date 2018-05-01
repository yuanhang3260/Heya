package controller;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.GregorianCalendar;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import application.SpringUtils;
import bean.Post;
import bean.User;
import dao.PostDAO;
import util.JsonUtils;
import util.UuidUtils;

@Controller
@RequestMapping("/post/{username}")
public class HeyaPost {
  private static final Logger log = Logger.getLogger(HeyaPost.class);

  private static String dataDir = null;
  static String getDataDir(HttpServletRequest request) {
    if (dataDir == null) {
      ServletContext context = request.getServletContext();
      dataDir = context.getInitParameter("data-storage");
    }
    return dataDir;
  } 

  @Autowired
  PostDAO postDAO;

  private User getAuthorizedUser(HttpSession session, String username) {
    User user = (User)session.getAttribute("user");
    if (user != null && username != null &&
        username.equals(user.getUsername())) {
      return user;
    }
    return null;
  }

  @RequestMapping(value="/all", method=RequestMethod.GET)
  public void getPosts(
      HttpServletRequest request,
      HttpServletResponse response,
      HttpSession session,
      @PathVariable("username") String username) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }
    String uid = user.getUid();

    // Query db to get posts of this user.
    List<Post> posts = this.postDAO.getPosts(uid);

    Map<String, Object> result = new HashMap<String, Object>();
    result.put("posts", Post.toJSONOArray(posts));
    JsonUtils.WriteJSONResponse(response, result);
  }

  @RequestMapping(value="", method=RequestMethod.POST)
  public void doAddPost(
      HttpServletRequest request,
      HttpServletResponse response,
      HttpSession session,
      @RequestHeader("Content-Type") String contentType,
      @PathVariable("username") String username)
      throws IOException, ServletException {
    if (contentType == null ||
        !contentType.split(";")[0].equals("multipart/form-data")) {
      JsonUtils.WriteJSONResponse(response, false, "Invalid content type");
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }
    String uid = user.getUid();

    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");

    // Process post data.
    String content = null;
    List<String> pictures = new ArrayList<String>();

    Collection<Part> parts = request.getParts();
    for (Part part : parts) {
      String partName = part.getName();
      if (partName.equals("content")){
        content = getPostContent(part);
      } else if (partName.startsWith("image-")) {
        String pic = getPostPicture(uid, part, baseDir);
        if (pic != null) {
          pictures.add(pic);
        }
      }
    }

    // Add new post to database.
    Post post = new Post();
    post.setContent(content);
    post.setPicturesFromList(pictures);

    Map<String, Object> result = new HashMap<String, Object>();
    String pid = this.postDAO.addNewPost(uid, post);
    if (pid != null) {
      result.put("post", post.toJSONObject());
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "server data update error");
    }
  }

  private String getPostContent(Part part) {
    try {
      BufferedReader br =
          new BufferedReader(new InputStreamReader(part.getInputStream()));

      StringBuilder strBuilder = new StringBuilder();
      char[] buffer = new char[4096];
      int totalLen = 0;
      int readlen = 0;
      while((readlen = br.read(buffer)) > 0) {
        totalLen += readlen;
        strBuilder.append(buffer, 0, readlen);
      }
      return strBuilder.toString();
    } catch(IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private String getPostPicture(String uid, Part part, String baseDir) {
    // image type.
    String type = part.getHeader("Content-Type");
    String[] re = type.split("/");
    if (re.length != 2) {
      return null;
    }
    String ext = re[1];

    // image file name.
    String filename = UuidUtils.compressedUuid() + "." + ext;

    FileOutputStream fos = null;
    try {
      InputStream is = part.getInputStream();

      String imageFile = Paths.get(
          baseDir, "user", uid, "postimages", filename).toString();
      fos = new FileOutputStream(imageFile);
      byte[] buffer = new byte[4096];
      int readlen = 0;
      while((readlen = is.read(buffer)) > 0) {
        fos.write(buffer, 0, readlen);
      }
      return filename;
    } catch(IOException e) {
      e.printStackTrace();
      return null;
    } finally{
      try {
        fos.close();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  @RequestMapping(value="/{pid}", method=RequestMethod.DELETE)
  public void doDeletePost(
      HttpServletRequest request,
      HttpServletResponse response,
      HttpSession session,
      @PathVariable("username") String username,
      @PathVariable("pid") String pid)
      throws IOException, ServletException {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }
    String uid = user.getUid();

    Post post = this.postDAO.deletePost(uid, pid);
    if (post != null) {
      // Delete images.
      List<String> pictures = post.getPicturesAsList();
      for (String pic : pictures) {
        String imageFile = Paths.get(
            getDataDir(request), "user", uid, "postimages", pic).toString();
        File file = new File(imageFile);
        if (file.delete()) {
          log.error("Delete image file " + imageFile + " failed");
        }
      }
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to delete post");
    }
  }
}
