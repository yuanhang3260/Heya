<!DOCTYPE html>
<!-- <%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import = "bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
  // Get Viewer from session (current active user).
  User viewer = (User)session.getAttribute("user");
  int viewer = user.getUsername();
  int viewer_uid = user.getUid();

  // Find user of this page (true owner). If not specified, then user is viewer.
  User user;
  String username = request.getParameter("username");
  String uid;
  if (username == null) {
    user = viewer;
    username = viewer;
    uid = viewer_uid;
  } else {
    User user = User.GetUserByUsername(username);
    if (user != null) {
      uid = user.getUid();
    } else {
      user = viewer;
      username = viewer;
      uid = viewer_uid;
    }
  }
%> -->

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya User Profile</title>
  <link rel="stylesheet" href="css/bootstrap.css">
  <link rel="stylesheet" href="css/profile.css">
  <!-- <link rel="stylesheet" href="css/font-awesome.min.css"> -->
  <script defer src="https://use.fontawesome.com/releases/v5.0.6/js/all.js"></script>
</head>

<body uid="<%= uid %>" username="<%= username %>" viewer="<%= viewer %>" viewer="<%= viewer_uid %>" >
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

<div class="profile-main-container">
  <div class="profile-container">
    <div class="overall-title">
      <i class="fas fa-user profile-icon"></i><span> About</span>
    </div>
    <div class="profile-view-container">
      <div class="profile-menu">
        <ul>
          <li class="profile-basic-item">Basic Infomation</li>
          <li class="profile-education-item">Education</li>
          <li class="profile-work-item">Work</li>
          <li class="profile-places-item">Places Lived</li>
          <li class="profile-other-item">Other</li>
        </ul>
      </div>
      <div class="profile-panel">
        <div class="profile-basic-panel">
          <ul class="basic-info-display">
            <li class="name-info">
              <i class="basic-info-icon far fa-user"></i>
              <span>Snoopy</span>
            </li>
            <li class="email-info">
              <i class="basic-info-icon fa fa-envelope"></i>
              <span>snoopy@qq.com</span>
            </li>
            <li class="phone-info">
              <i class="basic-info-icon icon-phone fas fa-mobile-alt"></i>
              <span>4123203825</span>
            </li>
            <li class="birth-info">
              <i class="basic-info-icon icon-birth fas fa-birthday-cake"></i>
              <span>03/26/1991</span>
            </li>
            <div class="edit-button">
              <i class="fa fa-edit"></i><span> Edit profile basic info</span>
            </div>
          </ul>
          <div class="basic-info-edit">
            <form method="post" action="signup">
              <div class="form-group">
                <label class="profile-edit-label">Name</label>
                <input type="text" name="name" class="form-control profile-edit-input">
              </div>
              <div class="form-group">
                <label class="profile-edit-label">Email</label>
                <input type="text" name="email" class="form-control profile-edit-input">
              </div>
              <div class="form-group">
                <label class="profile-edit-label">Phone</label>
                <input type="text" name="phone" class="form-control profile-edit-input">
              </div>
              <div class="form-group">
                <label class="profile-edit-label">Birth</label>
                <input type="text" name="birth" class="form-control profile-edit-input">
              </div>
              <div class="button-box">
                <button type="button" class="btn btn-success save-btn mb-3">Save Changes</button>
                <button type="button" class="btn btn-light cancel-btn mb-3">Cancel</button>
              <div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

</body>

<script src="js/require.js" data-main="js/profile_main"></script>
</html>
