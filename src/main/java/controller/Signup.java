package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import application.SpringUtils;
import bean.User;
import dao.UserDAO;
import util.JsonUtils;

@Controller
public class Signup {
  private static final String CREATE_USER_ERROR =
      "Server error, failed to create new user";

  @Autowired
  UserDAO userDAO;

  @RequestMapping(value="/signup/new", method=RequestMethod.POST)
  public void SignupHandler(HttpServletRequest request,
                            HttpServletResponse response,
                            User user) {
    boolean success = false;
    String err = null;

    if (this.userDAO.GetUserByUsername(user.getUsername()) != null) {
      err = "Username already used";
      JsonUtils.WriteJSONResponse(response, success, err);
      return;
    }

    // TODO: Encrypt password!
    String uid = this.userDAO.addNewUser(user.getUsername(),
                                         user.getEmail(), user.getPassword());
    System.out.println("UID = " + uid);
    if (uid != null) {
      createUserDataDirectory(request, uid);
      request.getSession().setAttribute("user", user);
      success = true;
    } else {
      // TODO: Rollback the user directory on fail?
      err = CREATE_USER_ERROR;
    }

    JsonUtils.WriteJSONResponse(response, success, err);
    return;
  }

  private boolean createUserDataDirectory(HttpServletRequest request,
                                          String uid) {
    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");
    File theDir = new File(baseDir + "user/" + uid);
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
