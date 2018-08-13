package bean;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.ChatMessage;

public class FriendRequest {
  private String username1;
  private String username2;
  private String status;
  private Long lastUpdate;

  // Constructors.
  public FriendRequest() {}
  public FriendRequest(FriendRequest other) {
    this.username1 = other.username1;
    this.username2 = other.username2;
    this.lastUpdate = other.lastUpdate;
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

  public String getStatus() {
    return this.status;
  }
  public void setStatus(String status) {
    this.status = status;
  }

  public Long getLastUpdate() {
    return this.lastUpdate;
  }
  public void setLastUpdate(Long lastUpdate) {
    this.lastUpdate = lastUpdate;
  }
}
