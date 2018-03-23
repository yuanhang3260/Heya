define(["jquery", "profile-edit", "profile-display", "utils", "popups"],
       function($, edit, display, utils, popups) {
  // ----------------------------------------------------------------------- //
  // Profile education section, top panel.
  function ProfileEducation(el) {
    this.el = $(el);

    this.addNew = new AddNew(this.el.find(".add-new-item"), this);
    this.schools = [];
  }

  ProfileEducation.prototype.addSchool = function(school) {
    this.schools.push(school);
    // TODO: Sort schools based on graduation time in descending order?
  }

  ProfileEducation.prototype.removeSchool = function(sid) {
    for (let i = 0; i < this.schools.length; i++) {
      if (this.schools[i].sid === sid) {
        this.schools.splice(i, 1);
        return;
      }
    }
  }

  ProfileEducation.prototype.initUserInfo = function(data) {
    var education = data.education;
    if (education &&
        Object.prototype.toString.call(education) === "[object Array]") {
      education.sort(utils.sortByYearDesc);
      for (let school of education) {
        this.addNew.createNewSchool(school);
      }
    }
  }

  ProfileEducation.prototype.hide = function() {
    this.el.hide();
  }

  ProfileEducation.prototype.show = function() {
    this.el.show();
  }

  // ----------------------------------------------------------------------- //
  // Add new school.
  function AddNew(el, panel) {
    this.el = $(el);

    this.button = new AddNewButton(this.el.find(".add-item-button"));
    this.edit = new Edit(this.el.find(".profile-info-edit"));

    this.edit.display = this.button;
    this.button.edit = this.edit;

    this.displayTemplate = this.el.find(".profile-info-display");
    
    this.panel = panel;

    // Pass this reference to editor.
    this.edit.box = this;
  }

  AddNew.prototype.createNewSchool = function(data) {
    // Create school box. It clones display and edit DOMs from templates
    // in addNew.
    var el = $("<div>", {"class": "profile-info-box", "sid": data.sid});
    el.append(this.cloneDisplay());
    el.append(this.cloneEdit());
    this.el.before(el);

    var school = new School(el, data.sid);
    school.panel = this.panel;
    school.displaySchoolInfo(data);
    this.panel.addSchool(school);
  }

  AddNew.prototype.cloneEdit = function() {
    return this.edit.el.clone();
  }

  AddNew.prototype.cloneDisplay = function() {
    return this.displayTemplate.clone();
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
  // School info box.
  function School(el, sid) {
    this.el = $(el);
    this.sid = sid;

    // Create display.
    this.display = new Display(this.el.find(".profile-info-display"));
    this.display.box = this;

    // Create edit.
    this.edit = new Edit(this.el.find(".profile-info-edit"));
    this.edit.box = this;

    // Keep reference of each other.
    this.edit.display = this.display;
    this.display.edit = this.edit;
  }

  School.prototype.displaySchoolInfo = function(data) {
    this.edit.hide();
    this.display.displayData(data);
  }

  School.prototype.remove = function() {
    this.panel.removeSchool(this.sid);
    this.el.remove();
  }

  School.prototype.getSid = function() {
    return this.sid;
  }

  School.prototype.setSid = function(sid) {
    this.sid = sid;
  }

  // ----------------------------------------------------------------------- //
  function Display(el, config) {
    display.DisplayBase.call(this, el, config);

    this.el.find(".profile-edit-button").click($.proxy(this.clickEdit, this));
    this.el.find(".profile-delete-button")
           .click($.proxy(this.clickDelete, this));
  }

  Display.prototype = Object.create(display.DisplayBase.prototype);
  Display.prototype.constructor = Display;

  var googleSearch = "https://www.google.com/search?q=";

  Display.prototype.displayData = function(data) {
    this.el.find("a.school-info").html(data.school)
                                 .attr("href", googleSearch + data.school);

    var detail = this.el.find("p.profile-detail");
    detail.empty();
    var yearAdded = false;
    if (data.year && !isNaN(data.year.start) && !isNaN(data.year.end)) {
      detail.append($("<span>").html("Class of "))
            .append($("<span>", {"class": "year-info",
                                 "start-year": data.year.start})
                     .html(data.year.end));
      yearAdded = true;
    }
    if (data.major) {
      if (yearAdded) {
        detail.append($("<span>").html(" &middot "));
      }
      detail.append($("<span>", {"class": "major-info"}).html(data.major));
    }

    this.el.show();
  }

  Display.prototype.generateData = function() {
    var data = {};
    data.school = this.el.find(".school-info").html();
    data.major = this.el.find(".major-info").html();

    data.year = {};
    data.year.start = this.el.find(".year-info").attr("start-year");
    data.year.end = this.el.find(".year-info").html();
    return data;
  }

  Display.prototype.deleteSchool = function() {
    var reqData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user"),
      "section": "education",
      "action": "delete",
      "sid": this.box.getSid(),
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
        me.box.remove();
      } else {
        window.alert("Delete school failed - " + data.reason);
      }
    });

    // Local front end test.
    // this.box.remove();
  }  

  Display.prototype.clickDelete = function() {
    let school = this.el.find(".school-info").html();
    popups.confirm({
      message: "Are you sure delete school " + school + "?",
      task: $.proxy(this.deleteSchool, this),
    });
  }

  // ----------------------------------------------------------------------- //
  // Component - Edit/Add school, inherits from EditBase.
  function Edit(el) {
    edit.EditBase.call(this, el, Edit.prototype.config);
  }

  Edit.prototype = Object.create(edit.EditBase.prototype);
  Edit.prototype.constructor = Edit;

  Edit.prototype.config = {
    "inputs": ["school", "major"],
    "selects": [
      {
        "name" : "year",
        "subs": [
          {
            "name" : "start",
            "min" : 1980,
            "max" : (new Date()).getFullYear(),
          },
          {
            "name" : "end",
            "min" : 1980,
            "max" : (new Date()).getFullYear(),
          },
        ],
      },
    ],
  }

  Edit.prototype.submitData = function() {
    var action;
    if (this.display instanceof AddNewButton) {
      action = "add";
    } else {
      action = "update";
    }

    // Send AJAX to update basic info.
    var reqData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user"),
      "section": "education",
      "action": action,
    };

    if (action === "update") {
      reqData.sid = this.box.getSid();
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

    if (!formData.school) {
      this.showErrorMsg("School name required");
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
        me.updateEducationInfo(data.schoolId, formData);
      } else {
        me.showErrorMsg(data.reason);
      }
    });

    // Local front end test.
    // this.enableButtons();
    // this.hide();
    // this.updateEducationInfo(0, formData);
  }

  Edit.prototype.updateEducationInfo = function(sid, data) {
    if (this.display instanceof AddNewButton) {
      // Add new school to display. This is in school box, this.display points
      // to addNew instance.
      data.sid = sid;
      this.box.createNewSchool(data);

      this.hide();
      this.display.displayData(null);  // Displays "Add new school".
    } else {
      // This is in school box, this.display points to Display instance.
      this.hide();
      this.display.displayData(data);
    }
  }

  return {
    ProfileEducation: ProfileEducation,
  }
});
