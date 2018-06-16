<template>

<div class="card home-profile-container">
  <div class="card-img-top cover-img-container">
    <img class="cover-img" v-bind:src="coverImageURL">
  </div>
  <div class="profile-img-container">
    <img class="profile-img" v-bind:src="profileImageURL" v-on:click="clickProfileImage">
  </div>
  <div class="card-body username-container">{{username}}</div>
  <div class="card-body follower-container">
    <div class="follower">
      <div class="follower-title">Followers</div>
      <div class="follower-num">{{followersDisplay}}</div>
    </div>
    <div class="follower-split-line"></div>
    <div class="follower">
      <div class="follower-title">Following</div>
      <div class="follower-num">{{followingDisplay}}</div>
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
      default: "default",
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
      followers: 12000000,
      following: 235,
    }
  },
  computed: avatar.computed,
  methods: avatar.methods,
}
</script>

<style lang="scss">
@import "~heya/home/css/avatar.scss"
</style>
