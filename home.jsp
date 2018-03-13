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
  <title>Heya</title>
  <link rel="stylesheet" href="css/bootstrap.css">
  <link rel="stylesheet" href="css/font-awesome.min.css">
  <link rel="stylesheet" href="css/home.css">
  <script src="js/font-awesome.5.0.6.min.js"></script>
</head>

<body uid="<%= uid %>" user="<%= username %>" viewer_id="<%= viewer_uid %>" viewer="<%= viewer_username %>" >
<nav class="fixed-top navbar heya-navbar">
  <div class="heya-navbar-container">
    <a class="navbar-brand heya-navbar-brand" href="#" tips="hei">
      <b>Heya</b> <i class="fa fa-leaf heya-logo"></i>
    </a>

    <form method="get" class="form-inline my-lg-0 heya-navbar-search" action="search">
      <div class="input-group">
        <input type="text" class="form-control heya-search-box" name="value" placeholder="Search">
        <div class="input-group-append">
          <button class="btn btn-outline-secondary heya-search-btn" type="submit">
            <i class="fa fa-search"></i>
          </button>
        </div>
      </div>
    </form>
  </div>
</nav>

<div class="home-main-container">
  <div class="home-left-bar">
    <div class="card home-profile-container">
      <img class="card-img-top cover-img" src="<%= user.getProfileCoverImg(request)%>" style="opacity: 0.7">
      <div id="profile-image" class="profile-img-container" style="background-image: url(<%= user.getProfileImg(request) %>)">
        <span></span>
      </div>
      <div class="card-body username-container"><%= user.getUsername()%></div>
      <div class="card-body follower-container">
        <div class="follower">
          <div class="follower-title">Followers</div>
          <div class="follower-num">12M</div>
        </div>
        <div class="follower-split-line"></div>
        <div class="follower">
          <div class="follower-title">Following</div>
          <div class="follower-num">235</div>
        </div>
      </div>
    </div>

    <div id="user-info" class="card home-profile-detail-container">
      <h6 class="profile-card-title">About <small>-</small> 
      <a href="profile?username=<%= username %>" class="profile-edit-button" >Edit</a></h6>
    </div>
  </div>
</div>

</body>

<script src="js/require.js" data-main="js/home_main"></script>
</html>
