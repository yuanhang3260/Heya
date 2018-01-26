define(["jquery"], function($) {
  function UserInfo(el) {
    this.el = $(el);

    setTimeout($.proxy(this.load, this), 500);
    // this.load();
  };

  UserInfo.prototype.displayUserInfo = function(data) {
    if (data.work) {
      this.el.append(this.createWorkInfo(data.work));
    }
    if (data.education) {
      for (var i = 0; i < data.education.length; i++) {
        var school = data.education[i];
        if (school.highest) {
          // Highest education level.
          this.el.append(this.createEducationInfo(school.name, true));
        }
      }
      for (var i = 0; i < data.education.length; i++) {
        var school = data.education[i];
        if (!school.highest) {
          this.el.append(this.createEducationInfo(school.name, false));
        }
      }
    }
    if (data.live) {
      this.el.append(this.createLiveInfo(data.live));
    }
    if (data.places) {
      for (var i = 0; i < data.places.length; i++) {
        var place = data.places[i];
        if (place.hometown) {
          this.el.append(this.createHomeTownInfo(place.name, true));
        }
      }
    }
  }

  UserInfo.prototype.createWorkInfo = function(work) {
    var item = $("<div>", {"class": "user-profile-item"});
    var icon = $("<i>", {"class": "user-profile-icon fa fa-building-o"});
    var detail = $("<p>", {"class": "user-profile-detail"});
    detail.append("Works at ");
    var link = $("<a>", {"href": "https://www.google.com/search?q=" + work,
                         "target": "_blank",
                         "class": "user-profile-link"});
    link.html(work);
    detail.append(link);
    item.append(icon).append(detail);
    return item;
  }

  UserInfo.prototype.createEducationInfo = function(school, graduate) {
    var item = $("<div>", {"class": "user-profile-item"});
    var icon = $("<i>", {"class": "user-profile-icon user-education-icon fa fa-graduation-cap"});
    var detail = $("<p>", {"class": "user-profile-detail"});
    if (graduate) {
      detail.append("Graduated from ");
    } else {
      detail.append("Studied at ");
    }
    var link = $("<a>", {"href": "https://www.google.com/search?q=" + school,
                         "target": "_blank",
                         "class": "user-profile-link"});
    link.html(school);
    detail.append(link);
    item.append(icon).append(detail);
    return item;
  }

  UserInfo.prototype.createLiveInfo = function(live) {
    var item = $("<div>", {"class": "user-profile-item"});
    var icon = $("<i>", {"class": "user-profile-icon fa fa-home"});
    var detail = $("<p>", {"class": "user-profile-detail"});
    detail.append("Live in ");
    var link = $("<a>", {"href": "https://www.google.com/maps/search/" + live,
                         "target": "_blank",
                         "class": "user-profile-link"});
    link.html(live);
    detail.append(link);
    item.append(icon).append(detail);
    return item;
  }

  UserInfo.prototype.createHomeTownInfo = function(hometown) {
    var item = $("<div>", {"class": "user-profile-item"});
    var icon = $("<i>", {"class": "user-profile-icon user-hometown-icon fa fa-map-marker"});
    var detail = $("<p>", {"class": "user-profile-detail"});
    detail.append("From ");
    var link = $("<a>", {"href": "https://www.google.com/maps/search/" +
                                 hometown,
                         "target": "_blank",
                         "class": "user-profile-link"});
    link.html(hometown);
    detail.append(link);
    item.append(icon).append(detail);
    return item;
  }

  // Load user info with AJAX.
  UserInfo.prototype.load = function() {
    var formData = {
        "uid" : +$("body").attr("uid"),
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
        me.displayUserInfo(data);
      }
    });
  };

  return {
    UserInfo: UserInfo,
  }
});
