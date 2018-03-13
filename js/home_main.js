requirejs.config({
  paths: {
    "jquery": "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min",
    "bootstrap": "bootstrap.bundle.min",

    "ImageClipper": "image-clipper.dist"
  }
});

requirejs(["jquery", "home"], function($, home) {
  console.log("# Module jquery loaded");
  console.log("# Module home loaded");

  var userInfo = new home.UserInfo("#user-info");

  var profileImage = new home.ProfileImage("#profile-image"); 
});
