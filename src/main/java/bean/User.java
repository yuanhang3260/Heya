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

  public static User GetUserByUid(int uid) {
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rset = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      String sqlStr = "SELECT * from Users where uid = ?";
      stmt = conn.prepareStatement(sqlStr);
      stmt.setInt(1, uid);
  
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
        if (rset.getString("work") != null) {
          this.work = Work.parseFromJSONArray(
              new JSONArray(rset.getString("work")));
        }
        if (rset.getString("places") != null) {
          this.places = Place.parseFromJSONArray(
              new JSONArray(rset.getString("places")));
        }
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

  public String profileImageURL() {
    final HttpUriRequest request = RequestBuilder.get()
        .setUri("profileimage")
        .addParameter("uid", Integer.toString(uid))
        .addParameter("type", "profile")
        .build();

    return request.getURI().toString();
  }

  public String profileCoverImageURL() {
    final HttpUriRequest request = RequestBuilder.get()
        .setUri("profileimage")
        .addParameter("uid", Integer.toString(uid))
        .addParameter("type", "cover")
        .build();

    return request.getURI().toString();
  }

  public boolean updateUserBasicInfo(
      String name, String email, String phone, Date birth) {
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
      stmt.setInt(2, this.uid);
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
      stmt.setInt(4, this.uid);
      stmt.executeUpdate();

      conn.commit();

      this.name = name;
      this.email = email;
      this.phone = phone;
      this.birth = birth;
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

  // ----------------------------------------------------------------------- //
  public int addSchoolInfo(Education education) {
    return addSchoolInfo(education.getSchool(), education.getMajor(),
                         education.getStartYear(), education.getEndYear());
  }

  public int addSchoolInfo(String school, String major,
                           Integer startYear, Integer endYear) {
    if (this.education == null) {
      this.education = new ArrayList<Education>();
    }

    int sid = 0;
    if (!this.education.isEmpty()) {
      sid = this.education.get(this.education.size() - 1).getSid() + 1;
    }

    Education newSchool = Education.getBuilder()
                                   .setSid(sid)
                                   .setSchool(school)
                                   .setMajor(major)
                                   .setStartYear(startYear)
                                   .setEndYear(endYear)
                                   .build();

    JSONArray array = Education.toJSONArray(this.education);
    array.put(newSchool.toJSONObject());

    String sql = "UPDATE UserDetail set education = ? WHERE uid = ?";
    System.out.println("education: " + array);

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      this.education.add(newSchool);
      return sid;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    } catch (JSONException e) {
      e.printStackTrace();
      return -1;
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

  public boolean updateSchoolInfo(Integer sid, Education education) {
    return updateSchoolInfo(sid,
                            education.getSchool(), education.getMajor(),
                            education.getStartYear(), education.getEndYear());
  }

  public boolean updateSchoolInfo(int sid, String school, String major,
                                  Integer startYear, Integer endYear) {
    if (this.education == null) {
      return false;
    }

    int index = -1;
    for (index = 0; index < this.education.size(); index++) {
      if (this.education.get(index).getSid() == sid) {
        break;
      }
    }

    if (index < 0) {
      return false;
    }

    Education copy = new Education(this.education.get(index));
    this.education.set(index, Education.getBuilder()
                                       .setSid(sid)
                                       .setSchool(school)
                                       .setMajor(major)
                                       .setStartYear(startYear)
                                       .setEndYear(endYear)
                                       .build());

    JSONArray array = Education.toJSONArray(this.education);

    String sql = "UPDATE UserDetail set education = ? WHERE uid = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      // Rollback local education info.
      this.education.set(index, copy);
      return false;
    } catch (JSONException e) {
      e.printStackTrace();
      // Rollback local education info.
      this.education.set(index, copy);
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

  public boolean deleteSchoolInfo(int sid) {
    if (this.education == null) {
      return true;
    }

    int index = -1;
    for (index = 0; index < this.education.size(); index++) {
      if (this.education.get(index).getSid() == sid) {
        break;
      }
    }

    if (index < 0) {
      return true;
    }

    Education removed = this.education.remove(index);
    JSONArray array = Education.toJSONArray(this.education);

    String sql = "UPDATE UserDetail set education = ? WHERE uid = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      // Rollback local education info.
      this.education.add(index, removed);
      return false;
    } catch (JSONException e) {
      e.printStackTrace();
      // Rollback local education info.
      this.education.add(index, removed);
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

  // ----------------------------------------------------------------------- //
  public int addCompanyInfo(Work work) {
    return addCompanyInfo(work.getCompany(), work.getPosition(),
                          work.getStartYear(), work.getEndYear());
  }

  public int addCompanyInfo(String company, String position,
                            Integer startYear, Integer endYear) {
    if (this.work == null) {
      this.work = new ArrayList<Work>();
    }

    int cid = 0;
    if (!this.work.isEmpty()) {
      cid = this.work.get(this.work.size() - 1).getCid() + 1;
    }

    Work newCompany = Work.getBuilder()
                          .setCid(cid)
                          .setCompany(company)
                          .setPosition(position)
                          .setStartYear(startYear)
                          .setEndYear(endYear)
                          .build();

    JSONArray array = Work.toJSONArray(this.work);
    array.put(newCompany.toJSONObject());

    String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";
    System.out.println("work: " + array);

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      this.work.add(newCompany);
      return cid;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    } catch (JSONException e) {
      e.printStackTrace();
      return -1;
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

  public boolean updateCompanyInfo(int cid, Work work) {
    return updateCompanyInfo(cid, work.getCompany(), work.getPosition(),
                                  work.getStartYear(), work.getEndYear());
  }

  public boolean updateCompanyInfo(int cid, String company, String position,
                                   Integer startYear, Integer endYear) {
    if (this.work == null) {
      return false;
    }

    int index = -1;
    for (index = 0; index < this.work.size(); index++) {
      if (this.work.get(index).getCid() == cid) {
        break;
      }
    }

    if (index < 0) {
      return false;
    }

    Work copy = new Work(this.work.get(index));
    this.work.set(index, Work.getBuilder()
                             .setCid(cid)
                             .setCompany(company)
                             .setPosition(position)
                             .setStartYear(startYear)
                             .setEndYear(endYear)
                             .build());

    JSONArray array = Work.toJSONArray(this.work);

    String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      // Rollback local work info.
      this.work.set(index, copy);
      return false;
    } catch (JSONException e) {
      e.printStackTrace();
      // Rollback local work info.
      this.work.set(index, copy);
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

  public boolean deleteCompanyInfo(int cid) {
    if (this.work == null) {
      return true;
    }

    int index = -1;
    for (index = 0; index < this.work.size(); index++) {
      if (this.work.get(index).getCid() == cid) {
        break;
      }
    }

    if (index < 0) {
      return true;
    }

    Work removed = this.work.remove(index);
    JSONArray array = Work.toJSONArray(this.work);

    String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      // Rollback local work info.
      this.work.add(index, removed);
      return false;
    } catch (JSONException e) {
      e.printStackTrace();
      // Rollback local work info.
      this.work.add(index, removed);
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

  // ----------------------------------------------------------------------- //
  public int addPlaceInfo(Place place) {
    return addPlaceInfo(place.getPlace(),
                        place.getCurrent(), place.getHometown());
  }

  public int addPlaceInfo(String place, boolean current, boolean hometown) {
    if (this.places == null) {
      this.places = new ArrayList<Place>();
    }

    int pid = 0;
    if (!this.places.isEmpty()) {
      pid = this.places.get(this.places.size() - 1).getPid() + 1;
    }

    Place newPlace = Place.getBuilder()
                          .setPid(pid)
                          .setPlace(place)
                          .setCurrent(current)
                          .setHometown(hometown)
                          .build();

    JSONArray array = Place.toJSONArray(this.places);
    array.put(newPlace.toJSONObject());

    String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";
    System.out.println("places: " + array);

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      this.places.add(newPlace);
      return pid;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    } catch (JSONException e) {
      e.printStackTrace();
      return -1;
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

  public boolean updatePlaceInfo(int pid, Place place) {
    return updatePlaceInfo(pid, place.getPlace(),
                           place.getCurrent(), place.getHometown());
  }

  public boolean updatePlaceInfo(int pid, String place,
                                 boolean current, boolean hometown) {
    if (this.places == null) {
      return false;
    }

    int index = -1;
    for (index = 0; index < this.places.size(); index++) {
      if (this.places.get(index).getPid() == pid) {
        break;
      }
    }

    if (index < 0) {
      return false;
    }

    Place copy = new Place(this.places.get(index));
    this.places.set(index, Place.getBuilder()
                               .setPid(pid)
                               .setPlace(place)
                               .setCurrent(current)
                               .setHometown(hometown)
                               .build());

    JSONArray array = Place.toJSONArray(this.places);

    String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      // Rollback local work info.
      this.places.set(index, copy);
      return false;
    } catch (JSONException e) {
      e.printStackTrace();
      // Rollback local work info.
      this.places.set(index, copy);
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

  public boolean deletePlaceInfo(int pid) {
    if (this.places == null) {
      return true;
    }

    int index = -1;
    for (index = 0; index < this.places.size(); index++) {
      if (this.places.get(index).getPid() == pid) {
        break;
      }
    }

    if (index < 0) {
      return true;
    }

    Place removed = this.places.remove(index);
    JSONArray array = Place.toJSONArray(this.places);

    String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";

    Connection conn = null;
    PreparedStatement stmt = null;
    try {
      conn = DBManager.getDBConnection();
      conn.setAutoCommit(false);

      stmt = conn.prepareStatement(sql);
      stmt.setString(1, array.toString(2));
      stmt.setInt(2, this.uid);
      stmt.executeUpdate();

      conn.commit();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      // Rollback local work info.
      this.places.add(index, removed);
      return false;
    } catch (JSONException e) {
      this.places.add(index, removed);
      return false;
    }
    finally {
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
        json_obj.put("work", Work.toJSONArray(work));
      }
      if (places != null) {
        json_obj.put("places", Place.toJSONArray(places));
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

  public List<Work> getWork() {
    return this.work;
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

  public List<Education> getEducation() {
    return this.education;
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

  public List<Place> getPlace() {
    return this.places;
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
