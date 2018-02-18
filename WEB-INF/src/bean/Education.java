package bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Education {
  private int id;  // internal ID just for this user.
  private String school;
  private String major;
  private int startYear;
  private int endYear;

  public Education() {}

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

    public Builder setId(int id) {
      obj().setId(id);
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
    public Builder setStartYear(int year) {
      obj().setStartYear(year);
      return this;
    }
    public Builder setEndYear(int year) {
      obj().setEndYear(year);
      return this;
    }

    public Education buildFromJSON(JSONObject json_obj) {
      this.obj = new Education();
      try {
        if (!json_obj.isNull("id")) {
          this.obj.setId(json_obj.getInt("id"));
        }
        if (!json_obj.isNull("school")) {
          this.obj.setSchool(json_obj.getString("school"));
        }
        if (!json_obj.isNull("major")) {
          this.obj.setMajor(json_obj.getString("major"));
        }
        if (!json_obj.isNull("startYear")) {
          this.obj.setStartYear(json_obj.getInt("startYear"));
        }
        if (!json_obj.isNull("endYear")) {
          this.obj.setEndYear(json_obj.getInt("endYear"));
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
      json_obj.put("id", this.id)
              .put("school", this.school)
              .put("major", this.major)
              .put("startYear", startYear)
              .put("endYear", endYear);
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

  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
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

  public int getStartYear() {
    return this.startYear;
  }
  public void setStartYear(int year) {
    this.startYear = year;
  }

  public int getEndYear() {
    return this.endYear;
  }
  public void setEndYear(int year) {
    this.endYear = year;
  }
}
