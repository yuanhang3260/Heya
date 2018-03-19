requirejs.config({
  paths: {
    jquery : "jquery"
  }
});

requirejs(["jquery", "signup"], function($, signup) {
  console.log("# Module jquery loaded");
  console.log("# Module signup loaded");

  var signup_ = new signup.SignUp("#signup-form");
});
