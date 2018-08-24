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
import org.apache.log4j.Logger;
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
import bean.UserPlace;
import bean.UserWork;
import util.UuidUtils;

@Repository("UserDAO")
public class UserDAO {
  private static final Logger log = Logger.getLogger(UserDAO.class);

  @Autowired
  private SessionFactory sessionFactory;

  private Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  public void checkUser(String username, String password) {
    Query query = getSession().createQuery(
        " from User where username = ? and password = ?");
    query.setString(0, username);
    query.setString(1, password);
    List<User> re = query.list();
    if (!re.isEmpty()) {
      log.error("SQL injected");
    }
  }

  public User getUserByUsername(String username) {
    Query query = getSession().createQuery(" from User where username = ?");
    query.setString(0, username);
    // query.setCacheable(true);
    return (User)query.uniqueResult();
  }

  public List<User> searchUser(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
      return new ArrayList<User>();
    }

    Query query = getSession().createQuery(" from User where username like '%" + keyword + "%'");
    return (List<User>)query.list();
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
    query.setCacheable(true);
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

  public String updateUserEducationInfo(
      User user, String sid, UserEducation newUserEducation) {
    if (user == null || sid == null || newUserEducation == null) {
      return null;
    }

    Education education = newUserEducation.getEducation();

    Set<UserEducation> allUserEducation = user.getUserEducation();
    String updatedSid = sid;
    for (UserEducation ue : allUserEducation) {
      Education e = ue.getEducation();
      if (e.getSid().equals(sid)) {
        if (!e.getSchool().equals(education.getSchool())) {
          // School name changed. Look up from table School by name.
          Query query =
              getSession().createQuery(" from Education where name = ?");
          query.setString(0, education.getSchool());
          query.setCacheable(true);
          Education result = (Education)query.uniqueResult();
          if (result == null) {
            // New school.
            education.setSid(UuidUtils.compressedUuid());
            getSession().save(education);
            result = education;
          }
          updatedSid = result.getSid();

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
    return updatedSid;
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

  // ----------------------------- Work ------------------------------- //
  public String addUserWorkInfo(User user,
                                     UserWork newUserWork) {
    if (user == null || newUserWork == null) {
      return null;
    }

    Work work = newUserWork.getWork();

    Query query = getSession().createQuery(" from Work where name = ?");
    query.setString(0, work.getCompany());
    query.setCacheable(true);
    Work result = (Work)query.uniqueResult();
    if (result == null) {
      // Add new company.
      work.setCid(UuidUtils.compressedUuid());
      getSession().save(work);
    } else {
      newUserWork.setWork(result);
    }

    user.getUserWork().add(newUserWork);
    getSession().update(user);
    return newUserWork.getWork().getCid();
  }

  public String updateUserWorkInfo(
      User user, String cid, UserWork newUserWork) {
    if (user == null || cid == null || newUserWork == null) {
      return null;
    }

    Work work = newUserWork.getWork();

    Set<UserWork> allUserWork = user.getUserWork();
    String updatedCid = cid;
    for (UserWork ue : allUserWork) {
      Work e = ue.getWork();
      if (e.getCid().equals(cid)) {
        if (!e.getCompany().equals(work.getCompany())) {
          // Company name changed. Look up from table Company by name.
          Query query =
              getSession().createQuery(" from Work where name = ?");
          query.setString(0, work.getCompany());
          query.setCacheable(true);
          Work result = (Work)query.uniqueResult();
          if (result == null) {
            // New company.
            work.setCid(UuidUtils.compressedUuid());
            getSession().save(work);
            result = work;
          }
          updatedCid = result.getCid();

          // update UserWork relation table.
          ue.mergeFrom(newUserWork);
          ue.setWork(result);
          getSession().update(ue);
        } else {
          ue.mergeFrom(newUserWork);
          getSession().update(ue);
        }
        break;
      }
    }
    return updatedCid;
  }

  public boolean deleteUserWorkInfo(User user, String cid) {
    if (user == null || cid == null) {
      return false;
    }

    Set<UserWork> allUserWork = user.getUserWork();
    for (UserWork ue : allUserWork) {
      if (ue.getWork().getCid().equals(cid)) {
        allUserWork.remove(ue);
        getSession().update(user);
        getSession().delete(ue);
        break;
      }
    }
    return true;
  }

  // ----------------------------- Place ------------------------------- //
  public String addUserPlaceInfo(User user,
                                 UserPlace newUserPlace) {
    if (user == null || newUserPlace == null) {
      return null;
    }

    Place place = newUserPlace.getPlace();

    Query query = getSession().createQuery(" from Place where name = ?");
    query.setString(0, place.getName());
    query.setCacheable(true);
    Place result = (Place)query.uniqueResult();
    if (result == null) {
      // Add new company.
      place.setPid(UuidUtils.compressedUuid());
      getSession().save(place);
    } else {
      newUserPlace.setPlace(result);
    }

    user.getUserPlaces().add(newUserPlace);
    // TODO: Update user or update UserPlace?
    getSession().update(user);
    return newUserPlace.getPlace().getPid();
  }

  public String updateUserPlaceInfo(
      User user, String pid, UserPlace newUserPlace) {
    if (user == null || pid == null || newUserPlace == null) {
      return null;
    }

    Place place = newUserPlace.getPlace();

    Set<UserPlace> allUserPlaces = user.getUserPlaces();
    String updatedPid = pid;
    for (UserPlace up : allUserPlaces) {
      Place e = up.getPlace();
      if (e.getPid().equals(pid)) {
        if (!e.getName().equals(place.getName())) {
          // Name name changed. Look up from table Name by name.
          Query query =
              getSession().createQuery(" from Place where name = ?");
          query.setString(0, place.getName());
          query.setCacheable(true);
          Place result = (Place)query.uniqueResult();
          if (result == null) {
            // New company.
            place.setPid(UuidUtils.compressedUuid());
            getSession().save(place);
            result = place;
          }
          updatedPid = result.getPid();

          // update UserPlace relation table.
          up.mergeFrom(newUserPlace);
          up.setPlace(result);
          getSession().update(up);
        } else {
          up.mergeFrom(newUserPlace);
          getSession().update(up);
        }
        break;
      }
    }
    return updatedPid;
  }

  public boolean deleteUserPlaceInfo(User user, String pid) {
    if (user == null || pid == null) {
      return false;
    }

    Set<UserPlace> allUserPlaces = user.getUserPlaces();
    for (UserPlace up : allUserPlaces) {
      if (up.getPlace().getPid().equals(pid)) {
        allUserPlaces.remove(up);
        getSession().update(user);
        getSession().delete(up);
        break;
      }
    }
    return true;
  }
}
