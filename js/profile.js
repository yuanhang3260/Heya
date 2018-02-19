define(["jquery", "profile-basic"], function($, basic) {
  function Profile(el) {
    this.el = $(el);

    this.loadUserInfo();

    this.menu = new Menu(this.el.find(".profile-menu"));
    this.panel = new Panel(this.el.find(".profile-panel"));

    this.el.click($.proxy(this.clickHandler, this));
  }

  Profile.prototype.categories =
      ["basic", "education", "work", "places", "other"];

  Profile.prototype.loadUserInfo = function() {
    var formData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user")
    };

    // Process the form.
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
        // TODO: Use me.panel.initUserInfo(data)
        me.panel["basic"].initUserInfo(data);
      }
    });
  };

  Profile.prototype.clickHandler = function(event) {
    let target = $(event.target);
    
    // Handle click menu event.
    let selected = null;
    if (target.is(".profile-menu li.profile-basic-item")) {
      selected = "basic";
    } else if (target.is(".profile-menu li.profile-education-item")) {
      selected = "education";
    } else if (target.is(".profile-menu li.profile-work-item")) {
      selected = "work";
    } else if (target.is(".profile-menu li.profile-places-item")) {
      selected = "places";
    } else if (target.is(".profile-menu li.profile-other-item")) {
      selected = "other";
    }

    if (selected) {
      for (let item of Profile.prototype.categories) {
        if (item === selected) {
          this.menu[item].addClass("item-selected");
          this.panel[item].show();
        } else {
          this.menu[item].removeClass("item-selected");
          this.panel[item].hide();
        }
      }
    }
  }

  function Menu(el) {
    this.el = $(el);

    this.basic = this.el.find("li.profile-basic-item");
    this.education = this.el.find("li.profile-education-item");
    this.work = this.el.find("li.profile-work-item");
    this.places = this.el.find("li.profile-places-item");
    this.other = this.el.find("li.profile-other-item");
  }

  function Panel(el) {
    this.el = $(el);

    this.basic = new basic.ProfileBasic(this.el.find(".profile-basic-panel"));
    this.education = this.el.find(".profile-education-panel");
    this.work = this.el.find(".profile-work-panel");
    this.places = this.el.find(".profile-places-panel");
    this.other = this.el.find(".profile-other-panel");

    this.basic.show();
  }

  return {
    Profile: Profile,
  }
});
