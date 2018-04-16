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
    <profile-basic v-show="basicSelected" :uid="uid" :username="username" :basicInfo="userinfo" :debug=debug></profile-basic>
    <profile-education v-show="educationSelected" :uid="uid" :username="username" :educationInfo="userinfo.education" :debug=debug></profile-education>
    <profile-work v-show="workSelected" :uid="uid" :username="username" :workInfo="userinfo.work" :debug=debug></profile-work>
    <profile-places v-show="placesSelected" :uid="uid" :username="username" :placesInfo="userinfo.places" :debug=debug></profile-places>
  </div>
</div>

</template>

<script>
import profile from "./profile.js";

import profileBasic from "./profile-basic.vue";
import profileEducation from "./profile-education.vue";
import profileWork from "./profile-work.vue";
import profilePlaces from "./profile-places.vue";

export default {
  name: "profile",
  components: {
    profileBasic,
    profileEducation,
    profileWork,
    profilePlaces,
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
  // beforeCreate: function() { console.log("main beforeCreate"); },
  // created: function() { console.log("main created"); },
  // beforeMount: function() { console.log("main beforeMount"); },
  mounted: profile.mounted,
}

</script>

<style lang="scss">
@import "~heya/profile/css/profile.scss"
</style>