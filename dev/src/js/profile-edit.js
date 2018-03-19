define(["jquery"], function($) {
  // Component Input.
  function Input(el) {
    this.el = $(el);
    this.input = this.el.find("input");
  }

  Input.prototype.getData = function() {
    return this.input.val();
  }

  Input.prototype.setData = function(data) {
    this.input.val(data);
  }

  // Component Select.
  function Select(el, config) {
    this.el = $(el);
    this.config = config;

    for (let sub of config.subs) {
      let subName = sub.name;
      this[subName] = this.el.find("select[name=" + subName + "]");
      this.addNumberOptions(this[subName], sub.min, sub.max);
    }
  }

  Select.prototype.getData = function() {
    var data = {};
    for (let sub of this.config.subs) {
      let subName = sub.name;
      let subData = this[subName].val();
      if (subData) {
        data[subName] = subData;
      }
    }
    return data;
  }

  Select.prototype.setData = function(data) {
    this.resetSelectOptions();

    for (let sub of this.config.subs) {
      let subName = sub.name;
      if (data && data[subName]) {
        let option = this[subName].find("option[value=" + data[subName] + "]");
        if (option) {
          option.attr("selected", "true");
        } else {
          this[subName].find("option[value=--]").attr("selected", "true");
        }
      } else {
        this[subName].find("option[value=--]").attr("selected", "true");
      }
    }
  }

  Select.prototype.addNumberOptions = function(select, start, end) {
    select.find("option").remove();
    select.append($('<option></option>')
          .val("--").html("--").attr("selected", "true"));
    for (let num = start; num <= end; num++) {
      select.append($('<option></option>').val(num).html(num));
    }
  }

  Select.prototype.resetSelectOptions = function() {
    for (let sub of this.config.subs) {
      this[sub.name].find("option").removeAttr("selected");
    }
  }

  // EditBase
  function EditBase(el, config) {
    this.el = $(el);
    this.config = config;

    // Buttons.
    this.cancelBtn = this.el.find(".cancel-btn");
    this.cancelBtn.click($.proxy(this.clickEditCancel, this));

    this.saveBtn = this.el.find(".save-btn");
    this.saveBtn.click($.proxy(this.clickEditSave, this));

    // Create text inputs.
    if (config.inputs) {
      for (let input of config.inputs) {
        this[input] = new Input(this.el.find(".edit-" + input));
      }
    }

    // Create select options.
    if (config.selects) {
      for (let select of config.selects) {
        this[select.name] =
            new Select(this.el.find(".edit-" + select.name), select);
      }
    }

    this.display = null;
  }

  EditBase.prototype.setData = function(data) {
    // Preset input data.
    if (this.config.inputs) {
      for (let input of this.config.inputs) {
        this[input].setData(data[input]);
      }
    }

    // Preset select data.
    if (this.config.selects) {
      for (let select of this.config.selects) {
        this[select.name].setData(data[select.name]);
      }
    }
  }

  // This should be called by submitData to submit data to backend and
  // display.
  EditBase.prototype.generateData = function() {
    var data = {};

    // Input data.
    if (this.config.inputs) {
      for (let input of this.config.inputs) {
        var inputData = this[input].getData();
        if (inputData) {
          data[input] = inputData;
        }
      }
    }

    // Select data.
    if (this.config.selects) {
      for (let select of this.config.selects) {
        var selectData = this[select.name].getData();
        if (selectData) {
          data[select.name] = selectData;
        }
      }
    }

    return data;
  }

  // For sub class to implement.
  EditBase.prototype.submitData = function() {}

  EditBase.prototype.clickEditSave = function() {
    this.submitData();
  }

  EditBase.prototype.clickEditCancel = function() {
    this.hide();
    if (this.display) {
      this.display.show();
    }
  }

  EditBase.prototype.disableButtons = function() {
    this.saveBtn.attr("disabled", "true");
    this.cancelBtn.attr("disabled", "true");
  }

  EditBase.prototype.enableButtons = function() {
    this.saveBtn.removeAttr("disabled");
    this.cancelBtn.removeAttr("disabled");
  }

  EditBase.prototype.hideErrorMsg = function() {
    this.el.find(".update-error-msg").html(null).hide();
  }

  EditBase.prototype.showErrorMsg = function(msg) {
    this.el.find(".update-error-msg").html("Failed: " + msg).show();
  }

  EditBase.prototype.hide = function() {
    this.hideErrorMsg();
    this.el.hide();
  }

  EditBase.prototype.show = function() {
    this.hideErrorMsg();
    this.el.show();
  }

  return {
    EditBase : EditBase,
  }
});