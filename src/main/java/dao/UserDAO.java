package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
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
import bean.UserEducation;
import util.UuidUtils;

@Repository("UserDAO")
public class UserDAO {

  @Autowired
  private SessionFactory sessionFactory;

  private Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  public User GetUserByUsername(String username) {
    Query query = getSession().createQuery(" from User where username = ?");
    query.setString(0, username);
    return (User)query.uniqueResult();
  }

  public String addNewUser(String username,
                           String email,
                           String password) {
    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setPassword(password);
    getSession().save(user);
    return user.getUid();
  }

  public boolean updateUserBasicInfo(
      User user, String name, String email, String phone, Date birth) {
    if (name != null) {
      user.setName(name);
    }
    if (email != null) {
      user.setEmail(email);
    }
    if (phone != null) {
      user.setPhone(phone);
    }
    if (birth != null) {
      user.setBirth(birth);
    }
    getSession().update(user);
    return true;
  }

  // ----------------------------- Education ------------------------------- //
  public String addUserEducationInfo(User user,
                                     UserEducation newUserEducation) {
    if (user == null || newUserEducation == null) {
      return null;
    }

    Education education = newUserEducation.getEducation();

    Query query = getSession().createQuery(" from Education where name = ?");
    query.setString(0, education.getSchool());
    Education result = (Education)query.uniqueResult();
    if (result == null) {
      // Add new school.
      education.setSid(UuidUtils.compressedUuid());
      getSession().save(education);
    } else {
      newUserEducation.setEducation(result);
    }

    user.getUserEducation().add(newUserEducation);
    getSession().update(user);
    return newUserEducation.getEducation().getSid();
  }

  public boolean updateUserEducationInfo(
      User user, String sid, UserEducation newUserEducation) {
    if (user == null || sid == null || newUserEducation == null) {
      return false;
    }

    Education education = newUserEducation.getEducation();

    Set<UserEducation> allUserEducation = user.getUserEducation();
    for (UserEducation ue : allUserEducation) {
      Education e = ue.getEducation();
      if (e.getSid().equals(sid)) {
        if (!e.getSchool().equals(education.getSchool())) {
          // School name changed. Look up from table School by name.
          Query query =
              getSession().createQuery(" from Education where name = ?");
          query.setString(0, education.getSchool());
          Education result = (Education)query.uniqueResult();
          if (result == null) {
            // New school.
            education.setSid(UuidUtils.compressedUuid());
            getSession().save(education);
            result = education;
          }
          // update UserEducation relation table.
          ue.mergeFrom(newUserEducation);
          ue.setEducation(result);
          getSession().update(ue);
        } else {
          ue.mergeFrom(newUserEducation);
          getSession().update(ue);
        }
        break;
      }
    }
    return true;
  }

  public boolean deleteUserEducationInfo(User user, String sid) {
    if (user == null || sid == null) {
      return false;
    }

    Set<UserEducation> allUserEducation = user.getUserEducation();
    for (UserEducation ue : allUserEducation) {
      if (ue.getEducation().getSid().equals(sid)) {
        allUserEducation.remove(ue);
        getSession().update(user);
        getSession().delete(ue);
        break;
      }
    }
    return true;
  }

  // // ------------------------------- Work ---------------------------------- //
  // public int addCompanyInfo(User user, Work newCompany) {
  //   if (user == null || newCompany == null) {
  //     return -1;
  //   }

  //   ArrayList<Work> work = user.getWork();
  //   if (work == null) {
  //     work = new ArrayList<Work>();
  //     user.setWork(work);
  //   }

  //   int cid = 0;
  //   if (!work.isEmpty()) {
  //     cid = work.get(work.size() - 1).getCid() + 1;
  //   }
  //   newCompany.setCid(cid);

  //   JSONArray array = Work.toJSONArray(work);
  //   array.put(newCompany.toJSONObject());

  //   String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";
  //   System.out.println("work: " + array);

  //   try {
  //     this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
  //     work.add(newCompany);
  //     return cid;
  //   } catch (Exception e) {
  //     return -1;
  //   }
  // }

  // public boolean updateCompanyInfo(User user, int cid, Work company) {
  //   if (user == null || cid < 0 || company == null) {
  //     return false;
  //   }

  //   ArrayList<Work> work = user.getWork();
  //   if (work.isEmpty()) {
  //     return false;
  //   }

  //   int index = -1;
  //   for (index = 0; index < work.size(); index++) {
  //     if (work.get(index).getCid() == cid) {
  //       break;
  //     }
  //   }

  //   if (index < 0 || index == work.size()) {
  //     return false;
  //   }

  //   Work copy = new Work(work.get(index));
  //   work.set(index, company);

  //   JSONArray array = Work.toJSONArray(work);

  //   String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";
  //   try {
  //     this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
  //     return true;
  //   } catch (Exception e) {
  //     // Rollback local work info.
  //     work.set(index, copy);
  //     return false;
  //   }
  // }
  // public boolean deleteCompanyInfo(User user, int cid) {
  //   if (user == null || cid < 0) {
  //     return false;
  //   }

  //   ArrayList<Work> work = user.getWork();
  //   if (work.isEmpty()) {
  //     return false;
  //   }

  //   int index = -1;
  //   for (index = 0; index < work.size(); index++) {
  //     if (work.get(index).getCid() == cid) {
  //       break;
  //     }
  //   }

  //   if (index < 0 || index == work.size()) {
  //     return false;
  //   }

  //   Work removed = work.remove(index);
  //   JSONArray array = Work.toJSONArray(work);

  //   String sql = "UPDATE UserDetail set work = ? WHERE uid = ?";
  //   try {
  //     this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
  //     return true;
  //   } catch (Exception e) {
  //     // Rollback local work info.
  //     work.add(index, removed);
  //     return false;
  //   }
  // }

  // // ------------------------------ Places --------------------------------- //
  // public int addPlaceInfo(User user, Place newPlace) {
  //   if (user == null || newPlace == null) {
  //     return -1;
  //   }

  //   ArrayList<Place> places = user.getPlaces();
  //   if (places == null) {
  //     places = new ArrayList<Place>();
  //     user.setPlaces(places);
  //   }

  //   int Pid = 0;
  //   if (!places.isEmpty()) {
  //     Pid = places.get(places.size() - 1).getPid() + 1;
  //   }
  //   newPlace.setPid(Pid);

  //   JSONArray array = Place.toJSONArray(places);
  //   array.put(newPlace.toJSONObject());

  //   String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";
  //   // System.out.println("places: " + array);

  //   try {
  //     this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
  //     places.add(newPlace);
  //     return Pid;
  //   } catch (Exception e) {
  //     return -1;
  //   }
  // }

  // public boolean updatePlaceInfo(User user, int pid, Place Place) {
  //   if (user == null || pid < 0 || Place == null) {
  //     return false;
  //   }

  //   ArrayList<Place> places = user.getPlaces();
  //   if (places.isEmpty()) {
  //     return false;
  //   }

  //   int index = -1;
  //   for (index = 0; index < places.size(); index++) {
  //     if (places.get(index).getPid() == pid) {
  //       break;
  //     }
  //   }

  //   if (index < 0 || index == places.size()) {
  //     return false;
  //   }

  //   Place copy = new Place(places.get(index));
  //   places.set(index, Place);

  //   JSONArray array = Place.toJSONArray(places);

  //   String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";
  //   try {
  //     this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
  //     return true;
  //   } catch (Exception e) {
  //     // Rollback local Place info.
  //     places.set(index, copy);
  //     return false;
  //   }
  // }
  // public boolean deletePlaceInfo(User user, int pid) {
  //   if (user == null || pid < 0) {
  //     return false;
  //   }

  //   ArrayList<Place> places = user.getPlaces();
  //   if (places.isEmpty()) {
  //     return false;
  //   }

  //   int index = -1;
  //   for (index = 0; index < places.size(); index++) {
  //     if (places.get(index).getPid() == pid) {
  //       break;
  //     }
  //   }

  //   if (index < 0 || index == places.size()) {
  //     return false;
  //   }

  //   Place removed = places.remove(index);
  //   JSONArray array = Place.toJSONArray(places);

  //   String sql = "UPDATE UserDetail set places = ? WHERE uid = ?";
  //   try {
  //     this.jdbcTemplate.update(sql, array.toString(2), user.getUid());
  //     return true;
  //   } catch (Exception e) {
  //     // Rollback local Place info.
  //     places.add(index, removed);
  //     return false;
  //   }
  // }
}
