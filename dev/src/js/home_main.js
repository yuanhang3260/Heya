requirejs.config({
  paths: {
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
