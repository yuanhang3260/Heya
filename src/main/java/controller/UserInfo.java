package controller;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.GregorianCalendar;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import application.SpringUtils;
import bean.Education;
import bean.Place;
import bean.Work;
import bean.User;
import bean.UserEducation;
import bean.UserPlace;
import bean.UserWork;
import dao.UserDAO;
import util.JsonUtils;

@Controller
@RequestMapping("/userinfo/{username}")
public class UserInfo {

  @Autowired
  UserDAO userDAO;

  /**
   *  GET userinfo handler.
   */
  @RequestMapping(value="", method=RequestMethod.GET)
  public void getUserInfo(HttpServletResponse response,
                          HttpSession session,
                          @PathVariable("username") String username) {
    User user;
    if (username == null) {
      user = (User)session.getAttribute("user");
    } else {
      user = this.userDAO.GetUserByUsername(username);
    }

    if (user != null) {
      JsonUtils.WriteJSONResponse(response, user.toJSONObject());
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Could not get user");
    }
  }

  private User getAuthorizedUser(HttpSession session, String username) {
    User user = (User)session.getAttribute("user");
    if (user != null && username != null &&
        username.equals(user.getUsername())) {
      return user;
    }
    return null;
  }

  @RequestMapping(value="/basic", method=RequestMethod.POST)
  public void basicUserInfo(
      HttpServletRequest request,
      HttpServletResponse response,
      HttpSession session,
      @PathVariable("username") String username,
      @RequestParam(value="email", required=true) String email,
      @RequestParam(value="name", required=false) String name,
      @RequestParam(value="phone", required=false) String phone,
      @RequestParam(value="birth", required=false) JSONObject birthJSON) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    Date birth = null;
    if (birthJSON != null) {
      try {
        birth = new GregorianCalendar(birthJSON.getInt("year"),
                                      birthJSON.getInt("month") - 1,  // hehe
                                      birthJSON.getInt("date")).getTime();
      } catch (JSONException e) {
        birth = null;
      }
    }

    if (this.userDAO.updateUserBasicInfo(
          user, name, email, phone, birth)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "server data update error");
    }
  }

  // ----------------------------- Education ------------------------------- //
  @RequestMapping(value="/education", method=RequestMethod.POST)
  public void addNewUserEducation(HttpServletResponse response,
                                  HttpSession session,
                                  @PathVariable("username") String username,
                                  Education education,
                                  UserEducation userEducation) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (education.getSchool() == null) {
      JsonUtils.WriteJSONResponse(response, false, "school is required");
      return;
    }

    userEducation.setEducation(education);
    userEducation.setUser(user);

    Map<String, Object> result = new HashMap<String, Object>();
    String sid = this.userDAO.addUserEducationInfo(user, userEducation);
    if (sid != null) {
      result.put("schoolId", (String)sid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to add school");
    }
  }

  @RequestMapping(value="/education/{sid}", method=RequestMethod.PUT)
  public void updateUserEducation(HttpServletResponse response,
                                  HttpSession session,
                                  @PathVariable("username") String username,
                                  @PathVariable("sid") String sid,
                                  Education education,
                                  UserEducation userEducation) {
    if (sid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid sid " + sid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (education.getSchool() == null) {
      JsonUtils.WriteJSONResponse(response, false, "school is required");
      return;
    }

    education.setSid(sid);
    userEducation.setEducation(education);
    userEducation.setUser(user);

    Map<String, Object> result = new HashMap<String, Object>();
    sid = this.userDAO.updateUserEducationInfo(user, sid, userEducation);
    if (sid != null) {
      result.put("schoolId", (String)sid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false,
                                  "Failed to update school info");
    }
  }

  @RequestMapping(value="/education/{sid}", method=RequestMethod.DELETE)
  public void deleteUserEducation(HttpServletResponse response,
                                  HttpSession session,
                                  @PathVariable("username") String username,
                                  @PathVariable("sid") String sid) {
    if (sid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid sid " + sid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (this.userDAO.deleteUserEducationInfo(user, sid)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to delete school");
    }
  }

  // -------------------------------- Work -------------------------------- //
  @RequestMapping(value="/work", method=RequestMethod.POST)
  public void addNewUserWork(HttpServletResponse response,
                             HttpSession session,
                             @PathVariable("username") String username,
                             Work work,
                             UserWork userWork) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (work.getCompany() == null) {
      JsonUtils.WriteJSONResponse(response, false, "company is required");
      return;
    }

    userWork.setWork(work);
    userWork.setUser(user);

    Map<String, Object> result = new HashMap<String, Object>();
    String cid = this.userDAO.addUserWorkInfo(user, userWork);
    if (cid != null) {
      result.put("companyId", (String)cid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to add company");
    }
  }

  @RequestMapping(value="/work/{cid}", method=RequestMethod.PUT)
  public void updateUserWork(HttpServletResponse response,
                             HttpSession session,
                             @PathVariable("username") String username,
                             @PathVariable("cid") String cid,
                             Work work,
                             UserWork userWork) {
    if (cid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid cid " + cid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (work.getCompany() == null) {
      JsonUtils.WriteJSONResponse(response, false, "company is required");
      return;
    }

    work.setCid(cid);
    userWork.setWork(work);
    userWork.setUser(user);

    Map<String, Object> result = new HashMap<String, Object>();
    cid = this.userDAO.updateUserWorkInfo(user, cid, userWork);
    if (cid != null) {
      result.put("companyId", (String)cid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false,
                                  "Failed to update company info");
    }
  }

  @RequestMapping(value="/work/{cid}", method=RequestMethod.DELETE)
  public void deleteUserWorkI(HttpServletResponse response,
                              HttpSession session,
                              @PathVariable("username") String username,
                              @PathVariable("cid") String cid) {
    if (cid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid cid " + cid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (this.userDAO.deleteUserWorkInfo(user, cid)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to delete company");
    }
  }

  // ------------------------------- Place -------------------------------- //
  @RequestMapping(value="/places", method=RequestMethod.POST)
  public void addNewUserPlace(HttpServletResponse response,
                              HttpSession session,
                              @PathVariable("username") String username,
                              Place place,
                              UserPlace userPlace) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (place.getName() == null) {
      JsonUtils.WriteJSONResponse(response, false, "place name is required");
      return;
    }

    userPlace.setPlace(place);
    userPlace.setUser(user);

    Map<String, Object> result = new HashMap<String, Object>();
    String pid = this.userDAO.addUserPlaceInfo(user, userPlace);
    if (pid != null) {
      result.put("placeId", (String)pid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to add place");
    }
  }

  @RequestMapping(value="/places/{pid}", method=RequestMethod.PUT)
  public void updateUserPlace(HttpServletResponse response,
                              HttpSession session,
                              @PathVariable("username") String username,
                              @PathVariable("pid") String pid,
                              Place place,
                              UserPlace userPlace) {
    if (pid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid pid " + pid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (place.getName() == null) {
      JsonUtils.WriteJSONResponse(response, false, "place is required");
      return;
    }

    place.setPid(pid);
    userPlace.setPlace(place);
    userPlace.setUser(user);

    Map<String, Object> result = new HashMap<String, Object>();
    pid = this.userDAO.updateUserPlaceInfo(user, pid, userPlace);
    if (pid != null) {
      result.put("placeId", (String)pid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false,
                                  "Failed to update place info");
    }
  }

  @RequestMapping(value="/places/{pid}", method=RequestMethod.DELETE)
  public void deleteUserPlaceInfo(HttpServletResponse response,
                                  HttpSession session,
                                  @PathVariable("username") String username,
                                  @PathVariable("pid") String pid) {
    if (pid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid pid " + pid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (this.userDAO.deleteUserPlaceInfo(user, pid)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to delete place");
    }
  }
}
