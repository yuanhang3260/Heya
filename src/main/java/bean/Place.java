package bean;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Place {
  private String pid;
  private String name;

  // Constructors.
  public Place() {}
  public Place(Place other) {
    this.pid = other.pid;
    this.name = other.name;
  }

  public String getPid() {
    return this.pid;
  }
  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }
}
