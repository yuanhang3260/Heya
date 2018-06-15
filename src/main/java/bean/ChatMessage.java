package bean;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class ChatMessage {
  private String dialogId;
  private String messageId;
  private String username;
  private String content;
  private Long createTime;

  // Constructors.
  public ChatMessage() {}
  public ChatMessage(ChatMessage other) {
    this.dialogId = other.dialogId;
    this.messageId = other.messageId;
    this.username = other.username;
    this.content = other.content;
    this.createTime = other.createTime;
  }

  public JSONObject toJSONObject() {
    JSONObject json_obj = new JSONObject();
    try {
      json_obj.put("from", this.username);
      json_obj.put("content", this.content);
      json_obj.put("timestamp", this.createTime);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  public static JSONArray toJSONOArray(List<ChatMessage> messages) {
    JSONArray array = new JSONArray();
    for (ChatMessage message : messages) {
      array.put(message.toJSONObject());
    }
    return array;
  }

  public String getDialogId() {
    return this.dialogId;
  }
  public void setDialogId(String dialogId) {
    this.dialogId = dialogId;
  }

  public String getMessageId() {
    return this.messageId;
  }
  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  public String getUsername() {
    return this.username;
  }
  public void setUsername(String username) {
    this.username = username;
  }

  public String getContent() {
    return this.content;
  }
  public void setContent(String content) {
    this.content = content;
  }

  public Long getCreateTime() {
    return this.createTime;
  }
  public void setCreateTime(Long createTime) {
    this.createTime = createTime;
  }
}
