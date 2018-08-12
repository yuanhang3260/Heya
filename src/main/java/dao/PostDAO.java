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

  public List<Post> getPosts(String uid) {
    Query query = getSession().createQuery(" from Post where uid = ?");
    query.setString(0, uid);
    return query.list();
  }

  public Long getPostsCount(String uid) {
    Query query = getSession().createQuery("select count(*) from Post where uid = ?");
    query.setString(0, uid);
    return (Long)query.uniqueResult();
  }

  public String addNewPost(String uid, Post post) {
    // Generate complete Post object.
    post.setPid(UuidUtils.compressedUuid());
    post.setUid(uid);
    post.setCreateTime(new Date());

    getSession().save(post);
    return post.getPid();
  }

  public Post deletePost(String uid, String pid) {
    Query query =
        getSession().createQuery(" from Post where uid = ? and pid = ?");
    query.setString(0, uid);
    query.setString(1, pid);
    Post post = (Post)query.uniqueResult();
    if (post == null) {
      return null;
    }

    getSession().delete(post);
    return post;
  }
}