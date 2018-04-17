package bean;

import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Work {
  private String cid;
  private String company;

  // Constructors.
  public Work() {}
  public Work(Work other) {
    this.cid = other.cid;
    this.company = other.company;
  }

  public String getCid() {
    return this.cid;
  }
  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getCompany() {
    return this.company;
  }
  public void setCompany(String company) {
    this.company = company;
  }
}
