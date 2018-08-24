<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<%@ page import = "bean.*,java.util.*,controller.ProfileImage"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
  String uid = (String)request.getAttribute("uid");
  String username = (String)request.getAttribute("username");

  String viewerUid = (String)request.getAttribute("viewerUid");
  String viewerUsername = (String)request.getAttribute("viewerUsername");

  List<User> matchedUsers = (List<User>)request.getAttribute("matchedUsers");
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya User Profile</title>
</head>

<body uid="<%= uid %>" user="<%= username %>" viewer_id="<%= viewerUid %>" viewer="<%= viewerUsername %>" >
<nav-bar id="navbar" :search-box=true :notification=true :uid="'<%= viewerUid %>'" :username="'<%= viewerUsername %>'" :width="1250" ></nav-bar>

<div class="search-main-container">
  <div class="search-left-container"></div>

  <div class="search-middle-container">
    <div class="search-result-container">
      <div class="search-result-header">
        <i class="fa fa-user-circle"></i>
        <span> Users</span>
      </div>

      <c:forEach items="${matchedUsers}" var="item">
        <div class="search-result-item">
          <a class="avatar-link" href="home?username=${item.username}" target="_blank">
            <img class="search-result-avatar" src="profileimage/profile/${item.username}" />
          </a>
          <div class="search-result-text">
            <a href="home?username=${item.username}" target="_blank">${item.username}</a>
          </div>
        </div>
      </c:forEach>

    </div>
  </div>

  <div class="search-right-container"></div>
</div>

</body>

<script src="dist/js/search.dist.js"></script>
</html>
