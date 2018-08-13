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

import bean.FriendRequest;
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
        obj.put("avatar", ProfileImage.profileImageURL(friend.getUsername()));
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
    List<User> friends = this.friendsDAO.getFriends(username);
    JSONObject result = new JSONObject();
    result.put("friendscount", friends.size());

    if (!username.equals(viewerUsername)) {
      boolean areFriends = false;
      for (User friend : friends) {
        if (friend.getUsername().equals(viewerUsername)) {
          areFriends = true;
          result.put("friendRelationship", "FRIENDS");
          break;
        }
      }

      if (!areFriends && friendsDAO.friendRequestIsSent(viewerUsername, username)) {
        result.put("friendRelationship", "FRIEND_REQUEST_SENT");
      }
    }
    JsonUtils.WriteJSONResponse(response, result);
  }

  @RequestMapping(value="notifications", method=RequestMethod.GET)
  public void getFriendNotifications(HttpServletResponse response,
                                     HttpSession session,
                                     @PathVariable("username") String username) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    List<List<FriendRequest>> re = this.friendsDAO.getFriendNotifications(username);
    JSONObject result = new JSONObject();

    // Friend requests.
    JSONArray array = new JSONArray();
    for (FriendRequest request : re.get(0)) {
      JSONObject obj = new JSONObject();
      obj.put("friendUsername", request.getUsername1());
      array.put(obj);
    }
    result.put("friendRequests", array);

    // Request replies.
    array = new JSONArray();
    for (FriendRequest request : re.get(1)) {
      JSONObject obj = new JSONObject();
      obj.put("friendUsername", request.getUsername2());
      obj.put("status", request.getStatus());
      obj.put("lastupdate", request.getLastUpdate());
      array.put(obj);
    }
    for (FriendRequest request : re.get(2)) {
      JSONObject obj = new JSONObject();
      obj.put("friendUsername", request.getUsername1());
      obj.put("lastupdate", request.getLastUpdate());
      array.put(obj);
    }
    result.put("requestReplies", array);

    JsonUtils.WriteJSONResponse(response, result);
  }

  @RequestMapping(value="addfriend", method=RequestMethod.POST)
  public void addfriend(HttpServletResponse response,
                        HttpSession session,
                        @PathVariable("username") String username,
                        @RequestParam("friendUsername") String friendUsername) {
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

  @RequestMapping(value="acceptfriend", method=RequestMethod.POST)
  public void acceptFriend(HttpServletResponse response,
                           HttpSession session,
                           @PathVariable("username") String username,
                           @RequestParam("friendUsername") String friendUsername) {
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

    // Note that friend must be put before owner user since this is the order in FriendRequest.
    if (friendsDAO.acceptFriend(friend.getUid(), friendUsername, user.getUid(), username)) {
      JsonUtils.WriteJSONResponse(response, new JSONObject());
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to accept add friend");
    }
  }

  @RequestMapping(value="ignorerequest", method=RequestMethod.POST)
  public void ignoreFriendRequest(HttpServletResponse response,
                                  HttpSession session,
                                  @PathVariable("username") String username,
                                  @RequestParam("friendUsername") String friendUsername) {
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

    if (friendsDAO.ignoreFriendRequest(friendUsername, username)) {
      JsonUtils.WriteJSONResponse(response, new JSONObject());
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to accept add friend");
    }
  }

  @RequestMapping(value="readnotifications", method=RequestMethod.POST)
  public void readNotifications(HttpServletResponse response,
                                HttpSession session,
                                @PathVariable("username") String username,
                                @RequestParam("maxTimeStamp") Long maxTimeStamp) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (friendsDAO.readNotifications(username, maxTimeStamp)) {
      JsonUtils.WriteJSONResponse(response, new JSONObject());
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to read notifications");
    }
  }

  @RequestMapping(value="unfriend", method=RequestMethod.POST)
  public void unfriend(HttpServletResponse response,
                        HttpSession session,
                        @PathVariable("username") String username,
                        @RequestParam("friendUsername") String friendUsername) {
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
