<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import = "bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
  // Get Viewer from session (current active user).
  User viewer = (User)session.getAttribute("user");
  String viewer_username = viewer.getUsername();
  int viewer_uid = viewer.getUid();

  // Find user of this page (true owner). If not specified, then user is viewer.
  User user;
  String username = request.getParameter("username");
  int uid;
  if (username == null) {
    user = viewer;
    username = viewer_username;
    uid = viewer_uid;
  } else {
    user = User.GetUserByUsername(username);
    if (user != null) {
      uid = user.getUid();
    } else {
      user = viewer;
      username = viewer_username;
      uid = viewer_uid;
    }
  }
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya User Profile</title>
</head>

<body uid="<%= uid %>" user="<%= username %>" viewer_id="<%= viewer_uid %>" viewer="<%= viewer_username %>" >
<nav-bar id="navbar" :uid=<%= uid %> :username="'<%= username %>'" :search-box=true></nav-bar>

<div class="profile-page-main-container">
  <div class="profile-container">
    <div class="overall-title">
      <i class="fa fa-user profile-icon"></i><span> About</span>
    </div>
    <profile id="profile" :uid=<%= uid %> :username="'<%= username %>'"></profile>
  </div>
</div>

</body>

<script src="dist/js/profile.dist.js"></script>
</html>
