define(["jquery", "profile-edit", "profile-display"],
       function($, edit, display) {
  
  function ProfileBasic(el) {
    this.el = $(el);

    this.display = new Display(this.el.find(".basic-info-display"));
    this.edit = new Edit(this.el.find(".basic-info-edit"));

    // Save reference of each other.
    this.display.edit = this.edit;
    this.edit.display = this.display;
  }

  ProfileBasic.prototype.hide = function() {
    this.el.hide();
  }

  ProfileBasic.prototype.show = function() {
    // this.display.show();
    // this.edit.hide();
    this.el.show();
  }

  ProfileBasic.prototype.initUserInfo = function(data) {
    this.display.displayData(data);
  }

  // Component Display inherits from DisplayBase.
  function Display(el) {
    display.DisplayBase.call(this, el, Display.prototype.config);

    this.el.find(".edit-button").click($.proxy(this.clickEdit, this));
  }

  Display.prototype = Object.create(display.DisplayBase.prototype);
  Display.prototype.constructor = Display;

  Display.prototype.config = {
    "items": ["name", "email", "phone", "birth"],
  };

  var itemIconClass = {
    "name" : "basic-info-icon far fa-user",
    "email" : "basic-info-icon fa fa-envelope",
    "phone" : "basic-info-icon icon-phone fas fa-mobile-alt",
    "birth" : "basic-info-icon icon-birth fas fa-birthday-cake",
  };

  Display.prototype.createItem = function(item, content) {
    var ele = $("<li>", {"class": item + "-info"});
    ele.append($("<i>", {"class": itemIconClass[item]}));
    ele.append($("<span>").html(content));

    // Insert to the correct position orderly.
    let items = Display.prototype.config.items;
    let inserted = false;
    for (var i = items.indexOf(item) + 1; i < items.length; i++) {
      if (this[items[i]]) {
        ele.insertBefore(this[items[i]]);
        inserted = true;
        break;
      }
    }
    if (!inserted) {
      ele.insertBefore(this.el.find(".edit-button"));
    }

    this[item] = ele;
    return ele;
  }

  Display.prototype.displayData = function(data) {
    for (let item of this.config.items) {
      var displayItem = this[item];
      var content = data[item];

      // Format birth.
      if (item === "birth") {
        if (Object.prototype.toString.call(content) === "[object Object]" &&
            !isNaN(content.year) &&
            !isNaN(content.month) && !isNaN(content.date)) {
          content = this.formatBirth(content.year, content.month, content.date);
        } else if (Object.prototype.toString.call(content) !==
                   "[object String]") {
          content = null;
        }
      }

      if (content) {
        if (displayItem) {
          displayItem.find("span").html(content);
        } else {
          displayItem = this.createItem(item, content);
        }
        displayItem.show();
      } else {
        if (displayItem) {
          displayItem.find("span").html(null);
          displayItem.hide();
        }
      }
    }
    this.show();
  }

  Display.prototype.generateData = function() {
    var data = {};
    for (let item of ["name", "email", "phone"]) {
      if (this[item]) {
        data[item] = this[item].find("span").html();
      } else {
        data[item] = null;
      }
    }

    data["birth"] = {};
    if (this["birth"]) {
      let birth = this.parseBirth(this["birth"].find("span").html());
      data["birth"]["year"] = (+birth[0]);
      data["birth"]["month"] = (+birth[1]);
      data["birth"]["date"] = (+birth[2]);
    }

    return data;
  }

  Display.prototype.parseBirth = function(birth) {
    return birth.split("-");
  }

  Display.prototype.formatBirth = function(year, month, date) {
    if (date < 10) {
      date = "0" + date;
    }
    if (month < 10) {
      month = "0" + month;
    }
    return year + "-" + month + "-" + date;
  }

  // Component Edit inherits from EditBase.
  function Edit(el) {
    edit.EditBase.call(this, el, Edit.prototype.config);
  }

  Edit.prototype = Object.create(edit.EditBase.prototype);
  Edit.prototype.constructor = Edit;

  Edit.prototype.config = {
    "inputs": ["name", "email", "phone"],
    "selects": [
      {
        "name" : "birth",
        "subs": [
          {
            "name" : "year",
            "min" : 1980,
            "max" : (new Date()).getFullYear(),
          },
          {
            "name" : "month",
            "min" : 1,
            "max" : 12,
          },
          {
            "name" : "date",
            "min" : 1,
            "max" : 31,
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
      "section": "basic",
      "action": "update",
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

    if (!formData.email) {
      this.showErrorMsg("Email required");
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
        me.display.displayData(formData);
      } else {
        me.showErrorMsg(data.reason);
      }
    });
  }

  return {
    ProfileBasic: ProfileBasic,
  }
});
