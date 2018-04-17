package bean;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.User;
import bean.Work;

public class UserWork {
  private String uwid;
  private User user;
  private Work work;

  private String position;
  private Integer startYear;
  private Integer endYear;

  public UserWork() {}
  public UserWork(UserWork other) {
    this.uwid = other.uwid;
    this.user = other.user;
    this.work = other.work;
    this.position = other.position;
    this.startYear = other.startYear;
    this.endYear = other.endYear;
  }

  public void mergeFrom(UserWork other) {
    if (other.position != null) {
      this.position = other.position;
    }
    if (other.startYear != null) {
      this.startYear = other.startYear;
    }
    if (other.endYear != null) {
      this.endYear = other.endYear;
    }
  }

  // Convert to JSON.
  public JSONObject toJSONObject() {
    JSONObject json_obj = new JSONObject();
    try {
      json_obj.put("uid", this.user.getUid());
      json_obj.put("cid", this.work.getCid());
      json_obj.put("company", this.work.getCompany());
      if (this.position != null) {
        json_obj.put("position", this.position);
      }

      if (this.startYear != null || this.endYear != null) {
        JSONObject year_obj = new JSONObject();
        if (this.startYear != null) {
          year_obj.put("start", this.startYear);
        }
        if (this.endYear != null) {
          year_obj.put("end", this.endYear);
        }
        json_obj.put("year", year_obj);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  // Given a list of UserWork object, convert to JSON array.
  static public JSONArray toJSONArray(Set<UserWork> ues) {
    JSONArray array = new JSONArray();
    for (UserWork ue : ues) {
      array.put(ue.toJSONObject());
    }
    return array;
  }

  public String getUwid() {
    return this.uwid;
  }
  public void setUwid(String uwid) {
    this.uwid = uwid;
  }

  public User getUser() {
    return this.user;
  }
  public void setUser(User user) {
    this.user = user;
  }

  public Work getWork() {
    return this.work;
  }
  public void setWork(Work work) {
    this.work = work;
  }

  public String getPosition() {
    return this.position;
  }
  public void setPosition(String position) {
    this.position = position;
  }

  public Integer getStartYear() {
    return this.startYear;
  }
  public void setStartYear(Integer year) {
    this.startYear = year;
  }

  public Integer getEndYear() {
    return this.endYear;
  }
  public void setEndYear(Integer year) {
    this.endYear = year;
  }
}
