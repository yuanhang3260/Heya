package controller;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.websocket.Session;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import application.SpringUtils;
import bean.User;
import dao.ChatDAO;
import dao.FriendsDAO;
import util.JsonUtils;

@Controller("ChatDispatcher")
public class ChatDispatcher {
  private static final Logger log = Logger.getLogger(ChatDispatcher.class);

  @Autowired
  ChatDAO chatDao;

  @Autowired
  FriendsDAO friendsDao;

  private Map<String, ChatUser> chatUsers = new HashMap<String, ChatUser>();

  private class ChatUser {
    public Set<Session> sessions = new HashSet<Session>();

    public void pushMessage(JSONObject msg) throws IOException {
      for (Session session : this.sessions) {
        session.getBasicRemote().sendText(msg.toString());
      }
    }
  }

  public void addNewSession(String username, Session session) {
    ChatUser user = chatUsers.get(username);
    if (user == null) {
      user = new ChatUser();
      chatUsers.put(username, user);
    }
    user.sessions.add(session);
  }

  public void removeSession(String username, Session session) {
    ChatUser user = chatUsers.get(username);
    if (user == null) {
      log.error("ChatUser " + username + " does not exist, skip removing session");
      return;
    }
    user.sessions.remove(session);
  }

  public boolean processMessage(String username, String msgStr) {
    try {
      JSONObject msg = new JSONObject(msgStr);
      if (!msg.has("type")) {
        log.error("No message type specified.");
        return false;
      }

      // Remove the "client" label in the message so that when this message is broadcasted,
      // receiver knows it is from server. This is to fix the onmessage double-trigger problem
      // in javascript websocket. 
      msg.remove("client");

      String type = msg.getString("type");
      if (type.equals("NewMessage")) {
        return processNewChatMessage(username, msg);
      } else if (type.equals("DialogRead")) {
        return processMessageRead(username, msg);
      } else {
        log.error("Unknown message type: " + type);
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  // A NewMessage message must contain following fields:
  //
  // - from
  // - to
  // - content
  // - timestamp
  private boolean processNewChatMessage(String username, JSONObject msg)
      throws JSONException, IOException {
    String from = msg.getString("from");
    if (!from.equals(username)) {
      log.error("Unauthorized NewMessage labeled from = " + from +
                ", the actual user is " + username);
      return false;
    }

    String to = msg.getString("to");
    String content = msg.getString("content");
    Date timestamp = new Date();
    timestamp.setTime(msg.getLong("timestamp"));

    // Add the new message to DB.
    if (!this.chatDao.AddChatMessage(from, to, content, timestamp)) {
      log.error("Failed to add new ChatMessage to DB");
      return false;
    }

    // Broadcast the new message to the receiver (aka "to"), and also to all connections of sender
    // itself (aka "from") to sync on all devices.
    ChatUser receiver = chatUsers.get(to);
    if (receiver != null) {
      receiver.pushMessage(msg);
    }

    ChatUser sender = chatUsers.get(from);
    if (sender != null) {
      sender.pushMessage(msg);
    }
    return true;
  }

  // A "DialogRead" message must contain following fields:
  //
  // - from
  // - to
  // - timestamp
  private boolean processMessageRead(String username, JSONObject msg)
      throws JSONException, IOException {
    String from = msg.getString("from");
    String to = msg.getString("to");
    if (!to.equals(username)) {
      log.error("Unauthorized DialogRead labeled to = " + to +
                ", the actual user is " + username);
      return false;
    }

    Date timestamp = new Date();
    timestamp.setTime(msg.getLong("timestamp"));

    // Update dialog in DB.
    if (!this.chatDao.updateReadTime(from, to, timestamp)) {
      log.error("Failed to update dialog read time");
      return false;
    }

    // Broadcast this message to all connections of the user "to".
    //
    // TODO: Should we also broadcast this to the user "from"? Probably not, at least we should
    // allow user "to" to configure whether they want the user "from" to see this.
    ChatUser receiver = chatUsers.get(to);
    if (receiver != null) {
      receiver.pushMessage(msg);
    }

    return true;
  }
}
