import $ from "jquery";
import utils from "heya/common/js/utils.js"

function handleDeleteSchool(payload) {
  let index = -1;
  for (let i = 0; i < this.educationInfo.length; i++) {
    if (this.educationInfo[i].sid === payload.sid) {
      index = i;
    }
  }
  if (index >= 0) {
    this.educationInfo.splice(index, 1);
  }
}

function cancelEdit() {
  this.addingNew = false;
}

function addNewSchool(education) {
  if (this.debug) {
    if (education.school) {
      this.educationInfo.push(education);
    }
    this.addingNew = false;
    return;
  }

  // Send AJAX to update school info.
  var formData = {};
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
    url : "userinfo/" + me.username + "/education",
    data : formData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);

    if (data.success) {
      education.sid = data.result.schoolId;
      me.educationInfo.push(education);
    } else {
      popups.alert("Update failed");
    }
    me.addingNew = false;
  }).fail(function() {
    popups.alert("Update failed");
  });
}

function sortedSchools() {
  return this.educationInfo.sort(utils.sortByYearDesc);
}

export default {
  computed: {
    sortedSchools,
  },

  methods: {
    handleDeleteSchool: handleDeleteSchool,
    cancelEdit: cancelEdit,
    addNewSchool: addNewSchool,
  }
}
