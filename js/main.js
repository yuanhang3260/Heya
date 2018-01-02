requirejs.config({
  paths: {
    jquery : "https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min"
  }
});

requirejs(["jquery", "backtop", "sidebar"], function($, backtop, sidebar) {
  console.log("# Module jquery loaded");
  console.log("# Module backtop loaded");
  console.log("# Module sidebar loaded");

  var scroll = new backtop.backTop($("#backTop"), {
    mode : "move",
  });

  var sidebar = new sidebar.Sidebar("#sidebar");

  $(document).click(function(e) {
    e.preventDefault();

    // Click outside sidebar, close all panels.
    if ($(e.target).closest("#sidebar .sidebar-menu").length == 0) {
      sidebar.closePanels();
    }
  });

  // $("#backTop").backtop({
  //   mode : "move",
  // });
});
