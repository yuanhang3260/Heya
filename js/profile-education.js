define(["jquery", "profile-edit", "profile-display"],
       function($, edit, display) {
  // Profile education section.
  function ProfileEducation(el) {
    this.el = $(el);

    this.addNew = new AddNew(this.el.find(".add-new-item"), this);
    this.schools = [];
  }

  ProfileEducation.prototype.addSchool = function(school) {
    this.schools.push(school);
    // TODO: Sort schools based on graduation time in descending order?
  }

  ProfileEducation.prototype.hide = function() {
    this.el.hide();
  }

  ProfileEducation.prototype.show = function() {
    this.el.show();
  }

  // Add new shcool.
  function AddNew(el, panel) {
    this.el = $(el);

    this.button = new AddNewButton(this.el.find(".add-item-button"));
    this.edit = new Edit(this.el.find(".profile-info-edit"));

    this.edit.display = this.button;
    this.button.edit = this.edit;

    this.displayTemplate = this.el.find(".profile-info-display");

    // Pass this reference to editor.
    this.edit.parent = this;
    this.panel = panel;
  }

  AddNew.prototype.createNewSchool = function(data) {
    var school = new School(this, data);
    if (this.panel) {
      this.panel.addSchool(school);
    }
  }

  // Component - AddNewButton, simply inherits from DisplayBase.
  function AddNewButton(el) {
    display.DisplayBase.call(this, el, {});

    this.el.click($.proxy(this.clickEdit, this));
  }

  AddNewButton.prototype = Object.create(display.DisplayBase.prototype);
  AddNewButton.prototype.constructor = AddNewButton;

  // School info box.
  function School(creator, data) {
    var el = $("<div>", {"class": "profile-info-box"});
    creator.el.before(el);
    this.el = el;

    // Create display. It clones the displayTemplate DOM from AddNew.
    var displayEle = creator.displayTemplate.clone();
    this.el.append(displayEle);
    this.display = new Display(displayEle);
    this.display.displayData(data);

    // Create edit. It clones the editor DOM from AddNew.
    var editEle = creator.edit.el.clone();
    this.el.append(editEle);
    this.edit = new Edit(editEle);

    // Keep reference of each other.
    this.edit.display = this.display;
    this.display.edit = this.edit;

    // Top panel.
    this.panel = creator.panel;
  }

  function Display(el) {
    display.DisplayBase.call(this, el, {});

    this.el.find(".profile-edit-button").click($.proxy(this.clickEdit, this));
  }

  Display.prototype = Object.create(display.DisplayBase.prototype);
  Display.prototype.constructor = Display;

  var googleSearch = "https://www.google.com/search?q=";

  Display.prototype.displayData = function(data) {
    this.el.find("a.school-info").html(data.school)
                                 .attr("href", googleSearch + data.school);

    var detail = this.find("p.profile-detail");
    detail.empty();
    var yearAdded = false;
    if (data.year) {
      detail.append("Class of ")
            .append($("<span>", {"class": "year-info",
                                 "start-year": data.year.start}))
            .html(data.year.end);
      yearAdded = true;
    }
    if (data.major) {
      if (yearAdded) {
        detail.append(" &middot ");
      }
      detail.append($("<span>", {"class": "major-info"}));
    }
  }

  Display.prototype.generateData = function() {
    var data = {};
    data.school = this.el.find(".school-info").val();
    data.major = this.el.find(".major-info").val();

    data.year = {};
    data.year.start = this.el.find(".year-info").attr("start-year");
    data.year.end = this.el.find(".year-info").html();
    return data;
  }

  // Component - Edit/Add school, inherits from EditBase.
  function Edit(el) {
    edit.EditBase.call(this, el, Edit.prototype.config);
  }

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
    // Send AJAX to update basic info.
    var reqData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user"),
      "section": "education",
      "action": "add",  // or update, depending on the type of this.display.
    };

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
      this.showErrorMsg("School name must NOT be empty");
      return;
    }

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
        me.hideErrorMsg();
        me.hide();
        me.updateEducationInfo(data.schoolId, formData);
      } else {
        me.showErrorMsg(data.reason);
      }
    });
  }

  Edit.prototype.updateEducationInfo = function(sid, data) {
    if (this.display instanceof AddNewButton) {
      // Add new school to display.
      this.parent.createNewSchool();
    } else {
      me.display.displayData(formData);
    }
  }

  return {
    ProfileEducation: ProfileEducation,
  }
});
