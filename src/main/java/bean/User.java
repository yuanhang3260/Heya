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

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import bean.Education;
import bean.Work;
import bean.Place;
import db.DBManager;

public class User {
  public enum Sex {
    Male,
    Female
  }

  private int uid;
  private String username;
  private String email;
  private String password;

  private String name;
  private Date birth;
  private Sex sex;
  private ArrayList<Education> education;
  private ArrayList<Work> work;
  private ArrayList<Place> places;
  private String relationship;
  private String phone;
  private String links;

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
    if (user.getEducation() != null) {
      this.education = user.getEducation();
    }
    if (user.getWork() != null) {
      this.work = user.getWork();
    }
    if (user.getPlaces() != null) {
      this.places = user.getPlaces();
    }
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

  // Getters and setters.
  public int getUid() {
    return this.uid;
  }

  public void setUid(int uid) {
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

  public ArrayList<Work> getWork() {
    return this.work;
  }

  public void setWork(ArrayList<Work> work) {
    this.work = work;
  }

  public void addWork(Work work) {
    this.work.add(work);
  }

  public void updateWork(Work work) {
    for (int i = 0; i < this.work.size(); i++) {
      if (this.work.get(i).getCid() == work.getCid()) {
        this.work.set(i, work);
      }
    }
  }

  public void deleteWork(int id) {
    for (int i = 0; i < this.work.size(); i++) {
      if (this.work.get(i).getCid() == id) {
        this.work.remove(i);
      }
    }
  }

  public ArrayList<Education> getEducation() {
    return this.education;
  }

  public void setEducation(ArrayList<Education> education) {
    this.education = education;
  }

  public void addEducation(Education education) {
    this.education.add(education);
  }

  public void updateEducation(Education education) {
    for (int i = 0; i < this.education.size(); i++) {
      if (this.education.get(i).getSid() == education.getSid()) {
        this.education.set(i, education);
      }
    }
  }

  public void deleteEducation(int sid) {
    for (int i = 0; i < this.education.size(); i++) {
      if (this.education.get(i).getSid() == sid) {
        this.education.remove(i);
      }
    }
  }

  public ArrayList<Place> getPlaces() {
    return this.places;
  }

  public void setPlaces(ArrayList<Place> places) {
    this.places = places;
  }

  public void addPlace(Place place) {
    this.places.add(place);
  }

  public void updatePlace(Place place) {
    for (int i = 0; i < this.places.size(); i++) {
      if (this.places.get(i).getPid() == place.getPid()) {
        this.places.set(i, place);
      }
    }
  }

  public void deletePlace(int pid) {
    for (int i = 0; i < this.places.size(); i++) {
      if (this.places.get(i).getPid() == pid) {
        this.places.remove(i);
      }
    }
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
}
