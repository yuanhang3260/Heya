requirejs.config({
  paths: {
    jquery : "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min"
  }
});

requirejs(["jquery", "login"], function($, login) {
  console.log("# Module jquery loaded");
  console.log("# Module login loaded");

  var login = new login.Login("#login-form");
});
