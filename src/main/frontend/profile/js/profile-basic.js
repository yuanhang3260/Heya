import $ from "jquery";

import utils from "./utils.js";
import popups from "heya/common/js//popups.js";

function clickEdit() {
  this.nameInput = this.name;
  this.emailInput = this.email;
  this.phoneInput = this.phone;
  if (this.birth) {
    let birth = utils.parseBirth(this.birth);
    this.birthYearInput = +birth[0];
    this.birthMonthInput = +birth[1];
    this.birthDateInput = +birth[2];
  } else {
    this.birthYearInput = "--";
    this.birthMonthInput = "--";
    this.birthDateInput = "--";
  }

  this.errMsg = null;
  this.mode = "edit";
}

function clickCancel() {
  this.mode = "display";
}

function clickSave() {
  if (!this.emailInput) {
    this.errMsg = "Email is required";
    return;
  }

  if (!this.debug) {
    this.updateBasicInfo();
  } else {
    this.updateModelData();
  }

  this.mode = "display";
}

function updateModelData() {
  if (this.name !== this.nameInput) {
    this.name = this.nameInput;
  }
  if (this.email !== this.emailInput) {
    this.email = this.emailInput;
  }
  if (this.phone !== this.phoneInput) {
    this.phone = this.phoneInput;
  }
  let birth = utils.formatBirth(
      this.birthYearInput, this.birthMonthInput, this.birthDateInput);
  if (this.birth != birth) {
    this.birth = birth;
  }
}

function updateBasicInfo() {
  var reqData = {
    "action": "update",
    "name": this.nameInput,
    "email": this.emailInput,
    "phone": this.phoneInput,
    "birth": JSON.stringify({
      year: this.birthYearInput,
      month: this.birthMonthInput,
      date: this.birthDateInput,
    })
  };

  // Process the form.
  var me = this;
  $.ajax({
    type : "POST",
    url : "userinfo/" + me.username + "/basic",
    data : reqData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);

    if (data.success) {
      me.updateModelData();
    } else {
      popups.alert("Update failed");
    }
  });
}

export default {
  computed: {
  },

  methods: {
    clickEdit: clickEdit,
    clickSave: clickSave,
    clickCancel: clickCancel,
    updateBasicInfo: updateBasicInfo,
    updateModelData: updateModelData,
    yearSelectOptions: utils.yearSelectOptions,
    monthSelectOptions: utils.monthSelectOptions,
    dateSelectOptions: utils.dateSelectOptions,
  },
}