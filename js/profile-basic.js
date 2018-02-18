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

  function Display(el) {
    this.el = $(el);
    this.el.find(".edit-button").click($.proxy(this.clickEdit, this));

    this.name = this.el.find("li.name-info");
    this.email = this.el.find("li.email-info");
    this.phone = this.el.find("li.phone-info");
    this.birth = this.el.find("li.birth-info");
  }

  Display.prototype.clickEdit = function() {
    this.edit["name"].find("input").val(this["name"].find("span").html());
    this.edit["email"].find("input").val(this["email"].find("span").html());
    this.edit["phone"].find("input").val(this["phone"].find("span").html());
    
    let birth = parseBirth(this["birth"].find("span").html());
    this.edit["selectYear"].find("option[value=" + birth[2] + "]")
                           .attr("selected", "true");
    this.edit["selectMonth"].find("option[value=" + (+birth[0]) + "]")
                            .attr("selected", "true");
    this.edit["selectDate"].find("option[value=" + (+birth[1]) + "]")
                           .attr("selected", "true");

    this.el.hide();
    this.edit.show();
  }

  function parseBirth(birth) {
    return birth.split("/");
  }

  function formatBirth(year, month, date) {
    if (date < 10) {
      date = "0" + date;
    }
    if (month < 10) {
      month = "0" + month;
    }
    return month + "/" + date + "/" + year;
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
    this.selectYear.append($('<option></option>').html("--"));
    for (let year = 1980; year <= thisYear; year++) {
      this.selectYear.append($('<option></option>').val(year).html(year));
    }

    this.selectMonth = this.birth.find("select[name='month']");
    this.selectMonth.append($('<option></option>').html("--"));
    for (let month = 1; month <= 12; month++) {
      this.selectMonth.append($('<option></option>').val(month).html(month));
    }

    this.selectDate = this.birth.find("select[name='date']");
    this.selectDate.append($('<option></option>').html("--"));
    for (let date = 1; date <= 31; date++) {
      this.selectDate.append($('<option></option>').val(date).html(date));
    }
  }

  Edit.prototype.clickEidtCancel = function() {
    this.display.show();
    this.hide();
  }

  Edit.prototype.clickEidtSave = function() {
    // TODO: Send AJAX and update html.
    this.display["name"].find("span").html(this["name"].find("input").val());
    this.display["email"].find("span").html(this["email"].find("input").val());
    this.display["phone"].find("span").html(this["phone"].find("input").val());
    this.display["birth"].find("span").html(
        formatBirth(this.selectYear.val(),
                    this.selectMonth.val(),
                    this.selectDate.val()));

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
