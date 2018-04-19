import $ from "jquery";
import "bootstrap/dist/css/bootstrap.min.css";

import utils from "heya/common/js/utils.js"
import popups from "heya/common/js/popups.js"

function init() {
  if (this.initData) {
    this.pid = this.initData.pid;
    this.name = this.initData.name;
  }
}

function clickEdit() {
  this.nameInput = this.name;
  this.mode = "edit";
}

function clickCancel() {
  this.mode = "display";
}

function clickSave() {
  if (!this.nameInput) {
    popups.alert("Place cannot be empty");
    return;
  }

  if (!this.debug) {
    if (this.pid) {
      this.updatePlaceInfo();
    } else {
      this.addPlaceInfo();
    }
  } else {
    this.updateModelData(100);
  }

  this.mode = "display";
}

function updateModelData(pid) {
  if (this.place) {
    this.name = this.nameInput;
  } else {
    // Add new place.
    this.pid = pid;
    this.name = this.nameInput;
  }
}

function addPlaceInfo() {
  // Send AJAX to update place info.
  var formData = {
    "_method": "POST",
    "name": this.nameInput,
    "current": this.type === "current",
    "hometown": this.type === "hometown",
  };

  // Process the form.
  var me = this;
  $.ajax({
    type : "POST",
    url : "userinfo/" + me.username + "/places",
    data : formData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);

    if (data.success) {
      me.updateModelData(data.result["placeId"]);
    } else {
      popups.alert("Update failed");
    }
  });
}

function updatePlaceInfo() {
  // Send AJAX to update place info.
  var formData = {
    "_method": "PUT",
    "name": this.nameInput,
    "current": this.type === "current",
    "hometown": this.type === "hometown",
  };

  // Process the form.
  var me = this;
  $.ajax({
    type : "POST",
    url : "userinfo/" + me.username + "/places/" + this.pid,
    data : formData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);

    if (data.success) {
      me.updateModelData();
      me.pid = data.result["placeId"];
    } else {
      popups.alert("Update failed");
    }
  });
}

function clickDelete() {
  let me = this;
  popups.confirm({
    message: "Are you sure to delete place " + this.name + "?",
    task: function() {
      if (!me.debug) {
        me.doDeletePlace();
      } else {
        me.name = null;
        me.pid = null;
        me.mode = "display";
      }
    }
  });
}

function doDeletePlace() {
  var formData = {
    "_method": "DELETE",
    "pid": this.pid,
  };

  var me = this;
  $.ajax({
    type : "POST",
    url : "userinfo/" + me.username + "/places/" + this.pid,
    data : formData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      me.name = null;
      me.pid = null;
      me.mode = "display";
    } else {
      popups.alert("Delete failed");
    }
  });
}

export default {
  computed: {
  },

  methods: {
    clickCancel: clickCancel,
    clickEdit: clickEdit,
    clickSave: clickSave,
    clickDelete: clickDelete,
    doDeletePlace: doDeletePlace,
    addPlaceInfo: addPlaceInfo,
    updatePlaceInfo: updatePlaceInfo,
    updateModelData: updateModelData,
    googleMapURL: utils.googleMapURL,
  },

  mounted: init,
}
