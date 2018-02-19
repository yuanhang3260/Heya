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
  private String work;
  private String places;
  private String relationship;
  private String phone;
  private String links;

  public static User GetUserByUsername(String username) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      String sqlStr = "SELECT * from Users where username = ?";
      stmt = conn.prepareStatement(sqlStr);
      stmt.setString(1, username);
  
      rset = stmt.executeQuery();
      if (rset.next()) {
        User user = new User();
        user.setUid(rset.getInt("uid"));
        user.setUsername(rset.getString("username"));
        user.setEmail(rset.getString("email"));
        user.setPassword(rset.getString("password"));
        return user;
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        if (rset != null) {
          rset.close();
        }
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public boolean getUserDetailInfo() {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      String sqlStr = "SELECT * from UserDetail where uid = ?";
      stmt = conn.prepareStatement(sqlStr);
      stmt.setInt(1, uid);

      rset = stmt.executeQuery();
      if (rset.next()) {
        this.name = rset.getString("name");
        this.birth = rset.getDate("birth");
        if (rset.getString("sex") != null) {
          this.sex = Sex.valueOf(rset.getString("sex"));
        }
        if (rset.getString("education") != null) {
          this.education = Education.parseFromJSONArray(
              new JSONArray(rset.getString("education")));
        }
        this.work = rset.getString("work");
        this.places = rset.getString("places");
        this.relationship = rset.getString("relationship");
        this.phone = rset.getString("phone");
        this.links = rset.getString("links");
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } catch (JSONException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (rset != null) {
          rset.close();
        }
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public static int getNextUid() {
    String sqlStr = "SELECT MAX(uid) as max_uid FROM Users";

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      conn = DBManager.getDBConnection();
      stmt = conn.prepareStatement(sqlStr);
      rset = stmt.executeQuery();
      int uid = 0;
      if (rset.next()) {
        uid = rset.getInt("max_uid") + 1;
        if (rset.wasNull()) {
          uid = 0;
        }
      }
      return uid;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    } finally {
      try {
        if (rset != null) {
          rset.close();
        }
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public static boolean addNewUser(int uid,
                                   String username,
                                   String email,
                                   String password) {
    String addUser = "INSERT INTO Users (uid, username, email, password) " +
                     "VALUES (?, ?, ?, ?)";
    String addUserDetail = "INSERT INTO UserDetail (uid) VALUES (?)";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(addUser);
      stmt.setInt(1, uid);
      stmt.setString(2, username);
      stmt.setString(3, email);
      stmt.setString(4, password);
      stmt.executeUpdate();

      stmt = conn.prepareStatement(addUserDetail);
      stmt.setInt(1, uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }  finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
    }
  }

  public String getProfileImg(HttpServletRequest request) {
    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");
    String path = Paths.get(
        baseDir, "user/", Integer.toString(uid), "/profile.jpg").toString();
    File file = new File(path);
    if (file.exists()) {
      return profileImageURL(Integer.toString(uid));
    } else {
      return profileImageURL("default");
    }
  }

  public String getProfileCoverImg(HttpServletRequest request) {
    ServletContext context = request.getServletContext();
    String baseDir = context.getInitParameter("data-storage");
    String path = Paths.get(
      baseDir, "user/", Integer.toString(uid), "/profile-cover.jpg").toString();
    File file = new File(path);
    if (file.exists()) {
      return profileCoverImageURL(Integer.toString(uid));
    } else {
      return profileCoverImageURL("default");
    }
  }

  private String profileImageURL(String user) {
    final HttpUriRequest request = RequestBuilder.get()
        .setUri("image")
        .addParameter("type", "userinfo")
        .addParameter("uid", user)
        .addParameter("username", username)
        .addParameter("q", "profile")
        .build();

    return request.getURI().toString();
  }

  private String profileCoverImageURL(String user) {
    final HttpUriRequest request = RequestBuilder.get()
        .setUri("image")
        .addParameter("type", "userinfo")
        .addParameter("uid", user)
        .addParameter("username", username)
        .addParameter("q", "profile-cover")
        .build();

    return request.getURI().toString();
  }

  static public boolean updateUserBaiscInfo(
      int uid, String name, String email, String phone, Date birth) {
    String updateUser = "UPDATE Users " +
                        "SET email = ? " +
                        "WHERE uid = ?";
    String updateDetail = "UPDATE UserDetail " +
                          "SET name = ?, phone = ?, birth = ? " +
                          "WHERE uid = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(updateUser);
      stmt.setString(1, email);
      stmt.setInt(2, uid);
      stmt.executeUpdate();

      stmt = conn.prepareStatement(updateDetail);
      if (name != null && !name.isEmpty()) {
        stmt.setString(1, name);
      } else {
        stmt.setNull(1, Types.NCHAR);
      }
      if (phone != null && !phone.isEmpty()) {
        stmt.setString(2, phone);
      } else {
        stmt.setNull(2, Types.NCHAR);
      }
      if (birth != null) {
        stmt.setDate(3, new java.sql.Date(birth.getTime()));
      } else {
        stmt.setNull(3, Types.DATE);
      }
      stmt.setInt(4, uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
        }
      }
      catch (SQLException ex) {
        ex.printStackTrace();
      }
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
      if (education != null) {
        json_obj.put("education", Education.toJSONArray(education));
      }
      if (work != null) {
        json_obj.put("work", work);
      }
      if (places != null) {
        json_obj.put("places", new JSONArray(places));
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
    } catch (JSONException e) {
      e.printStackTrace();
    }
    return json_obj;
  }

  // Getters and setters.
  public int getUid() {
    return this.uid;
  }

  public void setUid(int id) {
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

  public void setname(String name) {
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

  public String getWork() {
    return this.work;
  }

  public void setWork(String work) {
    this.work = work;
  }

  public List<Education> getEducation() {
    return this.education;
  }

  public void addEducation(Education education) {
    this.education.add(education);
  }

  public void updateEducation(Education education) {
    for (int i = 0; i < this.education.size(); i++) {
      if (this.education.get(i).getId() == education.getId()) {
        this.education.set(i, education);
      }
    }
  }

  public void deleteEducation(int id) {
    for (int i = 0; i < this.education.size(); i++) {
      if (this.education.get(i).getId() == id) {
        this.education.remove(i);
      }
    }
  }

  public String getPlaces() {
    return this.places;
  }

  public void setPlaces(String places) {
    this.places = places;
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
