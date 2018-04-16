<template>
    
<div class="profile-info-box">
  <profile-education-edit v-show="mode==='edit'" :show="mode==='edit'" :sid="sid" :type="'modify'" :school="school" :major="major" :yearStart="yearStart" :yearEnd="yearEnd" v-on:cancel-edit="cancelEdit" v-on:save-changes="saveChanges" ></profile-education-edit>

  <div v-show="mode==='display'" class="profile-info-display">
    <div class="profile-info">
      <a :href="googleSearchURL(school)" class="profile-name school-info" target="_blank">{{school}}</a>
      <p class="profile-detail">
        <span v-if="hasValidYearInfo">Class of</span>
        <span v-if="hasValidYearInfo" class="year-info"> {{yearEnd}}</span>
        <span v-if="hasValidYearInfo && major"> - </span>
        <span v-if="major" class="major-info">{{major}}</span>
      </p>
    </div>
    <i class="fa fa-graduation-cap profile-icon"></i>
    <div class="corner-buttons">
      <i v-on:click="mode='edit'" class="fa fa-edit profile-edit-button"></i>
      <i v-on:click="deleteSchool" class="fa fa-ban profile-delete-button"></i>
    </div>
  </div>
</div>

</template>

<script>
import profileEducationBox from "./profile-education-box.js"
import profileEducationEdit from "./profile-education-edit.vue"

export default {
  name: "profile-education-box",
  components: {
    profileEducationEdit,
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
    initData: {
      type: Object,
      default: function() { return {}; },
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      mode: "display",
      sid: null,
      school: null,
      major: null,
      yearStart: null,
      yearEnd: null,
    }
  },
  computed: profileEducationBox.computed,
  methods: profileEducationBox.methods,
  mounted: profileEducationBox.mounted,
}

</script>

<style scoped lang="scss">
</style>