import $ from "jquery";

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
  var reqData = {
    "uid" : this.uid,
    "username" : this.username,
    "section": "education",
    "action": "add",
  };

  if (education.school) {
    reqData["school"] = education.school;
  }
  if (education.major) {
    reqData["major"] = education.major;
  }
  if (education.year && (education.year.start || education.year.end)) {
    reqData["year"] = JSON.stringify({
      start: education.year.start,
      end: education.year.end,
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
      education.sid = data.schoolId;
      me.educationInfo.push(education);
    } else {
      popups.alert("Update failed");
    }
    me.addingNew = false;
  });
}

export default {
  methods: {
    handleDeleteSchool: handleDeleteSchool,
    cancelEdit: cancelEdit,
    addNewSchool: addNewSchool,
  }
}
