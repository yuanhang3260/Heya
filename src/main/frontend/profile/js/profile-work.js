import $ from "jquery";
import utils from "heya/common/js/utils.js"

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
  var formData = {};
  if (work.company) {
    formData["company"] = work.company;
  }
  if (work.position) {
    formData["position"] = work.position;
  }
  if (work.year) {
    if (work.year.start && !isNaN(work.year.start)) {
      formData["startYear"] = work.year.start;
    }
    if (work.year.end && !isNaN(work.year.end)) {
      formData["endYear"] = work.year.end;
    }
  }

  // Process the form.
  var me = this;
  $.ajax({
    type : "POST",
    url : "userinfo/" + me.username + "/work",
    data : formData,
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

function sortedCompanies() {
  return this.workInfo.sort(utils.sortByYearDesc);
}

export default {
  computed: {
    sortedCompanies,
  },

  methods: {
    handleDeleteCompany: handleDeleteCompany,
    cancelEdit: cancelEdit,
    addNewCompany: addNewCompany,
  }
}
