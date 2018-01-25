define(["jquery"], function($) {
  function UserInfo(el) {
    this.el = $(el);

    this.Load();
  };

  // Load user info with AJAX.
  UserInfo.prototype.Load = function() {
    var formData = {
        "uid" : +$("body").attr("uid"),
    };

    // Process the form.
    var form = this.el;
    var me = this;
    $.ajax({
        type : "GET",
        url : "getuserinfo",
        data : formData,
        dataType : "json",
        encode : true,
    }).done(function(data) {
      // log data to the console so we can see.
      console.log(data);
      if (data.success) {
        
      }
    });
  };

  return {
    UserInfo: UserInfo,
  }
});
