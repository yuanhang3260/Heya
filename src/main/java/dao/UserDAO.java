package dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import bean.Education;
import bean.Work;
import bean.Place;
import bean.User;

@Repository("UserDAO")
public class UserDAO {

  @Autowired
  private JdbcTemplate jdbcTemplate;

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

  // public User GetUserByUid(int uid) {
  //   Connection conn = null;
  //   PreparedStatement stmt = null;
  //   ResultSet rset = null;
  //   try {
  //     conn = DBManager.getDBConnection();
  //     conn.setAutoCommit(false);

  //     String sqlStr = "SELECT * from Users where uid = ?";
  //     stmt = conn.prepareStatement(sqlStr);
  //     stmt.setInt(1, uid);
  
  //     rset = stmt.executeQuery();
  //     if (rset.next()) {
  //       User user = new User();
  //       user.setUid(rset.getInt("uid"));
  //       user.setUsername(rset.getString("username"));
  //       user.setEmail(rset.getString("email"));
  //       user.setPassword(rset.getString("password"));
  //       return user;
  //     } else {
  //       return null;
  //     }
  //   } catch (SQLException e) {
  //     e.printStackTrace();
  //     return null;
  //   } finally {
  //     try {
  //       if (rset != null) {
  //         rset.close();
  //       }
  //       if (stmt != null) {
  //         stmt.close();
  //       }
  //     }
  //     catch (SQLException ex) {
  //       ex.printStackTrace();
  //     }
  //   }
  // }
}
