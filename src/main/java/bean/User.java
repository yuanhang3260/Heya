package bean;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.Education;
import bean.Work;
import bean.Place;
import bean.UserEducation;

public class User {
  public enum Sex {
    Male,
    Female
  }

  private String uid;
  private String username;
  private String email;
  private String password;

  private String name;
  private Date birth;
  private Sex sex;
  private String relationship;
  private String phone;
  private String links;
  private Set<UserEducation> userEducation;
  // private Set<Work> work;
  // private Set<Place> places;

  public void mergeFrom(User user) {
    if (user.getUsername() != null) {
      this.username = user.getUsername();
    }
    if (user.getEmail() != null) {
      this.email = user.getEmail();
    }
    if (user.getPassword() != null) {
      this.password = user.getPassword();
    }
    if (user.getName() != null) {
      this.name = user.getName();
    }
    if (user.getBirth() != null) {
      this.birth = user.getBirth();
    }
    if (user.getSex() != null) {
      this.sex = user.getSex();
    }
    if (user.getUserEducation() != null) {
      this.userEducation = user.getUserEducation();
    }
    // if (user.getWork() != null) {
    //   this.work = user.getWork();
    // }
    // if (user.getPlaces() != null) {
    //   this.places = user.getPlaces();
    // }
    if (user.getRelationship() != null) {
      this.relationship = user.getRelationship();
    }
    if (user.getPhone() != null) {
      this.phone = user.getPhone();
    }
    if (user.getLinks() != null) {
      this.links = user.getLinks();
    }
  }

  public JSONObject toJSONObject() {
    JSONObject json_obj = new JSONObject();
    try {
      json_obj.put("uid", uid);
      if (username != null) {
        json_obj.put("username", username);
      }
      if (email != null) {
        json_obj.put("email", email);
      }

      if (name != null) {
        json_obj.put("name", name);
      }
      if (birth != null) {
        json_obj.put("birth", birth);
      }
      if (sex != null) {
        json_obj.put("sex", sex.toString());
      }
      if (relationship != null) {
        json_obj.put("relationship", relationship);
      }
      if (phone != null) {
        json_obj.put("phone", phone);
      }
      if (links != null) {
        json_obj.put("links", links);
      }
      if (userEducation != null) {
        json_obj.put("education", UserEducation.toJSONArray(userEducation));
      }
      // if (work != null) {
      //   json_obj.put("work", Work.toJSONArray(work));
      // }
      // if (places != null) {
      //   json_obj.put("places", Place.toJSONArray(places));
      // }
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  // Getters and setters.
  public String getUid() {
    return this.uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getBirth() {
    return this.birth;
  }

  public void setBirth(Date birth) {
    this.birth = birth;
  }

  public Sex getSex() {
    return this.sex;
  }

  public void setSex(Sex sex) {
    this.sex = sex;
  }

  public String getRelationship() {
    return this.relationship;
  }

  public void setRelationship(String relationship) {
    this.relationship = relationship;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getLinks() {
    return this.links;
  }

  public void setLinks(String Links) {
    this.links = links;
  }

  public Set<UserEducation> getUserEducation() {
    if (this.userEducation == null) {
      this.userEducation = new HashSet<UserEducation>();
    }
    return this.userEducation;
  }

  public void setUserEducation(Set<UserEducation> userEducation) {
    this.userEducation = userEducation;
  }

  // public void addEducation(Education education) {
  //   this.education.add(education);
  // }

  // public void updateEducation(Education education) {
  //   for (int i = 0; i < this.education.size(); i++) {
  //     if (this.education.get(i).getSid() == education.getSid()) {
  //       this.education.set(i, education);
  //     }
  //   }
  // }

  // public void deleteEducation(int sid) {
  //   for (int i = 0; i < this.education.size(); i++) {
  //     if (this.education.get(i).getSid() == sid) {
  //       this.education.remove(i);
  //     }
  //   }
  // }

  // public Set<Work> getWork() {
  //   return this.work;
  // }

  // public void setWork(Set<Work> work) {
  //   this.work = work;
  // }

  // public void addWork(Work work) {
  //   this.work.add(work);
  // }

  // public void updateWork(Work work) {
  //   for (int i = 0; i < this.work.size(); i++) {
  //     if (this.work.get(i).getCid() == work.getCid()) {
  //       this.work.set(i, work);
  //     }
  //   }
  // }

  // public void deleteWork(int id) {
  //   for (int i = 0; i < this.work.size(); i++) {
  //     if (this.work.get(i).getCid() == id) {
  //       this.work.remove(i);
  //     }
  //   }
  // }

  // public ArrayList<Place> getPlaces() {
  //   return this.places;
  // }

  // public void setPlaces(ArrayList<Place> places) {
  //   this.places = places;
  // }

  // public void addPlace(Place place) {
  //   this.places.add(place);
  // }

  // public void updatePlace(Place place) {
  //   for (int i = 0; i < this.places.size(); i++) {
  //     if (this.places.get(i).getPid() == place.getPid()) {
  //       this.places.set(i, place);
  //     }
  //   }
  // }

  // public void deletePlace(int pid) {
  //   for (int i = 0; i < this.places.size(); i++) {
  //     if (this.places.get(i).getPid() == pid) {
  //       this.places.remove(i);
  //     }
  //   }
  // }
}
