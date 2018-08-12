<template>

<div class="friend-notification-box">
  <div v-on:click.stop="clickMenu" class="friend-notification-icon">
    <i class="fa fa-users"></i>
    <div v-show="hasPendingNotification" class="notifications-red-dot"></div>
  </div>

  <div v-show="showMenu" class="friend-notification-menu" v-bind:class="{ 'empty-menu': menuIsEmpty }">
    <div v-show="menuIsEmpty">
      <span class="empty-notification">No notification</span>
    </div>

    <div v-if="notifications.friendRequests.length > 0" class="friend-menu-header friend-request-headerline">
      <span>Friend Requests</span>
    </div>
    <div v-for="(request, index) in notifications.friendRequests" :key="'friendRequest' + index" class="friend-menu-item friend-request-item">
      <a :href="'Heya/home?username='+request.friendName" target="_blank">
        <img class="menu-item-avatar" :src="request.avatar"/>
      </a>
      <div class="menu-item-text">
        <span><a :href="'Heya/home?username='+request.friendName" target="_blank">{{request.friendName}}</a></span>
      </div>
      <div class="friend-request-button-box">
        <button v-on:click="acceptFriendRequest(request, index)" class="btn btn-outline-primary accept-friend-btn" type="button">
          <span>Accept</span>
        </button>
        <button v-on:click="ignoreFriendRequest(request, index)" class="btn btn-outline-secondary ignore-friend-btn" type="button">
          <span>Ignore</span>
        </button>
      </div>
    </div>

    <div v-if="notifications.requestReplies.length > 0" class="friend-menu-header new-friend-headerline">
      <span>New Friends</span>
    </div>
    <div v-for="(newFriend, index) in notifications.requestReplies" :key="'newFriend' + index" class="friend-menu-item new-friend-item">
      <a :href="'Heya/home?username='+newFriend.friendName" target="_blank">
        <img class="menu-item-avatar" :src="newFriend.avatar"/>
      </a>
      <div class="menu-item-text">
        <a :href="'Heya/home?username='+newFriend.friendName" target="_blank">{{newFriend.friendName}}</a>
        <span v-if="newFriend.status=='ACCEPTED'">accepted your friend request <i class="fa fa-check"></i></span>
        <span v-else>you added this new friend <i class="fa fa-handshake-o"></i></span>
      </div>
      <div class="new-friend-button-box">
      </div>
    </div>
  </div>

  <div v-show="showMenu" class="friend-notification-menu-arrow" />
</div>

</template>

<script>
import friendNotification from "./friend-notification.js" 

export default {
  name: "friend-notification",
  props: {
    uid: {
      type: String,
      default: null,
    },
    username: {
      type: String,
      default: null,
    },
    showMenu: {
      type: Boolean,
      default: false,
    },
    debug: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      notifications: {
        friendRequests: [],
        requestReplies: [],
      },
      hasRead: false,
    }
  },

  watch: {
    showMenu: function(newValue) {
      if (newValue) {
        this.hasRead = true;
      }
    }
  },

  methods: friendNotification.methods,
  computed: friendNotification.computed,
  mounted: friendNotification.mounted,
};
</script>

<style scoped lang="scss">
</style>
