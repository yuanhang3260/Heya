<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import = "bean.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
  String uid = (String)request.getAttribute("uid");
  String username = (String)request.getAttribute("username");

  String viewerUid = (String)request.getAttribute("viewerUid");
  String viewerUsername = (String)request.getAttribute("viewerUsername");

  boolean editable = uid.equals(viewerUid) && username.equals(viewerUsername);
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya User Profile</title>
</head>

<body uid="<%= uid %>" user="<%= username %>" viewer_id="<%= viewerUid %>" viewer="<%= viewerUsername %>" >
<nav-bar id="navbar" :search-box=true :notification=true :uid="'<%= viewerUid %>'" :username="'<%= viewerUsername %>'"></nav-bar>

<div class="profile-page-main-container">
  <div class="profile-container">
    <div class="overall-title">
      <i class="fa fa-user profile-icon"></i><span> About</span>
    </div>
    <profile id="profile" :uid="'<%= uid %>'" :username="'<%= username %>'" :editable="<%= editable %>"></profile>
  </div>
</div>

</body>

<script src="dist/js/profile.dist.js"></script>
</html>
