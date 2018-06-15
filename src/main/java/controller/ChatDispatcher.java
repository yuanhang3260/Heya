package controller;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.websocket.Session;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import application.SpringUtils;
import bean.User;
import bean.ChatMessage;
import dao.ChatDAO;
import dao.FriendsDAO;
import util.JsonUtils;
import util.SingleThreadExecutorGroup;

@Controller("ChatDispatcher")
public class ChatDispatcher {
  private static final Logger log = Logger.getLogger(ChatDispatcher.class);

  @Autowired
  @Qualifier("ChatDAO")
  ChatDAO chatDao;

  @Autowired
  @Qualifier("FriendsDAO")
  FriendsDAO friendsDao;

  private Map<String, ChatUser> chatUsers = new HashMap<String, ChatUser>();
  private SingleThreadExecutorGroup executorGroup = new SingleThreadExecutorGroup(4);

  // ChatUser manages sessions and status of a user. All IO and related DB operations are executed
  // in its internal single-threaded executor to avoid race conditions on this user.
  private class ChatUser {
    private Set<Session> sessions = new HashSet<Session>();
    private ExecutorService executor;

    public ChatUser() {
      this.executor = executorGroup.getExecutor();
    }

    synchronized public void addSession(Session s) {
      this.sessions.add(s);
    }

    synchronized public void removeSession(Session s) {
      this.sessions.remove(s);
    }

    public void pushMessage(JSONObject obj) {
      try {
        for (Session session : this.sessions) {
          session.getBasicRemote().sendText(obj.toString());
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void queueTask(Runnable task) {
      this.executor.submit(task);
    }

    synchronized public boolean isOnline() {
      return !this.sessions.isEmpty();
    }

    synchronized public int sessionsNum() {
      return this.sessions.size();
    }
  }

  synchronized private ChatUser getOrCreateChatUser(String username) {
    ChatUser user = chatUsers.get(username);
    if (user == null) {
      user = new ChatUser();
      chatUsers.put(username, user);
    }
    return user;
  }

  synchronized private ChatUser getChatUser(String username) {
    return chatUsers.get(username);
  }

  private boolean isUserOnline(String username) {
    ChatUser user;
    synchronized(this) {
      user = chatUsers.get(username);
      if (user == null) {
        return false;
      }
    }
    return user.isOnline();
  }

  public boolean processMessage(String username, String msgStr, Session session) {
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
  private boolean processNewChatMessage(String username, JSONObject msg) throws JSONException {
    String from = msg.getString("from");
    if (!from.equals(username)) {
      log.error("Unauthorized NewMessage labeled from = " + from +
                ", the actual user is " + username);
      return false;
    }

    String to = msg.getString("to");
    String content = msg.getString("content");
    Long timestamp = msg.getLong("timestamp");

    // Write the new message to DB and send it to receiver. Note this is done in the receiver's
    // thread executor.
    ChatUser receiver = getOrCreateChatUser(to);
    receiver.queueTask(() -> {
      // Add the new message to DB.
      if (!chatDao.addChatMessage(from, to, content, timestamp)) {
        log.error("Failed to add new ChatMessage to DB");
        return;
      }

      // Send the new message to the receiver (aka "to").
      receiver.pushMessage(msg);
    });

    // Also broadcast to all connections of sender itself (aka "from") to sync on all devices.
    ChatUser sender = getChatUser(from);
    if (sender == null) {
      log.error("ChatUser " + from + " does not exist");
      return false;
    }
    sender.queueTask(() -> {
      sender.pushMessage(msg);
    });
    return true;
  }

  // A "DialogRead" message must contain following fields:
  //
  // - from
  // - to
  // - timestamp
  private boolean processMessageRead(String username, JSONObject msg) throws JSONException {
    String from = msg.getString("from");
    String to = msg.getString("to");
    if (!to.equals(username)) {
      log.error("Unauthorized DialogRead labeled to = " + to +
                ", the actual user is " + username);
      return false;
    }
    Long timestamp = msg.getLong("timestamp");

    // Broadcast this message to all connections of the user "to".
    //
    // TODO: Should we also broadcast this to the user "from"? Probably not, at least we should
    // allow user "to" to configure whether they want the user "from" to see this.
    ChatUser receiver = getChatUser(to);
    if (receiver == null) {
      log.error("ChatUser " + to + " does not exist");
      return false;
    }

    receiver.queueTask(() -> {
      // Update dialog in DB.
      if (!chatDao.updateReadTime(from, to, timestamp)) {
        return;
      }
      // Sync.
      receiver.pushMessage(msg);
    });

    return true;
  }

  public void newSessionOpen(String username, Session session) {
    ChatUser user = getOrCreateChatUser(username);

    // Get friends list and return their online status and unread messages.
    user.queueTask(() -> {
      // Add this new session.
      user.addSession(session);

      // Get friends status and unread messages.
      List<User> friends = friendsDao.getFriends(username);
      JSONObject response = new JSONObject();
      try {
        JSONArray array = new JSONArray();
        for (User friend : friends) {
          JSONObject obj = new JSONObject();
          obj.put("username", friend.getUsername());
          obj.put("online", isUserOnline(friend.getUsername()));

          // Unread messages from this friend.
          List<ChatMessage> messages = chatDao.getUnreadMessages(friend.getUsername(), username);
          JSONArray unread = new JSONArray();
          for (ChatMessage message : messages) {
            unread.put(message.toJSONObject());
          }
          obj.put("unread", unread);

          array.put(obj);
        }

        response.put("type", "ChatFriends");
        response.put("friends", array);
      } catch (JSONException e) {
        e.printStackTrace();
        return;
      }

      // Return message to user, only on this session.
      try {
        session.getBasicRemote().sendText(response.toString());
      } catch (IOException e) {
        e.printStackTrace();
      }

      // If this session if first session of this user, we should broadcast online to all friends.
      if (user.sessionsNum() == 1) {
        for (User friend : friends) {
          ChatUser chatFriend = getChatUser(friend.getUsername());
          if (chatFriend != null) {
            JSONObject msg = new JSONObject();
            try {
              msg.put("type", "FriendOnline");
              msg.put("username", username);
            } catch (JSONException e) {
              e.printStackTrace();
              continue;
            }
            chatFriend.queueTask(() -> {
              chatFriend.pushMessage(msg);
            });
          }
        }
      }
    });
  }

  public void sessionClosed(String username, Session session) {
    ChatUser user = getChatUser(username);
    if (user == null) {
      log.error("ChatUser " + username + " does not exist, skip removing session");
      return;
    }

    user.queueTask(() -> {
      user.removeSession(session);

      // If this is the last session, remove this ChatUser and notify all friends offline message.
      if (!user.isOnline()) {
        List<User> friends = friendsDao.getFriends(username);
        for (User friend : friends) {
          ChatUser chatFriend = getChatUser(friend.getUsername());
          if (chatFriend != null) {
            JSONObject msg = new JSONObject();
            try {
              msg.put("type", "FriendOffline");
              msg.put("username", username);
            } catch (JSONException e) {
              e.printStackTrace();
              continue;
            }
            chatFriend.queueTask(() -> {
              chatFriend.pushMessage(msg);
            });
          }
        }
      }
    });
  }
}
