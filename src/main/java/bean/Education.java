package bean;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Education {
  private String sid;
  private String school;

  // Constructors.
  public Education() {}
  public Education(Education other) {
    this.sid = other.sid;
    this.school = other.school;
  }

  public String getSid() {
    return this.sid;
  }
  public void setSid(String sid) {
    this.sid = sid;
  }

  public String getSchool() {
    return this.school;
  }
  public void setSchool(String school) {
    this.school = school;
  }
}
