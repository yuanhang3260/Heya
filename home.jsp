<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import = "bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
  User user = (User)session.getAttribute("user");
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya</title>
  <link rel="stylesheet" href="css/bootstrap.css">
  <link rel="stylesheet" href="css/font-awesome.min.css">
  <link rel="stylesheet" href="css/home.css">
</head>

<body>
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
      <div class="profile-img-container" style="background-image: url(<%= user.getProfileImg(request)%>)">
        <!-- <img class="profile-img" src="img/default-profile2.jpg"> -->
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

    <div class="card home-profile-detail-container">
      <h6 class="profile-card-title">About <small>-</small> <a href="#" class="profile-edit-button" >Edit</a></h6>
      <div class="user-profile-item">
        <i class="user-profile-icon fa fa-building-o"></i>
        <p class="user-profile-detail">
          Works at
          <a href="https://www.google.com/search?q=Google" target="_blank" class="user-profile-link">
            Google
          </a>
        </p>
      </div>
      <div class="user-profile-item">
        <i class="user-profile-icon user-education-icon fa fa-graduation-cap"></i>
        <p class="user-profile-detail">
          Graduated from
          <a href="https://www.google.com/search?q=Carnegie Mellon University" target="_blank" class="user-profile-link">
            Carnegie Mellon University
          </a>
        </p>
      </div>
      <div class="user-profile-item">
        <i class="user-profile-icon fa fa-home"></i>
        <p class="user-profile-detail">
          Lives in
          <a href="https://www.google.com/maps/search/Santa Clara, California" target="_blank" class="user-profile-link">
            Santa Clara
          </a>
        </p>
      </div>
      <div class="user-profile-item">
        <i class="user-profile-icon user-hometown-icon fa fa-map-marker"></i>
        <p class="user-profile-detail">
          From
          <a href="https://www.google.com/maps/search/Jiangsu,China" target="_blank" class="user-profile-link">
            Jiangsu, China
          </a>
        </p>
      </div>
    </div>
  </div>
</div>

</body>

<script src="js/require.js" data-main="js/home_main"></script>
</html>
