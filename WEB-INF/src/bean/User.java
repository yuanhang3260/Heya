package bean;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import db.DBManager;

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

  public User() {}

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
    String sqlStr = "INSERT INTO Users (uid, username, email, password) " +
                    "VALUES (" +
                    Integer.toString(uid) + ", " +
                    DBManager.buildSqlStringValue(username) + ", " +
                    DBManager.buildSqlStringValue(email) + ", " +
                    DBManager.buildSqlStringValue(password) + ")";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      stmt = conn.prepareStatement(sqlStr);
      int re = stmt.executeUpdate();
      if (re != 1) {
        return false;
      }
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
