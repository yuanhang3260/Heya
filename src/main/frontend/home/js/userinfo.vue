<template>

<div class="card home-profile-detail-container">
  <h6 class="profile-card-title">About <small>-</small> 
  <a href="profile?uid=0&username=snoopy" class="profile-edit-button" >Edit</a></h6>
  <load-animation v-if="loading" :width=220 :height=200 style="margin-top: -15px"></load-animation>

  <div v-for="job of work" class="user-profile-item">
    <i class="user-profile-icon user-work-icon fa fa-laptop"></i>
    <p class="user-profile-detail">
      <span>{{workVerb(job.year)}}</span>
      <a v-bind:href="googleSearchURL(job.company)" class="user-profile-link" target="_blank">{{job.company}}</a>
    </p>
  </div>

  <div v-for="school of education" class="user-profile-item">
    <i class="user-profile-icon user-education-icon fa fa-graduation-cap"></i>
    <p class="user-profile-detail">
      <span>{{studyVerb(school.year)}}</span>
      <a v-bind:href="googleSearchURL(school.school)" class="user-profile-link" target="_blank">{{school.school}}</a>
    </p>
  </div>

  <div v-if="live" class="user-profile-item">    
    <i class="user-profile-icon user-live-icon fa fa-map-marker"></i>
    <p class="user-profile-detail">
      <span>Lives in</span>
      <a v-bind:href="googleMapURL(live.place)" class="user-profile-link" target="_blank">{{live.name}}</a>
    </p>
  </div>

  <div v-if="hometown" class="user-profile-item">
    <i class="user-profile-icon user-hometown-icon fa fa-home"></i>
    <p class="user-profile-detail">
      <span>From</span>
      <a v-bind:href="googleMapURL(hometown.place)" class="user-profile-link" target="_blank">{{hometown.name}}</a>
    </p>
  </div>
</div>

</template>

<script>
import LoadAnimation from "heya/common/js/content-load-animation.vue"
import userinfo from "./userinfo.js"

export default {
  name: "userinfo",
  components: {
    LoadAnimation,
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
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      loading: true,
      work: [],
      education: [],
      live: null,
      hometown: null,
    }
  },
  computed: userinfo.computed,
  methods: userinfo.methods,
  created: userinfo.created,
  beforeMount: userinfo.beforeMount,
}
</script>

<style scoped lang="scss">
@import "~heya/home/css/userinfo.scss"
</style>
