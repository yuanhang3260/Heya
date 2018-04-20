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
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya</title>
</head>

<body uid="<%= uid %>" user="<%= username %>" viewer_id="<%= viewerUid %>" viewer="<%= viewerUsername %>" >
<nav-bar id="navbar" :search-box=true :width=1250></nav-bar>

<div class="home-main-container">
  <div class="home-left-bar">
    <avatar id="avatar" :uid="'<%= uid %>'" :username="'<%= username %>'"></avatar>
    <user-info id="userinfo" :uid="'<%= uid %>'" :username="'<%= username %>'"></user-info>
  </div>
</div>

<script type="text/javascript" src="dist/js/home.dist.js"></script></body>

</html>
