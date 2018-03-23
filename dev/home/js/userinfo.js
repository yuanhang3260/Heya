import $ from "jquery";
import utils from "heya/common/js/utils.js"
import debug from "heya/common/js/debug.js"

function loadUserInfo() {
  var formData = {
    "uid" : this.uid,
  };

  // Process the form.
  var me = this;
  $.ajax({
      type: "GET",
      url: "getuserinfo",
      data: formData,
      dataType: "json",
      encode: true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {

    }
  });
};

function refreshUserInfo(info) {
  this.work = info.work.sort(utils.sortByYearDesc);
  this.education = info.education.sort(utils.sortByYearDesc);
  for (let place of info.places) {
    if (place.current) {
      this.live = place;
    }
    if (place.hometown) {
      this.hometown = place;
    }
  }
}

function isCurrentYear(year) {
  return year === (new Date()).getFullYear();
}

function workVerb(year) {
  return (isCurrentYear(year) ? "Works" : "Worked") + " at";
}

function studyVerb(year) {
  return (isCurrentYear(year) ? "Studies" : "Studied") + " at";
}

function googleSearchURL(target) {
  return "https://www.google.com/search?q=" + target;
}

function googleMapURL(place) {
  return "https://www.google.com/maps/search/" + place;
}

function created() {}

function beforeMount() {
  window.setTimeout(() => {
    if (!this.debug) {
      this.loadUserInfo();
    } else {
      this.loading = false;
      this.refreshUserInfo(debug.userInfo());
    }
  }, 2000);
}

export default {
  computed: {},
  methods: {
    loadUserInfo: loadUserInfo,
    refreshUserInfo: refreshUserInfo,
    workVerb: workVerb,
    studyVerb: studyVerb,
    googleSearchURL: googleSearchURL,
    googleMapURL: googleMapURL,
  },
  created: created,
  beforeMount: beforeMount,
}
