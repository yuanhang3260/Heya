import $ from "jquery";
import debug from "heya/common/js/debug.js";

function loadUserInfo() {
  var me = this;
  $.ajax({
      type : "GET",
      url : "userinfo/" + me.username,
      dataType : "json",
      encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      me.userinfo = data.result;
    }
  });
};

function init() {
  if (!this.debug) {
    this.loadUserInfo();
  } else {
    this.userinfo = debug.userInfo();
    console.log(this.userinfo);
  }
}

function basicSelected() {
  return this.selected === "basic";
}

function educationSelected() {
  return this.selected === "education";
}

function workSelected() {
  return this.selected === "work";
}

function placesSelected() {
  return this.selected === "places";
}

function otherSelected() {
  return this.selected === "other";
}

export default {
  computed: {
    basicSelected: basicSelected,
    educationSelected: educationSelected,
    workSelected: workSelected,
    placesSelected: placesSelected,
    otherSelected: otherSelected,
  },

  methods: {
    loadUserInfo: loadUserInfo,
    init: init,
  },

  mounted: init,
}