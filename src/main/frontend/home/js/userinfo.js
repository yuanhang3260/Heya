import $ from "jquery";
import utils from "heya/common/js/utils.js"
import debug from "heya/common/js/debug.js"

function loadUserInfo() {
  // Process the form.
  var me = this;
  $.ajax({
      type: "GET",
      url: "userinfo/" + me.username,
      dataType: "json",
      encode: true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      me.loading = false;
      me.refreshUserInfo(data.result);
    }
  });
};

function refreshUserInfo(info) {
  if (info.work) {
    this.work = info.work.sort(utils.sortByYearDesc);
  }
  if (info.education) {
    this.education = info.education.sort(utils.sortByYearDesc);
  }
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
  return ((year && isCurrentYear(year.end)) ? "Works" : "Worked") + " at";
}

function studyVerb(year) {
  return ((year && isCurrentYear(year.end)) ? "Studies" : "Studied") + " at";
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
  }, 500);
}

export default {
  computed: {},
  methods: {
    loadUserInfo: loadUserInfo,
    refreshUserInfo: refreshUserInfo,
    workVerb: workVerb,
    studyVerb: studyVerb,
    googleSearchURL: utils.googleSearchURL,
    googleMapURL: utils.googleMapURL,
  },
  created: created,
  beforeMount: beforeMount,
}
