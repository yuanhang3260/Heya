package bean;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.User;
import bean.Place;

public class UserPlace {
  private String upid;
  private User user;
  private Place place;

  private boolean current;
  private boolean hometown;

  public UserPlace() {}
  public UserPlace(UserPlace other) {
    this.upid = other.upid;
    this.user = other.user;
    this.place = other.place;
    this.current = other.current;
    this.hometown = other.hometown;
  }

  public void mergeFrom(UserPlace other) {
    this.current = other.current;
    this.hometown = other.hometown;
  }

  // Convert to JSON.
  public JSONObject toJSONObject() {
    JSONObject json_obj = new JSONObject();
    try {
      json_obj.put("uid", this.user.getUid());
      json_obj.put("pid", this.place.getPid());
      json_obj.put("name", this.place.getName());
      json_obj.put("current", this.current);
      json_obj.put("hometown", this.hometown);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  // Given a list of UserPlace object, convert to JSON array.
  static public JSONArray toJSONArray(Set<UserPlace> ues) {
    JSONArray array = new JSONArray();
    for (UserPlace ue : ues) {
      array.put(ue.toJSONObject());
    }
    return array;
  }

  public String getUpid() {
    return this.upid;
  }
  public void setUpid(String upid) {
    this.upid = upid;
  }

  public User getUser() {
    return this.user;
  }
  public void setUser(User user) {
    this.user = user;
  }

  public Place getPlace() {
    return this.place;
  }
  public void setPlace(Place place) {
    this.place = place;
  }

  public boolean getCurrent() {
    return this.current;
  }
  public void setCurrent(boolean current) {
    this.current = current;
  }

  public boolean getHometown() {
    return this.hometown;
  }
  public void setHometown(boolean hometown) {
    this.hometown = hometown;
  }
}
