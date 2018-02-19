define(["jquery"], function($) {
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
    this.el.show();
  }

  ProfileBasic.prototype.items = ["name", "email", "phone", "birth"];

  ProfileBasic.prototype.initUserInfo = function(data) {
    for (let item of this.items) {
      if (data[item]) {
        this.display.createItem(item, data[item]);
      }
    }
  }

  function Display(el) {
    this.el = $(el);
    this.el.find(".edit-button").click($.proxy(this.clickEdit, this));

    // this.name = this.el.find("li.name-info");
    // this.email = this.el.find("li.email-info");
    // this.phone = this.el.find("li.phone-info");
    // this.birth = this.el.find("li.birth-info");
  }

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
    let items = ProfileBasic.prototype.items;
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

  Display.prototype.clickEdit = function() {
    if (this["name"]) {
      this.edit["name"].find("input").val(this["name"].find("span").html());
    } else {
      this.edit["name"].find("input").val(null);
    }
    if (this["email"]) {
      this.edit["email"].find("input").val(this["email"].find("span").html());
    } else {
      this.edit["email"].find("input").val(null);
    }
    if (this["phone"]) {
      this.edit["phone"].find("input").val(this["phone"].find("span").html());
    } else {
      this.edit["phone"].find("input").val(null);
    }

    if (this["birth"]) {
      this.edit.resetSelectOptions();
      if (this["birth"].find("span").html()) {
        let birth = parseBirth(this["birth"].find("span").html());
        this.edit["selectYear"].find("option[value=" + birth[0] + "]")
                               .attr("selected", "true");
        this.edit["selectMonth"].find("option[value=" + (+birth[1]) + "]")
                                .attr("selected", "true");
        this.edit["selectDate"].find("option[value=" + (+birth[2]) + "]")
                               .attr("selected", "true");
      } else {
        this.edit["selectYear"].find("option[value=--]")
                               .attr("selected", "true");
        this.edit["selectMonth"].find("option[value=--]")
                                .attr("selected", "true");
        this.edit["selectDate"].find("option[value=--]")
                               .attr("selected", "true");
      }
    }

    this.el.hide();

    this.edit.hideErrorMsg();
    this.edit.show();
  }

  function parseBirth(birth) {
    return birth.split("-");
  }

  function formatBirth(year, month, date) {
    if (date < 10) {
      date = "0" + date;
    }
    if (month < 10) {
      month = "0" + month;
    }
    return year + "-" + month + "-" + date;
  }

  Display.prototype.hide = function() {
    this.el.hide();
  }

  Display.prototype.show = function() {
    this.el.show();
  }

  function Edit(el) {
    this.el = $(el);

    this.el.find(".button-box .cancel-btn")
           .click($.proxy(this.clickEidtCancel, this));

    this.el.find(".button-box .save-btn")
           .click($.proxy(this.clickEidtSave, this));

    this.name = this.el.find(".edit-name");
    this.email = this.el.find(".edit-email");
    this.phone = this.el.find(".edit-phone");

    // Create birth select.
    this.birth = this.el.find(".edit-birth");
    this.selectYear = this.birth.find("select[name='year']");
    let thisYear = (new Date()).getFullYear();
    this.selectYear.append($('<option></option>').val("--").html("--"));
    for (let year = 1980; year <= thisYear; year++) {
      this.selectYear.append($('<option></option>').val(year).html(year));
    }

    this.selectMonth = this.birth.find("select[name='month']");
    this.selectMonth.append($('<option></option>').val("--").html("--"));
    for (let month = 1; month <= 12; month++) {
      this.selectMonth.append($('<option></option>').val(month).html(month));
    }

    this.selectDate = this.birth.find("select[name='date']");
    this.selectDate.append($('<option></option>').val("--").html("--"));
    for (let date = 1; date <= 31; date++) {
      this.selectDate.append($('<option></option>').val(date).html(date));
    }
  }

  Edit.prototype.resetSelectOptions = function() {
    this.selectYear.find("option").removeAttr("selected");
    this.selectMonth.find("option").removeAttr("selected");
    this.selectDate.find("option").removeAttr("selected");
  }

  Edit.prototype.clickEidtCancel = function() {
    this.display.show();
    this.hide();
  }

  Edit.prototype.clickEidtSave = function() {
    // Send AJAX to update basic info.
    var formData = {
      "uid" : +$("body").attr("uid"),
      "username" : $("body").attr("user"),
      "section": "basic",
      "action": "update",
      "name": this["name"].find("input").val(),
      "email": this["email"].find("input").val(),
      "phone": this["phone"].find("input").val(),
      "birth": JSON.stringify({
        "year": this.selectYear.val(),
        "month": this.selectMonth.val(),
        "date": this.selectDate.val(),
      }),
    };

    // Disable the buttons.
    this.el.find("button.save-btn").attr("disabled", "true");
    this.el.find("button.cancel-btn").attr("disabled", "true");

    // Process the form.
    var me = this;
    $.ajax({
      type : "POST",
      url : "updateuserinfo",
      data : formData,
      dataType : "json",
      encode : true,
    }).done(function(data) {
      // log data to the console so we can see.
      console.log(data);

      me.el.find("button.save-btn").removeAttr("disabled");
      me.el.find("button.cancel-btn").removeAttr("disabled");
      if (data.success) {
        me.hideErrorMsg();
        me.displayBasicInfo(data);
      } else {
        me.showErrorMsg(data.reason);
      }
    });
  }

  Edit.prototype.hideErrorMsg = function() {
    this.el.find(".update-error-msg").html("").hide();
  }

  Edit.prototype.showErrorMsg = function(msg) {
    this.el.find(".update-error-msg").html("Failed: " + msg).show();
  }

  Edit.prototype.displayBasicInfo = function() {
    // Display name, email, phone
    var items = ["name", "email", "phone", "birth"];

    for (let item of ProfileBasic.prototype.items) {
      var displayItem = this.display[item];
      var content = null;
      if (item !== "birth") {
        content = this[item].find("input").val();
      } else {
        if (this.selectYear.val() && !isNaN(this.selectYear.val()) &&
            this.selectMonth.val() && !isNaN(this.selectMonth.val()) &&
            this.selectDate.val() && !isNaN(this.selectDate.val())) {
          content = formatBirth(this.selectYear.val(),
                                this.selectMonth.val(),
                                this.selectDate.val());
        }
      }

      if (content) {
        if (displayItem) {
          displayItem.find("span").html(content);
        } else {
          displayItem = this.display.createItem(item, content);
        }
        displayItem.show();
      } else {
        if (displayItem) {
          displayItem.find("span").html(null);
          displayItem.hide();
        }
      }
    }

    this.display.show();
    this.hide();
  }

  Edit.prototype.hide = function() {
    this.el.hide();
  }

  Edit.prototype.show = function() {
    this.el.show();
  }

  return {
    ProfileBasic: ProfileBasic,
  }
});
