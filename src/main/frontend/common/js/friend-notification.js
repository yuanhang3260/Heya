import $ from "jquery"
import common from "heya/home/js/common.js"
import debug from "heya/common/js/debug.js"

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
    this.notifications.requestReplies.push(request);
    this.notifications.friendRequests.splice(index, 1);
    return;
  }

  var me = this;
  $.ajax({
    type: "POST",
    url: "friends/" + me.username + "/acceptfriend/" + request.friendName,
    dataType: "json",
    encode: true,
  }).done(function(data) {
    if (data.success) {
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
  $.ajax({
    type: "POST",
    url: "friends/" + me.username + "/ignorerequest/" + request.friendName,
    dataType: "json",
    encode: true,
  }).done(function(data) {
    if (data.success) {
      me.notifications.friendRequests.splice(index, 1);
    }
  });
}

function hasPendingNotification() {
  return this.notifications.friendRequests.length > 0 || !this.hasRead;
}

export default {
  methods:{
    profileImageURL: common.profileImageURL,
    loadFriendNotifications: loadFriendNotifications,
    clickMenu: clickMenu,
    acceptFriendRequest: acceptFriendRequest,
    ignoreFriendRequest: ignoreFriendRequest,
  },

  computed: {
    menuIsEmpty: menuIsEmpty,
    hasPendingNotification: hasPendingNotification,
  },

  mounted: mounted,
}
