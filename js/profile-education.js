define(["jquery"], function($) {
  function ProfileEducation(el) {
    this.el = $(el);

    this.addNew = new AddNew(this.el.find(".add-new-item"));
  }

  ProfileEducation.prototype.hide = function() {
    this.el.hide();
  }

  ProfileEducation.prototype.show = function() {
    this.el.show();
  }

  // Component - Edit school.
  function Edit(el) {
    this.el = $(el);

    this.el.find(".button-box .cancel-btn")
           .click($.proxy(this.clickEidtCancel, this));

    this.el.find(".button-box .save-btn")
           .click($.proxy(this.clickEidtSave, this));

    this.school = this.el.find(".edit-school");
    this.major = this.el.find(".edit-major");
    this.year = this.el.find(".edit-year");

    // Create year select options.
    let thisYear = (new Date()).getFullYear();
    this.selectStartYear = this.year.find("select[name='start']");
    this.addNumberOptions(this.selectStartYear, 1980, thisYear);
    this.selectEndYear = this.year.find("select[name='end']");
    this.addNumberOptions(this.selectEndYear, 1980, thisYear);
  }

  Edit.prototype.addNumberOptions = function(select, start, end) {
    select.append($('<option></option>')
          .val("--").html("--").attr("selected", "true"));
    for (let num = start; num <= end; num++) {
      select.append($('<option></option>').val(num).html(num));
    }
  }

  Edit.prototype.clickEidtCancel = function() {
    this.display.show();
    this.hide();
  }

  Edit.prototype.hide = function() {
    this.el.hide();
  }

  Edit.prototype.show = function() {
    this.el.show();
  }

  // Component - Add new shcool.
  function AddNew(el) {
    this.el = $(el);

    this.button = this.el.find(".add-item-button");
    this.button.click($.proxy(this.clickAddNew, this));

    this.edit = new Edit(this.el.find(".profile-info-edit"));

    this.edit.display = this.button;
  }

  AddNew.prototype.clickAddNew = function() {
    this.button.hide();
    this.edit.show();
  }

  return {
    ProfileEducation: ProfileEducation,
  }
});
