import utils from "./utils.js"

function syncData() {
  if (this.basicInfo) {
    this.name = this.basicInfo.name;
    this.email = this.basicInfo.email;
    this.phone = this.basicInfo.phone;
    this.birth = this.basicInfo.birth;
  }
}

function beforeMount() {
  this.syncData();
}

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
    this.birthYearInput = null;
    this.birthMonthInput = null;
    this.birthDateInput = null;
  }

  this.mode = "edit";
}

function clickSave() {
  if (!this.debug) {
    updateBasicInfo();
  } else {
    this.name = this.nameInput;
    this.email = this.emailInput;
    this.phone = this.phoneInput;
    this.birth = utils.formatBirth(
        this.birthYearInput, this.birthMonthInput, this.birthDateInput);
  }

  this.mode = "display";
}

function updateBasicInfo() {
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
  this.disableButtom = true;

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

    me.disableButtom = false;
    if (data.success) {
      // me.hideErrorMsg();
      // me.hide();
      // me.display.displayData(formData);
    } else {
      // me.showErrorMsg(data.reason);
    }
  });
}

export default {
  computed: {
  },

  methods: {
    syncData: syncData,
    clickEdit: clickEdit,
    clickSave: clickSave,
    updateBasicInfo: updateBasicInfo,
    yearSelectOptions: utils.yearSelectOptions,
    monthSelectOptions: utils.monthSelectOptions,
    dateSelectOptions: utils.dateSelectOptions,
  },

  beforeMount: beforeMount,
}