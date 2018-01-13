<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <title>Heya</title>
  <link rel="stylesheet" href="css/bootstrap.css">
  <link rel="stylesheet" href="css/font-awesome.min.css">
  <link rel="stylesheet" href="css/index.css">
</head>

<body>
  <p>Hello World!</p>
  <%
    out.println("Home page for user " +
                request.getSession(false).getAttribute("username"));
  %>
</body>

<script src="js/require.js" data-main="js/login_main"></script>
</html>
