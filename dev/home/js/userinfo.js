import $ from "jquery";
import utils from "heya/common/js/utils.js"

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

function debugUserInfo() {
  return {
    work: [
      {
        "cid": 1,
        "company": "Tintri",
        "position": "Software Engineer Intern",
        "year": {
          "end": 2014,
          "start": 2014
        }
      },
      {
        "cid": 2,
        "company": "Bicasl, SJTU",
        "position": "Research Assistant",
        "year": {
          "end": 2013,
          "start": 2012
        }
      },
      {
        "cid": 3,
        "company": "Google",
        "position": "Software Engineer",
        "year": {
          "end": 2018,
          "start": 2015
        }
      }
    ],
    education: [
      {
        "major": "Electrical Engineering",
        "school": "Shanghai Jiao Tong University",
        "sid": 2,
        "year": {
          "end": 2013,
          "start": 2009
        }
      },
      {
        "major": "Computer Engineering",
        "school": "Carnegie Mellon University",
        "sid": 3,
        "year": {
          "end": 2015,
          "start": 2013
        }
      }
    ],
    places: [
      {
        "current": false,
        "hometown": true,
        "pid": 3,
        "place": "Haimen, Jiangsu Province, China"
      },
      {
        "current": true,
        "hometown": false,
        "pid": 4,
        "place": "Sunnyvale, CA"
      }
    ]
  }
}

function created() {}

function beforeMount() {
  window.setTimeout(() => {
    if (!this.debug) {
      this.loadUserInfo();
    } else {
      this.loading = false;
      this.refreshUserInfo(debugUserInfo());
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
