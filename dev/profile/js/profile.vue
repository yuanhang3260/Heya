<template>
<div class="profile-view-container">
  <div class="profile-menu">
    <ul>
      <li v-bind:class="{'item-selected': basicSelected}" v-on:click="selected='basic'" class="profile-basic-item">
        Basic Infomation
      </li>
      <li v-bind:class="{'item-selected': educationSelected}" v-on:click="selected='education'" class="profile-education-item">
        Education
      </li>
      <li v-bind:class="{'item-selected': workSelected}" v-on:click="selected='work'" class="profile-work-item">
        Work
      </li>
      <li v-bind:class="{'item-selected': placesSelected}" v-on:click="selected='places'" class="profile-places-item">
        Places Lived
      </li>
      <li v-bind:class="{'item-selected': otherSelected}" v-on:click="selected='other'" class="profile-other-item">
        Other
      </li>
    </ul>
  </div>
  <div class="profile-panel">
    <profile-basic v-show="basicSelected" :uid="uid" :username="username" :basicInfo="basicUserInfo" :debug=debug></profile-basic>
  </div>
</div>

</template>

<script>
import profile from "./profile.js";

import profileBasic from "./profile-basic.vue";

export default {
  name: "profile",
  components: {
    profileBasic,
  },
  props: {
    uid: {
      type: Number,
      default: -1,
    },
    username: {
      type: String,
      default: "default",
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      selected: "basic",
      userinfo: {},
    }
  },
  computed: profile.computed,
  methods: profile.methods,
  beforeMount: profile.beforeMount,
}

</script>

<style lang="scss">
@import "~heya/profile/css/profile.scss"
</style>