<!DOCTYPE html>
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
      <img class="card-img-top cover-img" src="img/snoopy-house.jpg" style="opacity: 0.7">
      <div class="profile-img-container"></div>
      <div class="card-body username-container">Snoopy</div>
      <div class="card-body follower-container">
        <div class="follower">
          <div class="follower-title">Followers</div>
          <div class="follower-num">12M</div>
        </div>
        <div class="follower-split-line"></div>
        <div class="follower">
          <div class="follower-title">Following</div>
          <div class="follower-num">3</div>
        </div>
      </div>
    </div>
  </div>
</div>

</body>

<script src="js/require.js" data-main="js/login_main"></script>
</html>
