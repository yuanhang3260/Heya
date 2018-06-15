package bean;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.ChatMessage;

public class ChatDialog {
  private String dialogId;
  private String username1;
  private String username2;
  private Long user1ReadTime;
  private Long user2ReadTime;
  private Set<ChatMessage> messages;

  // Constructors.
  public ChatDialog() {}
  public ChatDialog(ChatDialog other) {
    this.dialogId = other.dialogId;
    this.username1 = other.username1;
    this.username2 = other.username2;
    this.user1ReadTime = other.user1ReadTime;
    this.user2ReadTime = other.user2ReadTime;
    this.messages = other.messages;
  }

  public String getDialogId() {
    return this.dialogId;
  }
  public void setDialogId(String dialogId) {
    this.dialogId = dialogId;
  }

  public String getUsername1() {
    return this.username1;
  }
  public void setUsername1(String username1) {
    this.username1 = username1;
  }

  public String getUsername2() {
    return this.username2;
  }
  public void setUsername2(String username2) {
    this.username2 = username2;
  }

  public Long getUser1ReadTime() {
    return this.user1ReadTime;
  }
  public void setUser1ReadTime(Long time) {
    this.user1ReadTime = time;
  }

  public Long getUser2ReadTime() {
    return this.user2ReadTime;
  }
  public void setUser2ReadTime(Long time) {
    this.user2ReadTime = time;
  }

  public Set<ChatMessage> getMessages() {
    if (this.messages == null) {
      this.messages = new HashSet<ChatMessage>();
    }
    return this.messages;
  }

  public void setMessages(Set<ChatMessage> messages) {
    this.messages = messages;
  }
}
