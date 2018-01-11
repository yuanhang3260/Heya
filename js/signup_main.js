requirejs.config({
  paths: {
    jquery : "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min"
  }
});

requirejs(["jquery", "signup"], function($, signup) {
  console.log("# Module jquery loaded");
  console.log("# Module signup loaded");

  var signup_ = new signup.SignUp("#signup-form");
});
