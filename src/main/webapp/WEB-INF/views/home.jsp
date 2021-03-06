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
  System.out.println("viewerUsername " + viewerUsername);
%>

<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya</title>
</head>

<body uid="<%= uid %>" user="<%= username %>" viewer_id="<%= viewerUid %>" viewer="<%= viewerUsername %>" >
<nav-bar id="navbar" :search-box=true :notification=true :uid="'<%= viewerUid %>'" :username="'<%= viewerUsername %>'" :width=1250></nav-bar>

<div class="home-main-container">
  <div class="home-left-bar">
    <avatar id="avatar" :uid="'<%= uid %>'" :username="'<%= username %>'" :viewer-uid="'<%= viewerUid %>'" :viewer-username="'<%= viewerUsername %>'" :editable="<%= editable %>"></avatar>
    <user-info id="userinfo" :uid="'<%= uid %>'" :username="'<%= username %>'" :editable="<%= editable %>"></user-info>
  </div>

  <div class="post-main-container">
    <poster id="poster" :uid="'<%= uid %>'" :username="'<%= username %>'" :editable="<%= editable %>" :debug=false></poster>
    <post-board id="post-board" :uid="'<%= uid %>'" :username="'<%= username %>'" :editable="<%= editable %>" :debug=false></post-board>
  </div>
</div>

<chat id="chat" :uid="'<%= viewerUid %>'" :username="'<%= viewerUsername %>'" :debug=false></chat>

<image-viewer id="image-viewer" :debug=false></image-viewer>

<script type="text/javascript" src="dist/js/home.dist.js"></script></body>

</html>
