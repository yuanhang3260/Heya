<template>

<div class="card home-profile-container">
  <div class="card-img-top cover-img-container">
    <img class="cover-img" v-bind:src="coverImageURL()">
  </div>
  <div class="profile-img-container">
    <img class="profile-img" v-bind:src="profileImageURL()" v-on:click="clickProfileImage">
  </div>
  <div class="card-body username-container">{{username}}</div>
  <div class="card-body social-container">
    <div class="social">
      <div class="social-title">Friends</div>
      <div class="social-num">{{friendsCountDisplay}}</div>
    </div>
    <div class="social-split-line"></div>
    <div class="social">
      <div class="social-title">Posts</div>
      <div class="social-num">{{postsCountDisplay}}</div>
    </div>
  </div>
  <div v-if="!editable" class="add-friend-container">
    <button v-show="friendRelationship=='NOT_FRIENDS'" v-on:click="addfriend" class="btn btn-outline-primary add-friend-btn" type="submit">
      <i class="fa fa-user-plus"></i>
      <span>Add Friend</span>
    </button>
    <button v-show="friendRelationship=='FRIENDS'" v-on:click="unfriend" class="btn are-friends-btn" type="submit">
      <i class="fa fa-check"></i>
      <span>Friends</span>
    </button>
    <button v-show="friendRelationship=='FRIEND_REQUEST_SENT'" class="btn friend-request-sent-btn" type="submit">
      <i class="fa fa-user-plus"></i>
      <span>Friend Request Sent</span>
    </button>
  </div>

  </div>
</div>

</template>

<script>
import avatar from "./avatar.js"

export default {
  name: "avatar",
  components: {
  },
  props: {
    uid: {
      type: String,
      default: null,
    },
    username: {
      type: String,
      default: null,
    },
    viewerId: {
      type: String,
      default: null,
    },
    viewerUsername: {
      type: String,
      default: null,
    },
    editable: {
      type: Boolean,
      default: false,
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      friendscount: 0,
      postscount: 0,
      friendRelationship: "NOT_FRIENDS",
    }
  },
  computed: avatar.computed,
  methods: avatar.methods,
  mounted: avatar.mounted,
}
</script>

<style lang="scss">
@import "~heya/home/css/avatar.scss"
</style>
