package controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.List;
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
public class Search {

  @Autowired
  UserDAO userDAO;

  @RequestMapping(value="/search", method=RequestMethod.GET)
  public String searchUser(HttpServletRequest request,
                           HttpServletResponse response,
                           @RequestParam(value="q") String keyword) {
    List<User> users = this.userDAO.searchUser(keyword);
    request.setAttribute("matchedUsers", users);
    return "search";
  }
}
