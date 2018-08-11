package controller;

import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bean.User;
import util.JsonUtils;

@Controller
public class Logout {
  @RequestMapping(value="/logout", method=RequestMethod.POST)
  public void LoginHandler(HttpServletRequest request,
                           HttpServletResponse response) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
    JsonUtils.WriteJSONResponse(response, true, null);
  }
}
