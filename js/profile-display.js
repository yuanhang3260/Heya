define(["jquery"], function($) {
  function DisplayBase(el, config) {
    this.el = $(el);
    this.config = config;

    this.edit = null;
  }

  // For sub class to implement. This function accepts data from editor or
  // backend and display the data.
  DisplayBase.prototype.displayData = function(data) {
    this.show();
  }

  // For sub class to implement. Generate data for editor to pre-set.
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
