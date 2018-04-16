package bean;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.User;
import bean.Education;

public class UserEducation {
  private String ueid;
  private User user;
  private Education education;

  private String major;
  private Integer startYear;
  private Integer endYear;

  public UserEducation() {}
  public UserEducation(UserEducation other) {
    this.ueid = other.ueid;
    this.user = other.user;
    this.education = other.education;
    this.major = other.major;
    this.startYear = other.startYear;
    this.endYear = other.endYear;
  }

  public void mergeFrom(UserEducation other) {
    if (other.major != null) {
      this.major = other.major;
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
      json_obj.put("sid", this.education.getSid());
      json_obj.put("school", this.education.getSchool());
      if (this.major != null) {
        json_obj.put("major", this.major);
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

  // Given a list of UserEducation object, convert to JSON array.
  static public JSONArray toJSONArray(Set<UserEducation> ues) {
    JSONArray array = new JSONArray();
    for (UserEducation ue : ues) {
      array.put(ue.toJSONObject());
    }
    return array;
  }

  public String getUeid() {
    return this.ueid;
  }
  public void setUeid(String ueid) {
    this.ueid = ueid;
  }

  public User getUser() {
    return this.user;
  }
  public void setUser(User user) {
    this.user = user;
  }

  public Education getEducation() {
    return this.education;
  }
  public void setEducation(Education education) {
    this.education = education;
  }

  public String getMajor() {
    return this.major;
  }
  public void setMajor(String major) {
    this.major = major;
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
