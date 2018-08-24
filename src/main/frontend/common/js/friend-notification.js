import $ from "jquery"
import common from "heya/home/js/common.js"
import debug from "heya/common/js/debug.js"
import utils from "heya/common/js/utils.js"

function loadFriendNotifications() {
  if (this.debug) {
    this.notifications.friendRequests = debug.friendRequests();
    this.notifications.requestReplies = debug.requestReplies();
    return;
  }

  var me = this;
  $.ajax({
    type: "GET",
    url: "friends/" + me.username + "/notifications",
    dataType: "json",
    encode: true,
  }).done(function(data) {
    console.log(data);
    if (data.success) {
      me.notifications.friendRequests = data.result.friendRequests;
      me.notifications.requestReplies = data.result.requestReplies;
    }
  });
}

function mounted() {
  this.loadFriendNotifications();
}

function clickMenu() {
  this.$emit("click-right-menu", {
    rightMenu: "friends",
  });
}

function menuIsEmpty() {
  return this.notifications.friendRequests.length == 0 &&
         this.notifications.requestReplies.length == 0;
}

function acceptFriendRequest(request, index) {
  if (this.debug) {
    request.lastupdate = (new Date()).getTime();
    this.notifications.requestReplies.push(request);
    this.notifications.friendRequests.splice(index, 1);
    return;
  }

  var me = this;

  let formData = {
    friendUsername: request.friendUsername,
  }
  $.ajax({
    type: "POST",
    url: "friends/" + me.username + "/acceptfriend",
    data: formData,
    dataType: "json",
    encode: true,
  }).done(function(data) {
    if (data.success) {
      request.lastupdate = (new Date()).getTime();
      me.notifications.requestReplies.push(request);
      me.notifications.friendRequests.splice(index, 1);
    }
  });
}

function ignoreFriendRequest(request, index) {
  if (this.debug) {
    this.notifications.friendRequests.splice(index, 1);
    return;
  }

  var me = this;
  
  let formData = {
    friendUsername: request.friendUsername,
  }
  $.ajax({
    type: "POST",
    url: "friends/" + me.username + "/ignorerequest",
    data: formData,
    dataType: "json",
    encode: true,
  }).done(function(data) {
    if (data.success) {
      me.notifications.friendRequests.splice(index, 1);
    }
  });
}

function markNotificationsRead() {
  if (this.debug) {
    for (let reply of this.notifications.requestReplies) {
      if (reply.status === "ACCEPTED") {
        reply.status = "CONFIRMED";
      }
    }
    return;
  }

  var me = this;

  let maxTimeStamp = 0;
  for (let reply of this.notifications.requestReplies) {
    if (reply.status == "ACCEPTED") {
      maxTimeStamp = Math.max(maxTimeStamp, reply.lastupdate);
    }
  }

  if (maxTimeStamp === 0) {
    return;
  }

  let formData = {
    "maxTimeStamp": maxTimeStamp,
  }

  $.ajax({
    type: "POST",
    url: "friends/" + me.username + "/readnotifications",
    data: formData,
    dataType: "json",
    encode: true,
  }).done(function(data) {
    for (let reply of me.notifications.requestReplies) {
      if (reply.status === "ACCEPTED") {
        reply.status = "CONFIRMED";
      }
    }
  });
}

function hasPendingNotification() {
  if (this.notifications.friendRequests.length > 0) {
    return true;
  }

  for (let reply of this.notifications.requestReplies) {
    if (reply.status === "ACCEPTED") {
      return true;
    }
  }
  return false;
}

function formTimeStamp(time) {
  return utils.formatDate(time);
}

export default {
  methods:{
    profileImageURL: common.profileImageURL,
    loadFriendNotifications: loadFriendNotifications,
    clickMenu: clickMenu,
    acceptFriendRequest: acceptFriendRequest,
    ignoreFriendRequest: ignoreFriendRequest,
    markNotificationsRead: markNotificationsRead,
    formTimeStamp: formTimeStamp,
  },

  computed: {
    menuIsEmpty: menuIsEmpty,
    hasPendingNotification: hasPendingNotification,
  },

  mounted: mounted,
}
