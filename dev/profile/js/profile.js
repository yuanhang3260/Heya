import debug from "heya/common/js/debug.js";

function loadUserInfo() {
  var formData = {
    "uid": this.uid,
    "username": this.username,
  };

  var me = this;
  $.ajax({
      type : "GET",
      url : "getuserinfo",
      data : formData,
      dataType : "json",
      encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {

    }
  });
};

function beforeMount() {
  if (!this.debug) {
    loadUserInfo();
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

function basicUserInfo() {
  return {
    name: this.userinfo.name,
    email: this.userinfo.email,
    phone: this.userinfo.phone,
    birth: this.userinfo.birth,
  }
}

export default {
  computed: {
    basicSelected: basicSelected,
    educationSelected: educationSelected,
    workSelected: workSelected,
    placesSelected: placesSelected,
    otherSelected: otherSelected,
    basicUserInfo: basicUserInfo,
  },

  methods: {
    loadUserInfo: loadUserInfo,
  },

  beforeMount: beforeMount,
}