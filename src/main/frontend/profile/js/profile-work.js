import $ from "jquery";

function handleDeleteCompany(payload) {
  let index = -1;
  for (let i = 0; i < this.workInfo.length; i++) {
    if (this.workInfo[i].cid === payload.cid) {
      index = i;
    }
  }
  if (index >= 0) {
    this.workInfo.splice(index, 1);
  }
}

function cancelEdit() {
  this.addingNew = false;
}

function addNewCompany(work) {
  if (this.debug) {
    if (work.company) {
      this.workInfo.push(work);
    }
    this.addingNew = false;
    return;
  }

  // Send AJAX to update company info.
  var reqData = {
    "uid" : this.uid,
    "username" : this.username,
    "section": "work",
    "action": "add",
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
      work.cid = data.companyId;
      me.workInfo.push(work);
    } else {
      popups.alert("Update failed");
    }
    me.addingNew = false;
  });
}

export default {
  methods: {
    handleDeleteCompany: handleDeleteCompany,
    cancelEdit: cancelEdit,
    addNewCompany: addNewCompany,
  }
}
