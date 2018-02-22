package bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Work {
  private int id;  // internal ID just for this user.
  private String company;
  private String position;
  private Integer startYear;
  private Integer endYear;

  // Constructors.
  public Work() {}
  public Work(Work other) {
    this.id = other.id;
    this.company = other.company;
    this.position = other.position;
    this.startYear = other.startYear;
    this.endYear = other.endYear;
  }

  // Builder pattern.
  public static class Builder {
    private Work obj;

    public Builder() {}

    private Work obj() {
      if (this.obj == null) {
        this.obj = new Work();
      }
      return this.obj;
    }

    public Builder setId(int id) {
      obj().setId(id);
      return this;
    }
    public Builder setCompany(String company) {
      obj().setCompany(company);
      return this;
    }
    public Builder setPosition(String position) {
      obj().setPosition(position);
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

    public Work buildFromJSON(JSONObject json_obj) {
      this.obj = new Work();
      try {
        if (!json_obj.isNull("id")) {
          this.obj.setId(json_obj.getInt("id"));
        }
        if (!json_obj.isNull("company")) {
          this.obj.setCompany(json_obj.getString("company"));
        }
        if (!json_obj.isNull("position")) {
          this.obj.setPosition(json_obj.getString("position"));
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
      Work ret = this.obj;
      this.obj = null;
      return ret;
    }

    Work build() {
      Work ret = obj();
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
      json_obj.put("id", this.id);
      if (this.company != null) {
        json_obj.put("company", this.company);
      }
      if (this.position != null) {
        json_obj.put("position", this.position);
      }
      if (this.startYear != null) {
        json_obj.put("startYear", this.startYear);
      }
      if (this.endYear != null) {
        json_obj.put("endYear", this.endYear);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  static public Work parseFromJSON(JSONObject json_obj) {
    return Work.getBuilder().buildFromJSON(json_obj);
  }

  // Given a list of Work object, convert to JSON array.
  static public JSONArray toJSONArray(List<Work> works) {
    JSONArray array = new JSONArray();
    for (Work work : works) {
      array.put(work.toJSONObject());
    }
    return array;
  }

  // Convert a JSON Work array to List<Work>.
  static public ArrayList<Work> parseFromJSONArray(JSONArray array) {
    ArrayList<Work> works = new ArrayList<Work>();
    try {
      for (int i = 0; i < array.length(); i++) {
        works.add(
            Work.getBuilder().buildFromJSON((JSONObject)array.get(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return works;
  }

  public int getId() {
    return this.id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public String getcompany() {
    return this.company;
  }
  public void setCompany(String company) {
    this.company = company;
  }

  public String getposition() {
    return this.position;
  }
  public void setPosition(String position) {
    this.position = position;
  }

  public int getStartYear() {
    return this.startYear;
  }
  public void setStartYear(Integer year) {
    this.startYear = year;
  }

  public int getEndYear() {
    return this.endYear;
  }
  public void setEndYear(Integer year) {
    this.endYear = year;
  }
}
