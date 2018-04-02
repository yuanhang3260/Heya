package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bean.User;
import util.JsonUtils;

@Controller
public class Signup {
  private static final String CREATE_USER_ERROR =
      "Server error, failed to create new user";

  @RequestMapping(value="/signup/new", method=RequestMethod.POST)
  public void SignupHandler(HttpServletRequest request,
                            HttpServletResponse response,
                            User user) {
    boolean success = false;
    String err = null;

    if (User.GetUserByUsername(user.getUsername()) != null) {
      err = "Username already used";
      JsonUtils.WriteJSONResponse(response, success, err);
      return;
    }

    int uid = User.getNextUid();
    if (uid < 0) {
      err = CREATE_USER_ERROR;
      JsonUtils.WriteJSONResponse(response, success, err);
      return;
    }

    // TODO: Encrypt password!
    // TODO: Rollback on fail?
    if (User.addNewUser(uid, user.getUsername(),
                        user.getEmail(), user.getPassword()) &&
        createUserDataDirectory(request, uid)) {
      // Create new user successfully. Create session and return.
      request.getSession().setAttribute("user", user);
      success = true;
    } else {
      err = CREATE_USER_ERROR;
    }

    JsonUtils.WriteJSONResponse(response, success, err);
    return;
  }

  private boolean createUserDataDirectory(HttpServletRequest request, int uid) {
    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");
    File theDir = new File(baseDir + "user/" + Integer.toString(uid));
    if (!theDir.exists()) {
      try{
        if (!theDir.mkdirs()) {
          return false;
        }
      }
      catch(SecurityException e){
        e.printStackTrace();
        return false;
      }
    }
    return true;
  }
}
