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

import bean.Post;
import bean.User;
import util.UuidUtils;

@Repository("PostDAO")
public class PostDAO {

  @Autowired
  private SessionFactory sessionFactory;

  private Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  public String addNewPost(String uid, Post post) {
    post.setPid(UuidUtils.compressedUuid());
    return post.getPid();
  }

  // public String addUserEducationInfo(User user,
  //                                    UserEducation newUserEducation) {
  //   if (user == null || newUserEducation == null) {
  //     return null;
  //   }

  //   Education education = newUserEducation.getEducation();

  //   Query query = getSession().createQuery(" from Education where name = ?");
  //   query.setString(0, education.getSchool());
  //   query.setCacheable(true);
  //   Education result = (Education)query.uniqueResult();
  //   if (result == null) {
  //     // Add new school.
  //     education.setSid(UuidUtils.compressedUuid());
  //     getSession().save(education);
  //   } else {
  //     newUserEducation.setEducation(result);
  //   }

  //   user.getUserEducation().add(newUserEducation);
  //   getSession().update(user);
  //   return newUserEducation.getEducation().getSid();
  // }
}