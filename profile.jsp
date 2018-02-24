<!DOCTYPE html>
<!-- <%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
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
%> -->

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya User Profile</title>
  <link rel="stylesheet" href="css/bootstrap.css">
  <link rel="stylesheet" href="css/profile.css">
  <!-- <link rel="stylesheet" href="css/font-awesome.min.css"> -->
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

<div class="profile-main-container">
  <div class="profile-container">
    <div class="overall-title">
      <i class="fas fa-user profile-icon"></i><span> About</span>
    </div>
    <div id="profile-container" class="profile-view-container">
      <div class="profile-menu">
        <ul>
          <li class="profile-basic-item item-selected">Basic Infomation</li>
          <li class="profile-education-item">Education</li>
          <li class="profile-work-item">Work</li>
          <li class="profile-places-item">Places Lived</li>
          <li class="profile-other-item">Other</li>
        </ul>
      </div>
      <div class="profile-panel">
        <div class="subpanel profile-basic-panel">
          <ul class="basic-info-display">
            <div class="edit-button">
              <i class="fa fa-edit"></i><span> Edit profile basic info</span>
            </div>
          </ul>
          <div class="profile-info-edit basic-info-edit">
            <form method="post">
              <div class="form-group edit-name">
                <label class="profile-edit-label">Name</label>
                <input type="text" name="name" class="form-control profile-edit-input">
              </div>
              <div class="form-group edit-birth">
                <label class="profile-edit-label">Birth</label>
                <select class="form-control profile-edit-select" name="year">
                </select>
                <select class="form-control profile-edit-select" name="month">
                </select>
                <select class="form-control profile-edit-select" name="date">
                </select>
              </div>
              <div class="form-group edit-email">
                <label class="profile-edit-label">Email</label>
                <input type="text" name="email" class="form-control profile-edit-input">
              </div>
              <div class="form-group edit-phone">
                <label class="profile-edit-label">Phone</label>
                <input type="text" name="phone" class="form-control profile-edit-input">
              </div>
              <div class="button-box">
                <button type="button" class="btn btn-success save-btn">Save Changes</button>
                <button type="button" class="btn btn-light cancel-btn">Cancel</button>
              </div>
              <div class="alert alert-danger update-error-msg" role="alert"></div>
            </form>
          </div>
        </div>

        <div class="subpanel profile-education-panel">
          <div class="add-new-item">
            <div class="add-item-button">
              <i class="far fa-plus-square add-item-icon"></i>
              <span class="add-item-text">Add a school</span>
            </div>
            <div class="profile-info-edit">
              <form method="post">
                <div class="form-group edit-school">
                  <label class="profile-edit-label">School</label>
                  <input type="text" name="school" class="form-control profile-edit-input">
                </div>
                <div class="form-group edit-year">
                  <label class="profile-edit-label"></label>
                  <select class="form-control profile-edit-select" name="start">
                  </select>
                  <span class="year-to">to</span>
                  <select class="form-control profile-edit-select" name="end">
                  </select>
                </div>
                <div class="form-group edit-major">
                  <label class="profile-edit-label">Major</label>
                  <input type="text" name="major" class="form-control profile-edit-input">
                </div>
                <div class="button-box">
                  <button type="button" class="btn btn-success save-btn">Save Changes</button>
                  <button type="button" class="btn btn-light cancel-btn">Cancel</button>
                </div>
                <div class="alert alert-danger update-error-msg" role="alert"></div>
              </form>
            </div>
            <div class="profile-info-display">
              <div class="profile-info">
                <a href="#" class="profile-name school-info" target="_blank"></a>
                <p class="profile-detail"></p>
              </div>
              <i class="fas fa-graduation-cap profile-icon"></i>
              <div class="corner-buttons">
                <i class="fas fa-edit profile-edit-button"></i>
                <i class="fas fa-ban profile-delete-button"></i>
              </div>
            </div>
          </div>
        </div>

        <div class="subpanel profile-work-panel">
          <div class="add-new-item">
            <div class="add-item-button">
              <i class="far fa-plus-square add-item-icon"></i>
              <span class="add-item-text">Add a company</span>
            </div>
            <div class="profile-info-edit">
              <form method="post">
                <div class="form-group edit-company">
                  <label class="profile-edit-label">Company</label>
                  <input type="text" name="company" class="form-control profile-edit-input">
                </div>
                <div class="form-group edit-year">
                  <label class="profile-edit-label"></label>
                  <select class="form-control profile-edit-select" name="start">
                  </select>
                  <span class="year-to">to</span>
                  <select class="form-control profile-edit-select" name="end">
                  </select>
                </div>
                <div class="form-group edit-position">
                  <label class="profile-edit-label">Position</label>
                  <input type="text" name="position" class="form-control profile-edit-input">
                </div>
                <div class="button-box">
                  <button type="button" class="btn btn-success save-btn">Save Changes</button>
                  <button type="button" class="btn btn-light cancel-btn">Cancel</button>
                </div>
                <div class="alert alert-danger update-error-msg" role="alert"></div>
              </form>
            </div>
            <div class="profile-info-display">
              <div class="profile-info">
                <a href="#" class="profile-name company-info" target="_blank"></a>
                <p class="profile-detail"></p>
              </div>
              <i class="fas fa-laptop profile-icon"></i>
              <div class="corner-buttons">
                <i class="fas fa-edit profile-edit-button"></i>
                <i class="fas fa-ban profile-delete-button"></i>
              </div>
            </div>
          </div>
        </div>
        
        <div class="subpanel profile-places-panel">
          <div class="critical-place-title">Current Place and Hometown</div>
          <div class="profile-info-box current-live">
            <div class="add-item-button">
              <i class="far fa-plus-square add-item-icon"></i>
              <span class="add-item-text">Add current place</span>
            </div>
            <div class="profile-info-edit">
              <form method="post">
                <div class="form-group edit-place">
                  <label class="profile-edit-label">Place</label>
                  <input type="text" name="company" class="form-control profile-edit-input">
                </div>
                <div class="button-box">
                  <button type="button" class="btn btn-success save-btn">Save Changes</button>
                  <button type="button" class="btn btn-light cancel-btn">Cancel</button>
                </div>
                <div class="alert alert-danger update-error-msg" role="alert"></div>
              </form>
            </div>
            <div class="profile-info-display">
              <div class="profile-info">
                <a href="#" class="profile-name place-info" target="_blank"></a>
                <p class="profile-detail">current live</p>
              </div>
              <i class="fas fa-map-marker-alt profile-icon"></i>
              <div class="corner-buttons">
                <i class="fas fa-edit profile-edit-button"></i>
                <i class="fas fa-ban profile-delete-button"></i>
              </div>
            </div>
          </div>
          <div class="profile-info-box hometown">
            <div class="add-item-button">
              <i class="far fa-plus-square add-item-icon"></i>
              <span class="add-item-text">Add hometown</span>
            </div>
            <div class="profile-info-edit">
              <form method="post">
                <div class="form-group edit-place">
                  <label class="profile-edit-label">Place</label>
                  <input type="text" name="company" class="form-control profile-edit-input">
                </div>
                <div class="button-box">
                  <button type="button" class="btn btn-success save-btn">Save Changes</button>
                  <button type="button" class="btn btn-light cancel-btn">Cancel</button>
                </div>
                <div class="alert alert-danger update-error-msg" role="alert"></div>
              </form>
            </div>
            <div class="profile-info-display">
              <div class="profile-info">
                <a href="#" class="profile-name place-info" target="_blank"></a>
                <p class="profile-detail">hometown</p>
              </div>
              <i class="fas fa-home profile-icon"></i>
              <div class="corner-buttons">
                <i class="fas fa-edit profile-edit-button"></i>
                <i class="fas fa-ban profile-delete-button"></i>
              </div>
            </div>
          </div>
        </div>
        
        <div class="subpanel profile-other-panel"></div>
      </div>
    </div>
  </div>
</div>

</body>

<script src="js/require.js" data-main="js/profile_main"></script>
</html>
