package controller;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.GregorianCalendar;
import javax.servlet.*;
import javax.servlet.http.*;
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

import bean.User;
import dao.PostDAO;
import util.JsonUtils;

@Controller
@RequestMapping("/post/{uid}")
public class PostImage {
  private static String dataDir = null;

  @Autowired
  PostDAO postDAO;

  @RequestMapping(value="/{pid}/image/{filename:.+}", method=RequestMethod.GET)
  public void doAddPost(
      HttpServletRequest request,
      HttpServletResponse response,
      HttpSession session,
      @PathVariable("uid") String uid,
      @PathVariable("filename") String filename)
      throws IOException, ServletException {
    if (uid == null || filename == null) {
      JsonUtils.WriteJSONResponse(response, false, "Invalid request");
      return;
    }

    if (PostImage.dataDir == null) {
      ServletContext context = request.getServletContext();
      PostImage.dataDir = context.getInitParameter("data-storage");
    }

    String path = Paths.get("user/", uid, "/postimages/" + filename).toString();
    File file = new File(Paths.get(dataDir, path).toString());
    if (file.exists()) {
      String imageURL = Paths.get("/data", path).toString();
        // Forward to FileSevlet for /data/* requests.
        request.getRequestDispatcher(imageURL).forward(request, response);
    } else {
      System.out.println("image " + Paths.get(dataDir, path).toString());
      response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
  }
}
