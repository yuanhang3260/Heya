requirejs.config({
  paths: {
    jquery : "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min"
  }
});

requirejs(["jquery", "home"], function($, home) {
  console.log("# Module jquery loaded");
  console.log("# Module home loaded");

  var userinfo = new home.UserInfo("#user-info");
});
