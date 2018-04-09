package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bean.Education;
import bean.Work;
import bean.Place;
import bean.User;

@Repository("UserDAO")
public class UserDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private Integer nextUid;

  public void init() {
    this.nextUid = fetchNextUid();
  }

  public int getNextUid() {
    synchronized(this) {
      return this.nextUid++;
    }
  }

  public User GetUserByUsername(String username) {
    String sql = "SELECT * from Users where username = ?";
    RowMapper<User> mapper = new BeanPropertyRowMapper<>(User.class);

    User user;
    try {
      user = this.jdbcTemplate.queryForObject(sql, mapper, username);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
    return user;
  }

  public User GetUserByUid(int uid) {
    String sql = "SELECT * from Users where uid = ?";
    RowMapper<User> mapper = new BeanPropertyRowMapper<>(User.class);

    User user;
    try {
      user = this.jdbcTemplate.queryForObject(sql, mapper, uid);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
    return user;
  }

  private static class UserDetailRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rset, int rowNumber) throws SQLException {
      User user = new User();
      user.setName(rset.getString("name"));
      user.setBirth(rset.getDate("birth"));
      if (rset.getString("sex") != null) {
        user.setSex(User.Sex.valueOf(rset.getString("sex")));
      }
      if (rset.getString("education") != null) {
        user.setEducation(Education.parseFromJSONArray(
            new JSONArray(rset.getString("education"))));
      }
      if (rset.getString("work") != null) {
        user.setWork(Work.parseFromJSONArray(
            new JSONArray(rset.getString("work"))));
      }
      if (rset.getString("places") != null) {
        user.setPlaces(Place.parseFromJSONArray(
            new JSONArray(rset.getString("places"))));
      }
      user.setRelationship(rset.getString("relationship"));
      user.setPhone(rset.getString("phone"));
      user.setLinks(rset.getString("links"));
      return user;
    }
  }

  public User getUserDetailInfo(User user) {
    String sql = "SELECT * from UserDetail where uid = ?";
    try {
      User userDetail = this.jdbcTemplate.queryForObject(
          sql, new UserDetailRowMapper(), user.getUid());
      user.mergeFrom(userDetail);
    } catch (EmptyResultDataAccessException e) {
      return user;
    }
    return user;
  }

  private int fetchNextUid() {
    String sql = "SELECT MAX(uid) as max_uid FROM Users";
    try {
      int max = this.jdbcTemplate.queryForObject(sql, int.class);
      return max + 1;
    } catch (EmptyResultDataAccessException e) {
      return 0;
    }
  }

  @Transactional
  public boolean addNewUser(int uid,
                            String username,
                            String email,
                            String password) {
    String addUser = "INSERT INTO Users (uid, username, email, password) " +
                     "VALUES (?, ?, ?, ?)";
    String addUserDetail = "INSERT INTO UserDetail (uid) VALUES (?)";

    try {
      this.jdbcTemplate.update(addUser, uid, username, email, password);
      this.jdbcTemplate.update(addUserDetail, uid);
      return true;
    } catch (DataAccessException e) {
      return false;
    }
  }

  @Transactional
  public boolean updateUserBasicInfo(
      int uid, String name, String email, String phone, Date birth) {
    if (name != null && name.isEmpty()) {
      name = null;
    }
    if (phone != null && phone.isEmpty()) {
      phone = null;
    }

    String updateUser = "UPDATE Users " +
                        "SET email = ? " +
                        "WHERE uid = ?";
    String updateDetail = "UPDATE UserDetail " +
                          "SET name = ?, phone = ?, birth = ? " +
                          "WHERE uid = ?";

    try {
      this.jdbcTemplate.update(updateUser, email, uid);
      this.jdbcTemplate.update(updateDetail, name, phone, birth, uid);
      return true;
    } catch (DataAccessException e) {
      return false;
    }
  }

  // ----------------------------- Education ------------------------------- //
  public int addSchoolInfo(User user, Education newSchool) {
    if (user == null || newSchool == null) {
      return -1;
    }

    ArrayList<Education> education = user.getEducation();
    if (education == null) {
      education = new ArrayList<Education>();
      user.setEducation(education);
    }

    int sid = 0;
    if (!education.isEmpty()) {
      sid = education.get(education.size() - 1).getSid() + 1;
    }
    newSchool.setSid(sid);

    JSONArray array = Education.toJSONArray(education);
    array.put(newSchool.toJSONObject());

    String sql = "UPDATE UserDetail set education = ? WHERE uid = ?";
    System.out.println("education: " + array);

    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      education.add(newSchool);
      return sid;
    } catch (Exception e) {
      return -1;
    }
  }

  public boolean updateSchoolInfo(User user, int sid, Education school) {
    if (user == null || sid < 0 || school == null) {
      return false;
    }

    ArrayList<Education> education = user.getEducation();
    if (education.isEmpty()) {
      return false;
    }

    int index = -1;
    for (index = 0; index < education.size(); index++) {
      if (education.get(index).getSid() == sid) {
        break;
      }
    }

    if (index < 0 || index == education.size()) {
      return false;
    }

    Education copy = new Education(education.get(index));
    education.set(index, school);

    JSONArray array = Education.toJSONArray(education);

    String sql = "UPDATE UserDetail set education = ? WHERE uid = ?";
    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      return true;
    } catch (Exception e) {
      // Rollback local education info.
      education.set(index, copy);
      return false;
    }
  }
  public boolean deleteSchoolInfo(User user, int sid) {
    if (user == null || sid < 0) {
      return false;
    }

    ArrayList<Education> education = user.getEducation();
    if (education.isEmpty()) {
      return false;
    }

    int index = -1;
    for (index = 0; index < education.size(); index++) {
      if (education.get(index).getSid() == sid) {
        break;
      }
    }

    if (index < 0 || index == education.size()) {
      return false;
    }

    Education removed = education.remove(index);
    JSONArray array = Education.toJSONArray(education);

    String sql = "UPDATE UserDetail set education = ? WHERE uid = ?";
    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      return true;
    } catch (Exception e) {
      // Rollback local education info.
      education.add(index, removed);
      return false;
    }
  }

  // ------------------------------- Work ---------------------------------- //
  public int addCompanyInfo(User user, Work newCompany) {
    if (user == null || newCompany == null) {
      return -1;
    }

    ArrayList<Work> work = user.getWork();
    if (work == null) {
      work = new ArrayList<Work>();
      user.setWork(work);
    }

    int cid = 0;
    if (!work.isEmpty()) {
      cid = work.get(work.size() - 1).getCid() + 1;
    }
    newCompany.setCid(cid);

    JSONArray array = Work.toJSONArray(work);
    array.put(newCompany.toJSONObject());

    String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";
    System.out.println("work: " + array);

    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      work.add(newCompany);
      return cid;
    } catch (Exception e) {
      return -1;
    }
  }

  public boolean updateCompanyInfo(User user, int cid, Work company) {
    if (user == null || cid < 0 || company == null) {
      return false;
    }

    ArrayList<Work> work = user.getWork();
    if (work.isEmpty()) {
      return false;
    }

    int index = -1;
    for (index = 0; index < work.size(); index++) {
      if (work.get(index).getCid() == cid) {
        break;
      }
    }

    if (index < 0 || index == work.size()) {
      return false;
    }

    Work copy = new Work(work.get(index));
    work.set(index, company);

    JSONArray array = Work.toJSONArray(work);

    String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";
    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      return true;
    } catch (Exception e) {
      // Rollback local work info.
      work.set(index, copy);
      return false;
    }
  }
  public boolean deleteCompanyInfo(User user, int cid) {
    if (user == null || cid < 0) {
      return false;
    }

    ArrayList<Work> work = user.getWork();
    if (work.isEmpty()) {
      return false;
    }

    int index = -1;
    for (index = 0; index < work.size(); index++) {
      if (work.get(index).getCid() == cid) {
        break;
      }
    }

    if (index < 0 || index == work.size()) {
      return false;
    }

    Work removed = work.remove(index);
    JSONArray array = Work.toJSONArray(work);

    String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";
    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      return true;
    } catch (Exception e) {
      // Rollback local work info.
      work.add(index, removed);
      return false;
    }
  }

  // ------------------------------ Places --------------------------------- //
  public int addPlaceInfo(User user, Place newPlace) {
    if (user == null || newPlace == null) {
      return -1;
    }

    ArrayList<Place> places = user.getPlaces();
    if (places == null) {
      places = new ArrayList<Place>();
      user.setPlaces(places);
    }

    int Pid = 0;
    if (!places.isEmpty()) {
      Pid = places.get(places.size() - 1).getPid() + 1;
    }
    newPlace.setPid(Pid);

    JSONArray array = Place.toJSONArray(places);
    array.put(newPlace.toJSONObject());

    String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";
    // System.out.println("places: " + array);

    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      places.add(newPlace);
      return Pid;
    } catch (Exception e) {
      return -1;
    }
  }

  public boolean updatePlaceInfo(User user, int pid, Place Place) {
    if (user == null || pid < 0 || Place == null) {
      return false;
    }

    ArrayList<Place> places = user.getPlaces();
    if (places.isEmpty()) {
      return false;
    }

    int index = -1;
    for (index = 0; index < places.size(); index++) {
      if (places.get(index).getPid() == pid) {
        break;
      }
    }

    if (index < 0 || index == places.size()) {
      return false;
    }

    Place copy = new Place(places.get(index));
    places.set(index, Place);

    JSONArray array = Place.toJSONArray(places);

    String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";
    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      return true;
    } catch (Exception e) {
      // Rollback local Place info.
      places.set(index, copy);
      return false;
    }
  }
  public boolean deletePlaceInfo(User user, int pid) {
    if (user == null || pid < 0) {
      return false;
    }

    ArrayList<Place> places = user.getPlaces();
    if (places.isEmpty()) {
      return false;
    }

    int index = -1;
    for (index = 0; index < places.size(); index++) {
      if (places.get(index).getPid() == pid) {
        break;
      }
    }

    if (index < 0 || index == places.size()) {
      return false;
    }

    Place removed = places.remove(index);
    JSONArray array = Place.toJSONArray(places);

    String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";
    try {
      this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
      return true;
    } catch (Exception e) {
      // Rollback local Place info.
      places.add(index, removed);
      return false;
    }
  }
}
