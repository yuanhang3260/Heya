require.config({
  paths: {
    "jquery": "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min",

    // Bootstrap JS is now AMD compliant. It is dependent on 'jquery'.
    // Use bootstrap.bundle which already includes popper.js.
    "bootstrap": "bootstrap.bundle.min",

    "popups": "popups.dist",
  },
});

require(["jquery", "profile"], function($, profile) {
  console.log("# Module jquery loaded");
  console.log("# Module profile loaded");

  var obj = new profile.Profile("#profile-container");
});
