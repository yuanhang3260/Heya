import $ from "jquery";
import "bootstrap/dist/css/bootstrap.min.css";

import utils from "heya/common/js/utils.js"
import popups from "heya/common/js/popups.js"

function init() {
  this.cid = this.initData.cid;
  this.company = this.initData.company;
  this.position = this.initData.position;
  if (this.initData.year) {
    this.yearStart = this.initData.year.start;
    this.yearEnd = this.initData.year.end;
  }
}

function hasStartYear() {
  return this.yearStart && !isNaN(this.yearStart);
}

function hasEndYear() {
  return this.yearEnd && !isNaN(this.yearEnd);
}

function cancelEdit() {
  this.mode = "display";
}

function saveChanges(work) {
  if (!this.debug) {
    this.updateCompanyInfo(work);
  } else {
    this.updateModelData(work);
  }

  this.mode = "display";
  // TOOD: emit further to upper level? or remove .prevent tag?
}

function updateModelData(work) {
  this.company = work.company;
  this.position = work.position;
  if (work.year) {
    this.yearStart = work.year.start;
    this.yearEnd = work.year.end;
  }
}

function updateCompanyInfo(work) {
  // Send AJAX to update company info.
  var reqData = {
    "uid" : this.uid,
    "username" : this.username,
    "section": "work",
    "action": "update",
    "cid": work.cid,
  };

  if (work.company) {
    reqData["company"] = work.company;
  }
  if (work.position) {
    reqData["position"] = work.position;
  }
  if (work.year && (work.year.start || work.year.end)) {
    reqData["year"] = JSON.stringify({
      start: work.year.start,
      end: work.year.end,
    });
  }

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

    if (data.success) {
      me.updateModelData(work);
    } else {
      popups.alert("Update failed");
    }
  });
}

function deleteCompany() {
  let me = this;
  popups.confirm({
    message: "Are you sure to delete company " + this.company + "?",
    task: function() {
      if (!me.debug) {
        me.doDeleteCompany();
      } else {
        me.$emit("delete-company", {
          cid: me.cid,
        });
      }
    }
  });
}

function doDeleteCompany() {
  var reqData = {
    "uid" : this.uid,
    "username" : this.username,
    "section": "work",
    "action": "delete",
    "cid": this.cid,
  };

  console.log(reqData);

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
    if (data.success) {
      me.$emit("delete-company", {
        cid: me.cid,
      });
    } else {
      popups.alert("Delete failed");
    }
  });
}

export default {
  computed: {
    hasStartYear: hasStartYear,
    hasEndYear: hasEndYear,
  },

  methods: {
    cancelEdit: cancelEdit,
    saveChanges: saveChanges,
    deleteCompany: deleteCompany,
    updateCompanyInfo: updateCompanyInfo,
    updateModelData: updateModelData,
    doDeleteCompany: doDeleteCompany,
    googleSearchURL: utils.googleSearchURL,
  },

  mounted: init,
}
