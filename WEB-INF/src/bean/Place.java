package bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Place {
  private int pid;  // internal ID just for this user.
  private String place;
  private boolean current;
  private boolean hometown;

  // Constructors.
  public Place() {}
  public Place(Place other) {
    this.pid = other.pid;
    this.place = other.place;
    this.current = other.current;
    this.hometown = other.hometown;
  }

  // Builder pattern.
  public static class Builder {
    private Place obj;

    public Builder() {}

    private Place obj() {
      if (this.obj == null) {
        this.obj = new Place();
      }
      return this.obj;
    }

    public Builder setPid(int pid) {
      obj().setPid(pid);
      return this;
    }
    public Builder setPlace(String place) {
      obj().setPlace(place);
      return this;
    }
    public Builder setCurrent(boolean current) {
      obj().setCurrent(current);
      return this;
    }
    public Builder setHometown(boolean hometown) {
      obj().setHometown(hometown);
      return this;
    }

    public Place buildFromJSON(JSONObject json_obj) {
      this.obj = new Place();
      try {
        if (!json_obj.isNull("pid")) {
          this.obj.setPid(json_obj.getInt("pid"));
        }
        if (!json_obj.isNull("place")) {
          this.obj.setPlace(json_obj.getString("place"));
        }
        if (!json_obj.isNull("current")) {
          this.obj.setCurrent(json_obj.getBoolean("current"));
        }
        if (!json_obj.isNull("hometown")) {
          this.obj.setHometown(json_obj.getBoolean("hometown"));
        }
      } catch (JSONException e) {
        e.printStackTrace();
        return null;
      }
      Place ret = this.obj;
      this.obj = null;
      return ret;
    }

    Place build() {
      Place ret = obj();
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
      json_obj.put("pid", this.pid);
      if (this.place != null) {
        json_obj.put("place", this.place);
      }
      json_obj.put("current", this.current);
      json_obj.put("hometown", this.hometown);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  static public Place parseFromJSON(JSONObject json_obj) {
    return Place.getBuilder().buildFromJSON(json_obj);
  }

  // Given a list of Place object, convert to JSON array.
  static public JSONArray toJSONArray(List<Place> educations) {
    JSONArray array = new JSONArray();
    for (Place education : educations) {
      array.put(education.toJSONObject());
    }
    return array;
  }

  // Convert a JSON education array to List<Place>.
  static public ArrayList<Place> parseFromJSONArray(JSONArray array) {
    ArrayList<Place> educations = new ArrayList<Place>();
    try {
      for (int i = 0; i < array.length(); i++) {
        educations.add(
            Place.getBuilder().buildFromJSON((JSONObject)array.get(i)));
      }
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }
    return educations;
  }

  public int getPid() {
    return this.pid;
  }
  public void setPid(int pid) {
    this.pid = pid;
  }

  public String getPlace() {
    return this.place;
  }
  public void setPlace(String place) {
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
