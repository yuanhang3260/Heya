package controller;

import javax.servlet.*;
import javax.servlet.http.*;
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
public class Login {

  @Autowired
  UserDAO userDAO;

  @RequestMapping(value="/login", method=RequestMethod.POST)
  public void LoginHandler(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParam("username") String username,
                           @RequestParam("password") String password) {
    boolean success = false;
    String err = null;

    User user = this.userDAO.GetUserByUsername(username);
    if (user == null) {
      err = "user cannot be found";
    } else {
      if (!user.getPassword().equals(password)) {
        err = "user/password mismatch";
      } else {
        // Login successfully, create session and return.
        HttpSession session = request.getSession(false);
        if (session != null) {
          // Remove current session.
          session.invalidate();
        }
        session = request.getSession();
        session.setAttribute("user", user);
        success = true;
      }
    }

    JsonUtils.WriteJSONResponse(response, success, err);
  }
}