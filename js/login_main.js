requirejs.config({
  paths: {
    jquery : "jquery"
  }
});

requirejs(["jquery", "login"], function($, login) {
  console.log("# Module jquery loaded");
  console.log("# Module login loaded");

  var login_ = new login.Login("#login-form");
});
