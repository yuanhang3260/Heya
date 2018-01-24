package bean;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import db.DBManager;

import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.methods.HttpUriRequest;

import bean.*;

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
  private String work;
  private String live;

  public static User GetUserByUsername(String username) {
    String sqlStr = "SELECT * from Users where username = \"" + username + "\"";

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      conn = DBManager.getDBConnection();
      stmt = conn.prepareStatement(sqlStr);
  
      rset = stmt.executeQuery();
      if (rset.next()) {
        User user = new User();
        user.setUid(rset.getInt("uid"));
        user.setUsername(rset.getString("username"));
        user.setEmail(rset.getString("email"));
        user.setPassword(rset.getString("password"));
        user.setBirth(rset.getDate("birth"));
        if (rset.getString("sex") != null) {
          user.setSex(Sex.valueOf(rset.getString("sex")));
        }
        user.setWork(rset.getString("work"));
        user.setLive(rset.getString("live"));
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

  public String getLive() {
    return this.live;
  }

  public void setLive(String live) {
    this.live = live;
  }
}
