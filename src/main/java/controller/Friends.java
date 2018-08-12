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
import dao.UserDAO;
import dao.FriendsDAO;
import util.JsonUtils;

@Controller
@RequestMapping("/friends/{username}")
public class Friends {

  @Autowired
  FriendsDAO friendsDAO;

  @Autowired
  UserDAO userDAO;

  private User getAuthorizedUser(HttpSession session, String username) {
    User user = (User)session.getAttribute("user");
    if (user != null && username != null && username.equals(user.getUsername())) {
      return user;
    }
    return null;
  }

  @RequestMapping(value="", method=RequestMethod.GET)
  public void getFriends(HttpServletResponse response,
                         HttpSession session,
                         @PathVariable("username") String username) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    List<User> friends = this.friendsDAO.getFriends(username);
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

  @RequestMapping(value="friendscount", method=RequestMethod.GET)
  public void getFriendsCount(HttpServletResponse response,
                              HttpSession session,
                              @PathVariable("username") String username,
                              @RequestParam("viewerUsername") String viewerUsername) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    List<User> friends = this.friendsDAO.getFriends(username);
    JSONObject result = new JSONObject();
    result.put("friendscount", friends.size());

    if (!username.equals(viewerUsername)) {
      for (User friend : friends) {
        if (friend.getUsername().equals(viewerUsername)) {
          result.put("areFriends", true);
          break;
        }
      }
    }
    JsonUtils.WriteJSONResponse(response, result);
  }

  @RequestMapping(value="addfriend/{friendUsername}", method=RequestMethod.POST)
  public void addfriend(HttpServletResponse response,
                        HttpSession session,
                        @PathVariable("username") String username,
                        @PathVariable("friendUsername") String friendUsername) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    User friend = userDAO.getUserByUsername(friendUsername);
    if (friend == null) {
      JsonUtils.WriteJSONResponse(response, false, "No user " + friendUsername);
      return;
    }

    if (friendsDAO.requestAddFriend(username, friendUsername)) {
      JsonUtils.WriteJSONResponse(response, new JSONObject());
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to sent friend request");
    }
  }

  @RequestMapping(value="acceptfriend/{friendUsername}", method=RequestMethod.POST)
  public void acceptFriend(HttpServletResponse response,
                           HttpSession session,
                           @PathVariable("username") String username,
                           @PathVariable("friendUsername") String friendUsername) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    User friend = userDAO.getUserByUsername(friendUsername);
    if (friend == null) {
      JsonUtils.WriteJSONResponse(response, false, "No user " + friendUsername);
      return;
    }

    if (friendsDAO.acceptFriend(user.getUid(), username, friend.getUid(), friendUsername)) {
      JsonUtils.WriteJSONResponse(response, new JSONObject());
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to accept add friend");
    }
  }

  @RequestMapping(value="unfriend/{friendUsername}", method=RequestMethod.POST)
  public void unfriend(HttpServletResponse response,
                        HttpSession session,
                        @PathVariable("username") String username,
                        @PathVariable("friendUsername") String friendUsername) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (friendsDAO.unFriend(username, friendUsername)) {
      JsonUtils.WriteJSONResponse(response, new JSONObject());
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to unfriend");
    }
  }
}
