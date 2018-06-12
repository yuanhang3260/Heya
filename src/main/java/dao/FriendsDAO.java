package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import bean.User;
import util.UuidUtils;

@Repository("FriendsDAO")
public class FriendsDAO {
  private static final Logger log = Logger.getLogger(FriendsDAO.class);

  @Autowired
  private JdbcTemplate jdbcTpl;

  private static class CustomerRowMapperByUser1 implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setUid(rs.getString("uid_2"));
      user.setUsername(rs.getString("username_2"));
      return user;
    }
  }

  private static class CustomerRowMapperByUser2 implements RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setUid(rs.getString("uid_1"));
      user.setUsername(rs.getString("username_1"));
      return user;
    }
  }

  public List<User> getFriends(String uid) {
    ArrayList<User> friends;

    String sql = "select uid_2, username_2 from Friends where uid_1 = ?";
    List<User> result = this.jdbcTpl.query(sql, new CustomerRowMapperByUser1(), new Object[]{uid});

    sql = "select uid_1, username_1 from Friends where uid_2 = ?";
    List<User> result2 = this.jdbcTpl.query(sql, new CustomerRowMapperByUser2(), new Object[]{uid});

    result.addAll(result2);
    return result;
  }
}
