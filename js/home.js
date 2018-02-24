define(["jquery", "utils"], function($, utils) {
  function UserInfo(el) {
    this.el = $(el);

    setTimeout($.proxy(this.load, this), 500);
    // this.load();
  };

  UserInfo.prototype.displayUserInfo = function(data) {
    if (data.work) {
      data.work.sort(utils.sortByYearDesc);
      for (var i = 0; i < Math.min(data.work.length, 2); i++) {
        var work = data.work[i];
        this.el.append(this.createWorkInfo(work.company, true));
      }
    }
    if (data.education) {
      data.education.sort(utils.sortByYearDesc);
      for (var i = 0; i < Math.min(data.education.length, 2); i++) {
        var education = data.education[i];
        if (!education.highest) {
          this.el.append(this.createEducationInfo(education.school, false));
        }
      }
    }
    if (data.places) {
      for (var i = 0; i < data.places.length; i++) {
        var place = data.places[i];
        if (place.current) {
          this.el.append(this.createLiveInfo(place.place));
        }
        if (place.hometown) {
          this.el.append(this.createHomeTownInfo(place.place));
        }
      }
    }
  }

  UserInfo.prototype.createWorkInfo = function(work) {
    var item = $("<div>", {"class": "user-profile-item"});
    var icon = $("<i>", {"class": "user-profile-icon user-work-icon " +
                                  "fa fa-laptop"});
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
    var icon = $("<i>", {"class": "user-profile-icon user-education-icon " +
                                  "fa fa-graduation-cap"});
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
    var icon = $("<i>", {"class": "user-profile-icon user-live-icon " +
                                  "fa fa-map-marker-alt"});
    var detail = $("<p>", {"class": "user-profile-detail"});
    detail.append("Lives in ");
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
    var icon =
        $("<i>", {"class": "user-profile-icon user-hometown-icon fa fa-home"});
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
