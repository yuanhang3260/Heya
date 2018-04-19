import $ from "jquery";
import "bootstrap/dist/css/bootstrap.min.css";

import utils from "heya/common/js/utils.js"
import popups from "heya/common/js/popups.js"

function init() {
  this.sid = this.initData.sid;
  this.school = this.initData.school;
  this.major = this.initData.major;
  if (this.initData.year) {
    this.yearStart = this.initData.year.start;
    this.yearEnd = this.initData.year.end;
  }
}

function hasValidYearInfo() {
  return this.yearEnd && !isNaN(this.yearEnd);
}

function cancelEdit() {
  this.mode = "display";
}

function saveChanges(education) {
  if (!this.debug) {
    this.updateSchoolInfo(education);
  } else {
    this.updateModelData(education);
  }

  this.mode = "display";
  // TOOD: emit further to upper level? or remove .prevent tag?
}

function updateModelData(education) {
  this.school = education.school;
  this.major = education.major;
  if (education.year) {
    this.yearStart = education.year.start;
    this.yearEnd = education.year.end;
  }
}

function updateSchoolInfo(education) {
  // Send AJAX to update school info.
  var formData = {
    "_method": "PUT",
  };
  if (education.school) {
    formData["school"] = education.school;
  }
  if (education.major) {
    formData["major"] = education.major;
  }
  if (education.year) {
    if (education.year.start && !isNaN(education.year.start)) {
      formData["startYear"] = education.year.start;
    }
    if (education.year.end && !isNaN(education.year.end)) {
      formData["endYear"] = education.year.end;
    }
  }

  // Process the form.
  var me = this;
  $.ajax({
    type : "POST",
    url : "userinfo/" + me.username + "/education/" + this.sid,
    data : formData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);

    if (data.success) {
      me.updateModelData(education);
      me.sid = data.result["schoolId"];
    } else {
      popups.alert("Update failed");
    }
  });
}

function deleteSchool() {
  let me = this;
  popups.confirm({
    message: "Are you sure to delete school " + this.school + "?",
    task: function() {
      if (!me.debug) {
        me.doDeleteSchool();
      } else {
        me.$emit("delete-school", {
          sid: me.sid,
        });
      }
    }
  });
}

function doDeleteSchool() {
  var formData = {
    "_method": "DELETE",  // Restful API for spring backend
  };

  var me = this;
  $.ajax({
    type : "POST",
    url : "userinfo/" + me.username + "/education/" + this.sid,
    data: formData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      me.$emit("delete-school", {
        sid: me.sid,
      });
    } else {
      popups.alert("Delete failed");
    }
  });
}

export default {
  computed: {
    hasValidYearInfo: hasValidYearInfo,
  },

  methods: {
    cancelEdit: cancelEdit,
    saveChanges: saveChanges,
    deleteSchool: deleteSchool,
    updateSchoolInfo: updateSchoolInfo,
    updateModelData: updateModelData,
    doDeleteSchool: doDeleteSchool,
    googleSearchURL: utils.googleSearchURL,
  },

  mounted: init,
}
