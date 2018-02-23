define(["jquery", "profile-edit", "profile-display"],
       function($, edit, display) {
  // ----------------------------------------------------------------------- //
  // Profile education section, top panel.
  function ProfilePlaces(el) {
    this.el = $(el);

    this.current = new Place(".current-live", {current: true});
    this.hometown = new Place(".hometown", {hometown: true});

    // this.addNew = new AddNew(this.el.find(".add-new-item"), this);
    // this.places = [];
  }

  ProfilePlaces.prototype.initUserInfo = function(data) {
    var places = data.places;
    if (places && Object.prototype.toString.call(places) === "[object Array]") {
      for (let place of places) {
        if (place.current) {
          this.current.displayPlaceInfo(place);
        } else if (place.hometown) {
          this.hometown.displayPlaceInfo(place);
        }
      }
    }
  }

  ProfilePlaces.prototype.hide = function() {
    this.el.hide();
  }

  ProfilePlaces.prototype.show = function() {
    this.el.show();
  }

  // ----------------------------------------------------------------------- //
  // Place info box.
  function Place(el, config) {
    this.el = $(el);
    this.config = config;
    this.pid = null;

    this.add = new AddNewButton(this.el.find(".add-item-button"));
    this.display = new Display(this.el.find(".profile-info-display"));
    this.edit = new Edit(this.el.find(".profile-info-edit"));

    // Keep reference of each other.
    this.add.edit = this.edit;
    this.display.edit = this.edit;
    this.display.add = this.add;
    this.edit.display = this.display;
    this.edit.add = this.add;

    // Keep reference of container.
    this.display.box = this;
    this.edit.box = this;

    // Top panel.
    this.empty = true;
  }

  Place.prototype.displayPlaceInfo = function(data) {
    this.add.hide();
    this.edit.hide();
    this.display.displayData(data);
    this.setEmpty(false);
  }

  Place.prototype.isCurrent = function() {
    return this.config.current;
  }  

  Place.prototype.isHometown = function() {
    return this.config.hometown;
  }

  Place.prototype.getPid = function() {
    return this.pid;
  }

  Place.prototype.setPid = function(pid) {
    this.pid = pid;
    this.el.attr("pid", pid);
  }

  Place.prototype.isEmpty = function() {
    return this.empty;
  }

  Place.prototype.setEmpty = function(empty) {
    this.empty = empty;
  }

  // ----------------------------------------------------------------------- //
  // Component - AddNewButton, simply inherits from DisplayBase.
  function AddNewButton(el) {
    display.DisplayBase.call(this, el, {});

    this.el.click($.proxy(this.clickEdit, this));
  }

  AddNewButton.prototype = Object.create(display.DisplayBase.prototype);
  AddNewButton.prototype.constructor = AddNewButton;

  // ----------------------------------------------------------------------- //
  function Display(el, config) {
    display.DisplayBase.call(this, el, config);

    this.el.find(".profile-edit-button").click($.proxy(this.clickEdit, this));
    this.el.find(".profile-delete-button")
           .click($.proxy(this.clickDelete, this));
  }

  Display.prototype = Object.create(display.DisplayBase.prototype);
  Display.prototype.constructor = Display;

  Display.prototype.resetDisplay = function() {
  }

  var googleMapSearch = "https://www.google.com/maps/search/";

  Display.prototype.displayData = function(data) {
    this.el.find("a.place-info").html(data.place)
                                .attr("href", googleMapSearch + data.place);
    this.box.setPid(data.pid);
    this.el.show();
  }

  Display.prototype.generateData = function() {
    var data = {};
    data.place = this.el.find(".place-info").html();
    return data;
  }

  Display.prototype.clickDelete = function() {
    var doDelete = window.confirm("Delete this place?");
    if (!doDelete) {
      return;
    }

    var reqData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user"),
      "section": "places",
      "action": "delete",
      "pid": this.box.getPid(),
    };

    console.log(reqData);

    // Process the form.
    var me = this;
    $.ajax({
      type : "POST",
      url : "updateuserinfo",
      data : reqData,
      dataType : "json",
      encode : true,
    }).done(function(data) {
      // log data to the console so we can see.
      console.log(data);
      if (data.success) {
        // TODO: Reset everything for Display?
        me.box.setEmpty(true);
        me.hide();
        me.edit.hide();
        me.add.show();
      } else {
        window.alert("Delete place failed - " + data.reason);
      }
    });

    // Local front end test.
    // this.box.setEmpty(true);
    // this.hide();
    // this.edit.hide();
    // this.add.show();
  }


  // ----------------------------------------------------------------------- //
  // Component - Edit/Add school, inherits from EditBase.
  function Edit(el) {
    edit.EditBase.call(this, el, Edit.prototype.config);
  }

  Edit.prototype = Object.create(edit.EditBase.prototype);
  Edit.prototype.constructor = Edit;

  Edit.prototype.config = {
    "inputs": ["place"],
  }

  // Override clickEditCancel.
  Edit.prototype.clickEditCancel = function() {
    this.hide();
    if (this.box.isEmpty()) {
      this.add.show();
    } else {
      this.display.show();
    }
  }

  Edit.prototype.submitData = function() {
    var action;
    if (this.box.isEmpty()) {
      action = "add";
    } else {
      action = "update";
    }

    // Send AJAX to update basic info.
    var reqData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user"),
      "section": "places",
      "action": action,
    };

    if (action === "update") {
      reqData.pid = this.box.getPid();
    }

    if (this.box.isCurrent()) {
      reqData.current = true;
    }
    if (this.box.isHometown()) {
      reqData.hometown = true;
    }

    // Add form data to http request data.
    var formData = this.generateData();
    for (let key in formData) {
      let value = formData[key];
      if (Object.prototype.toString.call(value) === "[object Object]") {
        reqData[key] = JSON.stringify(formData[key]);
      } else {
        reqData[key] = value;
      }
    }

    if (!formData.place) {
      this.showErrorMsg("Place required");
      return;
    }

    console.log(reqData);

    // Disable the buttons.
    this.disableButtons();

    // Process the form.
    var me = this;
    $.ajax({
      type : "POST",
      url : "updateuserinfo",
      data : reqData,
      dataType : "json",
      encode : true,
    }).done(function(data) {
      // log data to the console so we can see.
      console.log(data);

      me.enableButtons();
      if (data.success) {
        me.hide();
        me.updatePlaceInfo(data.placeId, formData);
      } else {
        me.showErrorMsg(data.reason);
      }
    });

    // Local front end test.
    // this.enableButtons();
    // this.hide();
    // this.updatePlaceInfo(0, formData);
  }

  Edit.prototype.updatePlaceInfo = function(pid, data) {
    if (this.box.isEmpty()) {
      // Add new place info.
      data.pid = pid;
      this.add.hide();
      this.hide();
      this.display.displayData(data);
      this.box.setEmpty(false);
    } else {
      this.display.displayData(data);
    }
  }

  return {
    ProfilePlaces: ProfilePlaces,
  }
});
