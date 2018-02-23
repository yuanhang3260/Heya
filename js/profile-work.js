define(["jquery", "profile-edit", "profile-display"],
       function($, edit, display) {
  // ----------------------------------------------------------------------- //
  // Profile work section, top panel.
  function ProfileWork(el) {
    this.el = $(el);

    this.addNew = new AddNew(this.el.find(".add-new-item"), this);
    this.companies = [];
  }

  ProfileWork.prototype.addCompany = function(company) {
    this.companies.push(company);
    // TODO: Sort companies based on graduation time in descending order?
  }

  ProfileWork.prototype.removeCompany = function(cid) {
    for (let i = 0; i < this.companies.length; i++) {
      if (this.companies[i].cid === cid) {
        this.companies.splice(i, 1);
        return;
      }
    }
  }

  ProfileWork.prototype.initUserInfo = function(data) {
    var work = data.work;
    if (work && Object.prototype.toString.call(work) === "[object Array]") {
      for (let company of work) {
        this.addNew.createNewCompany(company);
      }
    }
  }

  ProfileWork.prototype.hide = function() {
    this.el.hide();
  }

  ProfileWork.prototype.show = function() {
    this.el.show();
  }

  // ----------------------------------------------------------------------- //
  // Add new company.
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

  AddNew.prototype.createNewCompany = function(data) {
    // Create company box. It clones display and edit DOMs from templates
    // in addNew.
    var el = $("<div>", {"class": "profile-info-box", "cid": data.cid});
    el.append(this.cloneDisplay());
    el.append(this.cloneEdit());

    this.el.before(el);

    var company = new Company(el, data.cid);
    if (this.panel) {
      company.panel = this.panel;
      company.displayCompanyInfo(data);
      this.panel.addCompany(company);
    }
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
  // company info box.
  function Company(el, cid) {
    this.el = $(el);
    this.cid = cid;

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

  Company.prototype.displayCompanyInfo = function(data) {
    this.edit.hide();
    this.display.displayData(data);
  }

  Company.prototype.remove = function() {
    this.panel.removeCompany(this.cid);
    this.el.remove();
  }

  Company.prototype.getCid = function() {
    return this.cid;
  }

  Company.prototype.setCid = function(cid) {
    this.cid = cid;
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
    this.el.find("a.company-info").html(data.company)
                                 .attr("href", googleSearch + data.company);

    var detail = this.el.find("p.profile-detail");
    detail.empty();

    var positionAdded = false;
    if (data.position) {
      detail.append($('<span class="position-info">').html(data.position));
      positionAdded = true;
    }

    if (data.year) {
      var start = $("<span>", {"class": "start-year",
                               "year": data.year.start})
                  .html(data.year.start);
      var end = $("<span>", {"class": "end-year",
                             "year": data.year.end})
                .html(data.year.end);

      year = $("<span>");
      if (positionAdded) {
        year.append($("<span>").html(" &middot "));
      }
      year.append(start).append($("<span>").html(" - ")).append(end);
      if (!isNaN(data.year.start) && !isNaN(data.year.end)) {
        year.show();
      } else {
        year.hide();
      }
      detail.append(year);
    }

    this.el.show();
  }

  Display.prototype.generateData = function() {
    var data = {};
    data.company = this.el.find(".company-info").html();
    data.position = this.el.find(".position-info").html();

    data.year = {};
    data.year.start = this.el.find(".start-year").html();
    data.year.end = this.el.find(".end-year").html();
    return data;
  }

  Display.prototype.clickDelete = function() {
    var doDelete = window.confirm("Delete this company?");
    if (!doDelete) {
      return;
    }

    var reqData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user"),
      "section": "work",
      "action": "delete",
      "cid": this.box.getCid(),
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
        window.alert("Delete company failed - " + data.reason);
      }
    });

    // Local front end test.
    // this.box.remove();
  }


  // ----------------------------------------------------------------------- //
  // Component - Edit/Add company, inherits from EditBase.
  function Edit(el) {
    edit.EditBase.call(this, el, Edit.prototype.config);
  }

  Edit.prototype = Object.create(edit.EditBase.prototype);
  Edit.prototype.constructor = Edit;

  Edit.prototype.config = {
    "inputs": ["company", "position"],
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
      "section": "work",
      "action": action,
    };

    if (action === "update") {
      reqData.cid = this.box.getCid();
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

    if (!formData.company) {
      this.showErrorMsg("company name required");
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
        me.updateworkInfo(data.companyId, formData);
      } else {
        me.showErrorMsg(data.reason);
      }
    });

    // Local front end test.
    // this.enableButtons();
    // this.hide();
    // this.updateworkInfo(0, formData);
  }

  Edit.prototype.updateworkInfo = function(cid, data) {
    if (this.display instanceof AddNewButton) {
      // Add new company to display.
      data.cid = cid;
      this.parent.createNewCompany(data);
      this.display.displayData();  // Displays "Add new company".
    } else {
      this.display.displayData(data);
    }
  }

  return {
    ProfileWork: ProfileWork,
  }
});
