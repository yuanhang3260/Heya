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

import bean.FriendRequest;
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

  public boolean friendRequestIsSent(String username1, String username2) {
    String sql = "SELECT COUNT(*) FROM FriendRequest WHERE " +
                 "(username_1 = ? AND username_2 = ? AND status = \"PENDING\")";
    Integer result = (Integer)jdbcTpl.queryForObject(
        sql, new Object[]{username1, username2}, Integer.class);
    return result > 0;
  }

  private static class FriendRequestRowMapper implements RowMapper<FriendRequest> {
    public FriendRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
      FriendRequest request = new FriendRequest();
      request.setUsername1(rs.getString("username_1"));
      request.setUsername2(rs.getString("username_2"));
      request.setStatus(rs.getString("status"));
      request.setLastUpdate(rs.getLong("lastupdate"));
      return request;
    }
  }

  public List<List<FriendRequest>> getFriendNotifications(String username) {
    List<List<FriendRequest>> result = new ArrayList<List<FriendRequest>>();

    // Friend requests from others.
    String sql = "SELECT * FROM FriendRequest WHERE " +
                 "(username_2 = ? AND status = \"PENDING\")";

    List<FriendRequest> requests = jdbcTpl.query(
        sql, new FriendRequestRowMapper(), new Object[]{username});

    long timestampThreshold = (new Date()).getTime() - 1000 * 3600 * 24 * 14;
    // My requests that are accepted.
    sql = "SELECT * FROM " +
            "FriendRequest " +
          "WHERE " +
            "username_1 = ? AND " +
            "(status = \"ACCEPTED\" OR status = \"CONFIRMED\") AND " +
            "lastupdate > ?";

    List<FriendRequest> replies1 = jdbcTpl.query(
        sql, new FriendRequestRowMapper(), new Object[]{username, timestampThreshold});

    // Requests that I accepted.
    sql = "SELECT * FROM " +
            "FriendRequest " +
          "WHERE " +
            "username_2 = ? AND " +
            "(status = \"ACCEPTED\" OR status = \"CONFIRMED\") AND " +
            "lastupdate > ?";
    List<FriendRequest> replies2 = jdbcTpl.query(
        sql, new FriendRequestRowMapper(), new Object[]{username, timestampThreshold});

    result.add(requests);
    result.add(replies1);
    result.add(replies2);
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

    long time = (new Date()).getTime();
    sql = "INSERT INTO FriendRequest " +
          "(username_1, username_2, status, lastupdate) values (?, ?, ?, ?)";
    return jdbcTpl.update(sql, new Object[] {username1, username2, "PENDING", time}) == 1;
  }

  public boolean acceptFriend(String uid_1, String username1, String uid_2, String username2) {
    long time = (new Date()).getTime();
    String sql_1 = "UPDATE FriendRequest SET status = ?, lastupdate = ? " +
                   "WHERE (username_1 = ? AND username_2 = ?)";
    String sql_2 = "DELETE FROM FriendRequest WHERE (username_2 = ? AND username_1 = ?)";
    String sql_3 = "INSERT INTO Friends (uid_1, username_1, uid_2, username_2) values (?, ?, ?, ?)";
    return jdbcTpl.update(sql_1, new Object[] {"ACCEPTED", time, username1, username2}) > 0 &&
           jdbcTpl.update(sql_2, new Object[] {username1, username2}) >= 0 &&
           jdbcTpl.update(sql_3, new Object[] {uid_1, username1, uid_2, username2}) > 0;
  }

  public boolean ignoreFriendRequest(String username1, String username2) {
    String sql_1= "DELETE FROM FriendRequest WHERE (username_1 = ? AND username_2 = ?)";
    return jdbcTpl.update(sql_1, new Object[] {username1, username2}) > 0;
  }

  public boolean readNotifications(String username, long timestamp) {
    String sql = "UPDATE FriendRequest " +
                 "SET status = ? " +
                 "WHERE username_1 = ? AND lastupdate <= ?";
    return jdbcTpl.update(sql, new Object[] {"CONFIRMED", username, timestamp}) >= 0;
  }

  public boolean unFriend(String username1, String username2) {
    String sql_1 = "DELETE FROM Friends WHERE " +
                   "(username_1 = ? AND username_2 = ?) OR ((username_2 = ? AND username_1 = ?))";
    String sql_2 = "DELETE FROM FriendRequest WHERE " +
                   "(username_1 = ? AND username_2 = ?) OR ((username_2 = ? AND username_1 = ?))";
    return jdbcTpl.update(sql_1, new Object[] {username1, username2, username1, username2}) > 0 &&
           jdbcTpl.update(sql_2, new Object[] {username1, username2, username1, username2}) >= 0;
  }
}
