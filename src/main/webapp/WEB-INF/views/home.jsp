<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import = "bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
// Get Viewer from session (current active user).
  User viewer = (User)session.getAttribute("user");
  String viewer_username = viewer.getUsername();
  String viewer_uid = viewer.getUid();

  // Find user of this page (true owner). If not specified, then user is viewer.
  User user;
  String username = request.getParameter("username");
  String uid;
  user = viewer;
  username = viewer_username;
  uid = viewer_uid;
  // if (username == null) {
  //   user = viewer;
  //   username = viewer_username;
  //   uid = viewer_uid;
  // } else {
  //   user = User.GetUserByUsername(username);
  //   if (user != null) {
  //     uid = user.getUid();
  //   } else {
  //     user = viewer;
  //     username = viewer_username;
  //     uid = viewer_uid;
  //   }
  // }
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya</title>
</head>

<body uid="<%= uid %>" user="<%= username %>" viewer_id="<%= viewer_uid %>" viewer="<%= viewer_username %>" >
<nav-bar id="navbar" :search-box=true :width=1250></nav-bar>

<div class="home-main-container">
  <div class="home-left-bar">
    <avatar id="avatar" :uid="'<%= uid %>'" :username="'<%= username %>'"></avatar>
    <user-info id="userinfo" :uid="'<%= uid %>'" :username="'<%= username %>'"></user-info>
  </div>
</div>

<script type="text/javascript" src="dist/js/home.dist.js"></script></body>

</html>
