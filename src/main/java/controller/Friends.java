package controller;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.GregorianCalendar;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bean.User;
import dao.FriendsDAO;
import util.JsonUtils;

@Controller
@RequestMapping("/friends/{uid}")
public class Friends {

  @Autowired
  FriendsDAO friendsDAO;

  private User getAuthorizedUser(HttpSession session, String uid) {
    User user = (User)session.getAttribute("user");
    if (user != null && uid != null && uid.equals(user.getUid())) {
      return user;
    }
    return null;
  }

  @RequestMapping(value="", method=RequestMethod.GET)
  public void getFriends(HttpServletResponse response,
                         HttpSession session,
                         @PathVariable("uid") String uid) {
    User user = getAuthorizedUser(session, uid);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    List<User> friends = this.friendsDAO.getFriends(uid);
    JSONObject result = new JSONObject();
    try {
      JSONArray array = new JSONArray();
      for (User friend : friends) {
        JSONObject obj = friend.toJSONObject();
        obj.put("avatar", ProfileImage.profileImageURL(friend.getUid()));
        array.put(obj);
      }
      result.put("friends", array);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonUtils.WriteJSONResponse(response, result);
  }
}
