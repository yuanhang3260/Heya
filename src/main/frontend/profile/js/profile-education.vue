<template>

<div class="subpanel profile-education-panel">
  <profile-education-box v-for="education in sortedSchools" :key="education.sid" :uid="uid" :username="username" :initData="education" :editable="editable" :debug="debug" v-on:delete-school="handleDeleteSchool"></profile-education-box>

  <div v-if="editable" v-show="!addingNew" class="add-new-item">
    <div v-on:click="addingNew=true" class="add-item-button">
      <i class="fa fa-plus-square add-item-icon"></i>
      <span class="add-item-text">Add a school</span>
    </div>
  </div>
  <profile-education-edit v-show="addingNew" :show="addingNew" :type="'add'" v-on:cancel-edit="cancelEdit" v-on:save-changes="addNewSchool" ></profile-education-edit>
</div>

</template>

<script>
import profileEducation from "./profile-education.js"

import profileEducationBox from "./profile-education-box.vue"
import profileEducationEdit from "./profile-education-edit.vue"

export default {
  name: "profile-education",
  components: {
    profileEducationBox,
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
    editable: {
      type: Boolean,
      default: false,
    },
    educationInfo: {
      type: Array,
      default: function() { return []; },
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      addingNew: false,
    }
  },

  computed: profileEducation.computed,
  methods: profileEducation.methods,
}

</script>

<style lang="scss">
@import "~heya/profile/css/profile-education.scss"
</style>