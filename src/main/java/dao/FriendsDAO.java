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

  private static class CustomerRowMapperByUser1 implements RowMapper<User> {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setUid(rs.getString("uid_2"));
      user.setUsername(rs.getString("username_2"));
      return user;
    }
  }

  private static class CustomerRowMapperByUser2 implements RowMapper<User> {
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setUid(rs.getString("uid_1"));
      user.setUsername(rs.getString("username_1"));
      return user;
    }
  }

  public List<User> getFriends(String username) {
    ArrayList<User> friends;

    String sql = "SELECT uid_2, username_2 FROM Friends WHERE username_1 = ?";
    List<User> result = this.jdbcTpl.query(sql, new CustomerRowMapperByUser1(),
                                           new Object[]{username});

    sql = "SELECT uid_1, username_1 FROM Friends WHERE username_2 = ?";
    List<User> result2 = this.jdbcTpl.query(sql, new CustomerRowMapperByUser2(),
                                            new Object[]{username});

    result.addAll(result2);
    return result;
  }

  public boolean requestAddFriend(String username1, String username2) {
    // If they're already friends, return false.
    String sql = "SELECT COUNT(*) FROM Friends WHERE " +
                 "(username_1 = ? AND username_2 = ?) || (username_2 = ? AND username_1 = ?)";
    Integer result = (Integer)jdbcTpl.queryForObject(
        sql, new Object[]{username1, username2, username1, username2}, Integer.class);
    if (result > 0) {
      return false;
    }

    sql = "INSERT INTO FriendRequest (username_1, username_2, status) values (?, ?, ?)";
    return jdbcTpl.update(sql, new Object[] {username1, username2, "PENDING"}) == 1;
  }

  public boolean acceptFriend(String uid_1, String username1, String uid_2, String username2) {
    String sql_1 = "INSERT INTO Friends (uid_1, username_1, uid_2, username_2) values (?, ?, ?, ?)";
    String sql_2 = "UPDATE FriendRequest SET status = ? WHERE " +
                   "(username_1 = ? AND username_2 = ?) || (username_2 = ? AND username_1 = ?)";
    return jdbcTpl.update(sql_1, new Object[] {uid_1, username1, uid_2, username2}) > 0 &&
           jdbcTpl.update(
              sql_2, new Object[] {"ACCEPTED", username1, username2, username1, username2}) > 0;
  }

  public boolean unFriend(String username1, String username2) {
    String sql_1 = "DELETE FROM Friends WHERE " +
                   "(username_1 = ? AND username_2 = ?) || ((username_2 = ? AND username_1 = ?))";
    String sql_2 = "DELETE FROM FriendRequest WHERE " +
                   "(username_1 = ? AND username_2 = ?) || ((username_2 = ? AND username_1 = ?))";
    return jdbcTpl.update(sql_1, new Object[] {username1, username2, username1, username2}) > 0 &&
           jdbcTpl.update(sql_2, new Object[] {username1, username2, username1, username2}) >= 0;
  }
}
