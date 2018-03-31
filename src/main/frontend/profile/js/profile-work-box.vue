<template>
    
<div class="profile-info-box">
  <profile-work-edit v-show="mode==='edit'" :show="mode==='edit'" :cid="cid" :type="'modify'" :company="company" :position="position" :yearStart="yearStart" :yearEnd="yearEnd" v-on:cancel-edit="cancelEdit" v-on:save-changes="saveChanges" ></profile-work-edit>

  <div v-show="mode==='display'" class="profile-info-display">
    <div class="profile-info">
      <a :href="googleSearchURL(company)" class="profile-name company-info" target="_blank">{{company}}</a>
      <p class="profile-detail">
        <span v-if="position" class="position-info">{{position}}</span>
        <span v-if="position"> - </span>
        <span v-if="hasStartYear"> from </span>
        <span v-if="hasStartYear" class="start-year"> {{yearStart}}</span>
        <span v-if="hasEndYear"> to </span>
        <span v-if="hasEndYear" class="end-year"> {{yearEnd}}</span>
      </p>
    </div>
    <i class="fa fa-laptop profile-icon"></i>
    <div class="corner-buttons">
      <i v-on:click="mode='edit'" class="fa fa-edit profile-edit-button"></i>
      <i v-on:click="deleteCompany" class="fa fa-ban profile-delete-button"></i>
    </div>
  </div>
</div>

</template>

<script>
import profileWorkBox from "./profile-work-box.js"
import profileWorkEdit from "./profile-work-edit.vue"

export default {
  name: "profile-work-box",
  components: {
    profileWorkEdit,
  },
  props: {
    uid: {
      type: Number,
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
      cid: null,
      company: null,
      position: null,
      yearStart: null,
      yearEnd: null,
    }
  },
  computed: profileWorkBox.computed,
  methods: profileWorkBox.methods,
  mounted: profileWorkBox.mounted,
}

</script>

<style lang="scss">
</style>