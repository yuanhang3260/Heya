package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;;
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

import bean.ChatDialog;
import bean.ChatMessage;
import bean.User;
import util.UuidUtils;

@Repository("ChatDAO")
public class ChatDAO {
  private static final Logger log = Logger.getLogger(ChatDAO.class);
  private static StringComparator strComparator = new StringComparator();

  @Autowired
  private SessionFactory sessionFactory;

  private Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  private static class StringComparator implements Comparator<String> {
    @Override
    public int compare(String name1, String name2) {
      int l1 = name1.length();
      int l2 = name2.length();
      int L = Math.min(l1, l2);
      for (int i = 0; i < L; i++) {
        char c1 = name1.charAt(i);
        char c2 = name2.charAt(i);
        if (c1 < c2) {
          return -1;
        } else if (c1 > c2) {
          return 1;
        }
      }
      // Two strings have same prefix - the longer one wins.
      if (l1 < l2) {
        return -1;
      } else if (l1 > l2) {
        return 1;
      } else {
        return 0;
      }
    }
  }

  private class UserPair {
    public String username1;
    public String username2;

    public UserPair(String username1, String username2) {
      if (ChatDAO.strComparator.compare(username1, username2) < 0) {
        this.username1 = username1;
        this.username2 = username2;
      } else {
        this.username1 = username2;
        this.username2 = username1;
      }
    }
  }

  // Get a dialog. Note this method is private and not transactional, to avoid nested transaction
  // when called by other methods.
  private ChatDialog getDialog(String username1, String username2) {
    UserPair pair = new UserPair(username1, username2);

    Query query = getSession().createQuery(
        " from ChatDialog where username1 = ? and username2 = ?");
    query.setString(0, pair.username1);
    query.setString(1, pair.username2);

    return (ChatDialog)query.uniqueResult();
  }

  // Get or create a dialog.
  private ChatDialog getOrCreateDialog(String username1, String username2) {
    UserPair pair = new UserPair(username1, username2);

    Query query = getSession().createQuery(
        " from ChatDialog where username1 = ? and username2 = ?");
    query.setString(0, pair.username1);
    query.setString(1, pair.username2);

    ChatDialog dialog = (ChatDialog)query.uniqueResult();
    if (dialog == null) {
      dialog = new ChatDialog();
      dialog.setDialogId(UuidUtils.compressedUuid());
      dialog.setUsername1(pair.username1);
      dialog.setUsername2(pair.username2);
      Date date = new Date();
      dialog.setUser1ReadTime(date);
      dialog.setUser2ReadTime(date);
      getSession().saveOrUpdate(dialog);
    }

    return dialog;
  }

  public boolean AddChatMessage(String from, String to, String content, Date timestamp) {
    ChatDialog dialog = this.getOrCreateDialog(from, to);
    if (dialog == null) {
      return false;
    }

    ChatMessage message = new ChatMessage();
    message.setDialogId(dialog.getDialogId());
    message.setMessageId(UuidUtils.compressedUuid());
    message.setUsername(from);
    message.setContent(content);
    message.setCreateTime(new Date());  // use the passed in timestamp arg?
    getSession().saveOrUpdate(message);
    return true;
  }

  public boolean updateReadTime(String from, String to, Date timestamp) {
    ChatDialog dialog = this.getOrCreateDialog(from, to);
    if (dialog == null) {
      return false;
    }

    if (to.equals(dialog.getUsername1())) {
      dialog.setUser1ReadTime(timestamp);
    } else {
      dialog.setUser2ReadTime(timestamp);
    }
    getSession().saveOrUpdate(dialog);
    return true;
  }
}
