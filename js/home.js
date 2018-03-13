define(["jquery", "utils", "ImageClipper"], function($, utils, ImageClipper) {
  // ---------------------------- User Info --------------------------------- //
  function UserInfo(el) {
    this.el = $(el);

    setTimeout($.proxy(this.load, this), 500);
    // this.load();
  };

  UserInfo.prototype.displayUserInfo = function(data) {
    function isCurrentYear(year) {
      return year === (new Date()).getFullYear();
    }

    if (data.work) {
      data.work.sort(utils.sortByYearDesc);
      for (var i = 0; i < Math.min(data.work.length, 2); i++) {
        var work = data.work[i];
        var current = false;
        if (work.year && !isNaN(work.year.end)) {
          current = isCurrentYear(work.year.end);
        }
        this.el.append(this.createWorkInfo(work.company, current));
      }
    }
    if (data.education) {
      data.education.sort(utils.sortByYearDesc);
      for (var i = 0; i < Math.min(data.education.length, 2); i++) {
        var education = data.education[i];
        var current = false;
        if (education.year && !isNaN(education.year.end)) {
          current = isCurrentYear(education.year.end);
        }
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

  UserInfo.prototype.createWorkInfo = function(work, current) {
    var item = $("<div>", {"class": "user-profile-item"});
    var icon = $("<i>", {"class": "user-profile-icon user-work-icon " +
                                  "fa fa-laptop"});
    var detail = $("<p>", {"class": "user-profile-detail"});
    var verb = current ? "Works" : "Worked";
    detail.append(verb + " at ");
    var link = $("<a>", {"href": "https://www.google.com/search?q=" + work,
                         "target": "_blank",
                         "class": "user-profile-link"});
    link.html(work);
    detail.append(link);
    item.append(icon).append(detail);
    return item;
  }

  UserInfo.prototype.createEducationInfo = function(school, current) {
    var item = $("<div>", {"class": "user-profile-item"});
    var icon = $("<i>", {"class": "user-profile-icon user-education-icon " +
                                  "fa fa-graduation-cap"});
    var detail = $("<p>", {"class": "user-profile-detail"});
    var verb = current ? "Studies" : "Studied";
    detail.append(verb + " at ");
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
        type: "GET",
        url: "getuserinfo",
        data: formData,
        dataType: "json",
        encode: true,
    }).done(function(data) {
      // log data to the console so we can see.
      console.log(data);
      if (data.success) {
        me.displayUserInfo(data);
      }
    });
  };

  // --------------------------- Profile Image ---------------------------- //
  function ProfileImage(el) {
    this.$el = $(el);

    var clipper = new ImageClipper("Update Profile Image",
                                   $.proxy(this.updateProfileImage, this));

    this.$el.on("click", function() {
      clipper.open();
    });
  }

  ProfileImage.prototype.updateProfileImage = function(filename, blob) {
    // console.log("image submitted");

    var formData = new FormData();
    formData.append("uid", +$("body").attr("uid"));
    formData.append("action", "update");
    formData.append("filename", filename);
    formData.append("imagedata", blob);

    // Process the form.
    var me = this;
    $.ajax({
      type: "POST",
      url: "profileimage",
      data: formData,
      processData: false,
      contentType: false,
    }).done(function(data) {
      // log data to the console so we can see.
      console.log(data);
    });
  }

  return {
    UserInfo: UserInfo,
    ProfileImage: ProfileImage,
  }
});
