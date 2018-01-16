package db;

import java.sql.*;
import java.util.Properties;

public class DBManager {
  private static String dbDtriver = "com.mysql.jdbc.Driver";
  private static String database = "jdbc:mysql://localhost/Heya";
  private static String user = "hy";
  private static String password = "123456";
  private static Connection conn;

  public DBManager() {}

  public static Connection getDBConnection() throws SQLException {
    if (conn != null) {
      return conn;
    } else {
      try {
        Class.forName(DBManager.dbDtriver).newInstance();
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }

      Properties p = new Properties();
      p.put("user", DBManager.user);
      p.put("password", DBManager.password);
      conn = DriverManager.getConnection(DBManager.database, p);
      return conn;
    }
  }

  public static String buildSqlStringValue(String str) {
    return "\"" + str + "\"";
  }
}
