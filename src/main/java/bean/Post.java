package bean;

import java.util.*;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class Post {
  private String pid;
  private String uid;
  private Date createTime;
  private String content;
  private String pictures;

  // Constructors.
  public Post() {}
  public Post(Post other) {
    this.pid = other.pid;
    this.createTime = other.createTime;
    this.content = other.content;
    this.pictures = other.pictures;
  }

  public JSONObject toJSONObject() {
    JSONObject json_obj = new JSONObject();
    try {
      json_obj.put("uid", uid);
      json_obj.put("pid", pid);
      if (this.content != null) {
        json_obj.put("content", this.content);
      }
      JSONArray array = new JSONArray();
      for (String pic: getPicturesAsList()) {
        array.put(pic);
      }
      json_obj.put("pictures", array);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  public String getPid() {
    return this.pid;
  }
  public void setPid(String pid) {
    this.pid = pid;
  }

  public String getUid() {
    return this.uid;
  }
  public void setUid(String uid) {
    this.uid = uid;
  }

  public Date getCreateTime() {
    return this.createTime;
  }
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getContent() {
    return this.content;
  }
  public void setContent(String content) {
    this.content = content;
  }

  public String getPictures() {
    return this.pictures;
  }
  public void setPictures(String pictures) {
    this.pictures = pictures;
  }
  public void setPicturesFromList(List<String> pictures) {
    this.pictures = String.join(",\n", pictures);
  }
  public List<String> getPicturesAsList() {
    List<String> result = new ArrayList<String>();
    for (String pic: this.pictures.split(",\n")) {
      result.add(pic);
    }
    return result;
  }
}
