import $ from "jquery";
import ImageClipper from "heya/common/js/image-clipper.js";
import common from "./common.js";
import popups from "heya/common/js/popups.js";

var clipper = null;

function mounted() {
  this.getFriendsCount();
  this.getPostsCount();
}

function clickProfileImage() {
  if (this.editable) {
    if (!clipper) {
      clipper = new ImageClipper({
        title: "Update Profile Image",
        callback: this.updateProfileImage,
        outputFormat: "jpeg",
      });
    }

    clipper.open();
  } else {
    this.$bus.$emit("view-images", {
      images: [
        {
          url: this.profileImageURL,
        }
      ],
      imageIndex: 0,
    });
  }
}

function addfriend() {
  let me = this;
  if (me.debug) {
    me.friendRelationship = "FRIEND_REQUEST_SENT";
    return;
  }

  let formData = {
    friendUsername: me.username,
  };

  $.ajax({
    type: "POST",
    url: "friends/" + me.viewerUsername + "/addfriend",
    data: formData,
    dataType: "json",
    encode: true,
  }).done(function(data) {
    if (data.success) {
      me.friendRelationship = "FRIEND_REQUEST_SENT";
    } else {
      popups.alert(data.error);
    }
  });
}

function unfriend() {
  let me = this;
  function _unfriend(resolve, reject) {
    if (me.debug) {
      me.friendRelationship = "NOT_FRIENDS";
      resolve();
      return;
    }

    let formData = {
      friendUsername: me.username,
    };

    $.ajax({
      type: "POST",
      url: "friends/" + me.viewerUsername + "/unfriend",
      data: formData,
      dataType: "json",
      encode: true,
    }).done(function(data) {
      if (data.success) {
        me.friendRelationship = "NOT_FRIENDS";
        resolve();
      } else {
        reject(data.error);
      }
    }).fail(function(data) {
      reject("Failed to unfriend " + me.username);
    });
  }

  popups.confirm({
    message: "Are you sure to unfriend " + me.username + "?",
    task: _unfriend,
    syncWait: true,
  });
}

function updateProfileImage(filename, blob) {
  var formData = new FormData();
  formData.append("filename", filename);
  formData.append("imagedata", blob);

  $.ajax({
    type: "POST",
    url: "profileimage/profile/" + this.username,
    data: formData,
    processData: false,
    contentType: false,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
  });
}

function getFriendsCount() {
  if (this.debug) {
    this.friendscount = 120;
    return;
  }

  let me = this;

  var formData = {
    "viewerUsername": me.viewerUsername,
  };

  $.ajax({
    type: "GET",
    url: "friends/" + this.username + "/friendscount",
    data: formData,
    dataType: "json",
    encode: true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      me.friendscount = data.result.friendscount;
      if (data.result.friendRelationship) {
        me.friendRelationship = data.result.friendRelationship;
      }
    }
  });
}

function getPostsCount() {
  if (this.debug) {
    this.postscount = 5;
    return;
  }

  let me = this;
  $.ajax({
    type: "GET",
    url: "post/" + this.username + "/postscount",
    dataType: "json",
    encode: true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      me.postscount = data.result.postscount;
    }
  });
}

function friendlyNum(num) {
  if (num <= 100 * 1000) {
    return num;
  } else if (num <= 1000 * 1000) {
    return (num / 1000).toFixed(0) + "K";
  } else {
    return (num / 1000000).toFixed(0) + "M";
  }
}

function friendsCountDisplay() {
  return friendlyNum(this.friendscount);
}

function postsCountDisplay() {
  return friendlyNum(this.postscount);
}

export default {
  computed: {
    friendsCountDisplay,
    postsCountDisplay,
  },

  methods: {
    clickProfileImage: clickProfileImage,
    updateProfileImage: updateProfileImage,
    profileImageURL: common.profileImageURL,
    coverImageURL: common.profileCoverImageURL,
    getFriendsCount: getFriendsCount,
    getPostsCount: getPostsCount,
    addfriend: addfriend,
    unfriend: unfriend,
  },

  mounted: mounted,
}
