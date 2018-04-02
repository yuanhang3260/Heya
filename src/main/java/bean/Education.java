package bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Education {
  private int sid;  // internal ID just for this user.
  private String school;
  private String major;
  private Integer startYear;
  private Integer endYear;

  // Constructors.
  public Education() {}
  public Education(Education other) {
    this.sid = other.sid;
    this.school = other.school;
    this.major = other.major;
    this.startYear = other.startYear;
    this.endYear = other.endYear;
  }

  // Builder pattern.
  public static class Builder {
    private Education obj;

    public Builder() {}

    private Education obj() {
      if (this.obj == null) {
        this.obj = new Education();
      }
      return this.obj;
    }

    public Builder setSid(int sid) {
      obj().setSid(sid);
      return this;
    }
    public Builder setSchool(String school) {
      obj().setSchool(school);
      return this;
    }
    public Builder setMajor(String major) {
      obj().setMajor(major);
      return this;
    }
    public Builder setStartYear(Integer year) {
      obj().setStartYear(year);
      return this;
    }
    public Builder setEndYear(Integer year) {
      obj().setEndYear(year);
      return this;
    }

    public Education buildFromJSON(JSONObject json_obj) {
      this.obj = new Education();
      try {
        if (!json_obj.isNull("sid")) {
          this.obj.setSid(json_obj.getInt("sid"));
        }
        if (!json_obj.isNull("school")) {
          this.obj.setSchool(json_obj.getString("school"));
        }
        if (!json_obj.isNull("major")) {
          this.obj.setMajor(json_obj.getString("major"));
        }
        if (!json_obj.isNull("year")) {
          JSONObject year_obj = (JSONObject)json_obj.get("year");
          if (!year_obj.isNull("start")) {
            this.obj.setStartYear(year_obj.getInt("start"));
          }
          if (!year_obj.isNull("end")) {
            this.obj.setEndYear(year_obj.getInt("end"));
          }
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return null;
      }
      Education ret = this.obj;
      this.obj = null;
      return ret;
    }

    Education build() {
      Education ret = obj();
      this.obj = null;
      return ret;
    }
  }

  static public Builder getBuilder() {
    return new Builder();
  }

  // Convert to JSON.
  public JSONObject toJSONObject() {
    JSONObject json_obj = new JSONObject();
    try {
      json_obj.put("sid", this.sid);
      if (this.school != null) {
        json_obj.put("school", this.school);
      }
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

  static public Education parseFromJSON(JSONObject json_obj) {
    return Education.getBuilder().buildFromJSON(json_obj);
  }

  // Given a list of Education object, convert to JSON array.
  static public JSONArray toJSONArray(List<Education> educations) {
    JSONArray array = new JSONArray();
    for (Education education : educations) {
      array.put(education.toJSONObject());
    }
    return array;
  }

  // Convert a JSON education array to List<Education>.
  static public ArrayList<Education> parseFromJSONArray(JSONArray array) {
    ArrayList<Education> educations = new ArrayList<Education>();
    try {
      for (int i = 0; i < array.length(); i++) {
        educations.add(
            Education.getBuilder().buildFromJSON((JSONObject)array.get(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return educations;
  }

  public int getSid() {
    return this.sid;
  }
  public void setSid(int sid) {
    this.sid = sid;
  }

  public String getSchool() {
    return this.school;
  }
  public void setSchool(String school) {
    this.school = school;
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
