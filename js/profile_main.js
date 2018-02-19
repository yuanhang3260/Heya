require.config({
  paths: {
    "jquery" : "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min"
  }
});

require(["jquery", "profile"], function($, profile) {
  console.log("# Module jquery loaded");
  console.log("# Module profile loaded");

  var obj = new profile.Profile("#profile-container");
});
