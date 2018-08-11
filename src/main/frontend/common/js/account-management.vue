<template>

<div class="account-mangement-box">
  <div v-on:click="flipMenu" class="account-box-avatar">
    <img class="avatar-image" v-bind:src="profileImageURL()">
  </div>
  <div v-show="showMenu" class="account-box-menu">
    <div class="account-box-menu-item profile-management">
      <a v-bind:href="'profile?username='+username"><span>My Profile</span></a>
    </div>
    <div class="account-box-menu-item logout">
      <span v-on:click="logout">Logout</span>
    </div>
  </div>
  <div v-show="showMenu" class="account-box-menu-arrow" />
</div>

</template>

<script>
import accountMangement from "./account-management.js" 

export default {
  name: "account-management",
  props: {
    uid: {
      type: String,
      default: null,
    },
    username: {
      type: String,
      default: null,
    },
    debug: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      showMenu: false,
    }
  },
  methods: accountMangement.methods,
};
</script>

<style scoped lang="scss">
</style>
