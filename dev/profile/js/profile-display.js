define(["jquery"], function($) {
  function DisplayBase(el, config) {
    this.el = $(el);
    this.config = config;
  }

  // For sub class to implement.
  DisplayBase.prototype.displayData = function(data) {}

  // For sub class to implement.
  DisplayBase.prototype.generateData = function() {
    return {};
  }

  DisplayBase.prototype.clickEdit = function() {
    if (this.edit) {
      this.hide();
      this.edit.setData(this.generateData());
      this.edit.show();
    }
  }

  DisplayBase.prototype.hide = function() {
    this.el.hide();
  }

  DisplayBase.prototype.show = function() {
    this.el.show();
  }

  return {
    DisplayBase: DisplayBase,
  }
});
