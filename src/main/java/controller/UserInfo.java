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
import util.JsonUtils;

@Controller
@RequestMapping("/userinfo/{username}")
public class UserInfo {

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
      user = User.GetUserByUsername(username);
    }

    if (user != null) {
      user.getUserDetailInfo();
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
  public void basicUserInfo(HttpServletRequest request,
                            HttpServletResponse response,
                            HttpSession session,
                            @PathVariable("username") String username,
                            @RequestParam("name") String name,
                            @RequestParam("email") String email,
                            @RequestParam("phone") String phone,
                            @RequestParam("birth") JSONObject birthJSON) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (email == null) {
      JsonUtils.WriteJSONResponse(response, false, "email is required");
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

    if (user.updateUserBasicInfo(name, email, phone, birth)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "server data update error");
    }
  }

  @RequestMapping(value="/education", method=RequestMethod.POST)
  public void addNewSchool(HttpServletResponse response,
                           HttpSession session,
                           @PathVariable("username") String username,
                           Education education) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (education.getSchool() == null) {
      JsonUtils.WriteJSONResponse(response, false, "school is required");
      return;
    }

    Map<String, Object> result = new HashMap<String, Object>();
    int sid = user.addSchoolInfo(education);
    if (sid >= 0) {
      result.put("schoolId", (Integer)sid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to add school");
    }
  }

  @RequestMapping(value="/education/{sid}", method=RequestMethod.PUT)
  public void updateSchool(HttpServletResponse response,
                           HttpSession session,
                           @PathVariable("username") String username,
                           @PathVariable("sid") Integer sid,
                           Education education) {
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

    if (user.updateSchoolInfo(sid, education)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false,
                                  "Failed to update school info");
    }
  }

  @RequestMapping(value="/education/{sid}", method=RequestMethod.DELETE)
  public void deleteSchool(HttpServletResponse response,
                           HttpSession session,
                           @PathVariable("username") String username,
                           @PathVariable("sid") Integer sid) {
    if (sid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid sid " + sid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (user.deleteSchoolInfo(sid)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to delete school");
    }
  }

  @RequestMapping(value="/work", method=RequestMethod.POST)
  public void addNewWork(HttpServletResponse response,
                         HttpSession session,
                         @PathVariable("username") String username,
                         Work work) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (work.getCompany() == null) {
      JsonUtils.WriteJSONResponse(response, false, "company is required");
      return;
    }

    Map<String, Object> result = new HashMap<String, Object>();
    int cid = user.addCompanyInfo(work);
    if (cid >= 0) {
      result.put("companyId", (Integer)cid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to add company");
    }
  }

  @RequestMapping(value="/work/{cid}", method=RequestMethod.PUT)
  public void updateWork(HttpServletResponse response,
                         HttpSession session,
                         @PathVariable("username") String username,
                         @PathVariable("cid") Integer cid,
                         Work work) {
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

    if (user.updateCompanyInfo(cid, work)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false,
                                  "Failed to update company info");
    }
  }

  @RequestMapping(value="/work/{cid}", method=RequestMethod.DELETE)
  public void deleteCompany(HttpServletResponse response,
                            HttpSession session,
                            @PathVariable("username") String username,
                            @PathVariable("cid") Integer cid) {
    if (cid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid cid " + cid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (user.deleteCompanyInfo(cid)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to delete company");
    }
  }

  @RequestMapping(value="/places", method=RequestMethod.POST)
  public void addPlace(HttpServletResponse response,
                       HttpSession session,
                       @PathVariable("username") String username,
                       Place place) {
    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (place.getPlace() == null) {
      JsonUtils.WriteJSONResponse(response, false, "place is required");
      return;
    }

    Map<String, Object> result = new HashMap<String, Object>();
    int pid = user.addPlaceInfo(place);
    if (pid >= 0) {
      result.put("placeId", (Integer)pid);
      JsonUtils.WriteJSONResponse(response, result);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to add company");
    }
  }

  @RequestMapping(value="/places/{pid}", method=RequestMethod.PUT)
  public void updatePlace(HttpServletResponse response,
                          HttpSession session,
                          @PathVariable("username") String username,
                          @PathVariable("pid") Integer pid,
                          Place place) {
    if (pid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid pid " + pid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (place.getPlace() == null) {
      JsonUtils.WriteJSONResponse(response, false, "place is required");
      return;
    }

    if (user.updatePlaceInfo(pid, place)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false,
                                  "Failed to update Place info");
    }
  }

  @RequestMapping(value="/places/{pid}", method=RequestMethod.DELETE)
  public void deletePlace(HttpServletResponse response,
                          HttpSession session,
                          @PathVariable("username") String username,
                          @PathVariable("pid") Integer pid) {
    if (pid == null) {
      JsonUtils.WriteJSONResponse(response, false, "invalid pid " + pid);
      return;
    }

    User user = getAuthorizedUser(session, username);
    if (user == null) {
      JsonUtils.WriteJSONResponse(response, false, "permission denied");
      return;
    }

    if (user.deletePlaceInfo(pid)) {
      JsonUtils.WriteJSONResponse(response, true, null);
    } else {
      JsonUtils.WriteJSONResponse(response, false, "Failed to delete place");
    }
  }
}
