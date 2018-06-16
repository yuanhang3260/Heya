<template>

<div class="subpanel profile-work-panel">
  <profile-work-box v-for="work in sortedCompanies" :key="work.cid" :uid="uid" :username="username" :editable="editable" :initData="work" :debug="debug" v-on:delete-company="handleDeleteCompany"></profile-work-box>

  <div v-if="editable" v-show="!addingNew" class="add-new-item">
    <div v-on:click="addingNew=true" class="add-item-button">
      <i class="fa fa-plus-square add-item-icon"></i>
      <span class="add-item-text">Add a work</span>
    </div>
  </div>
  <profile-work-edit v-show="addingNew" :show="addingNew" :type="'add'" v-on:cancel-edit="cancelEdit" v-on:save-changes="addNewCompany" ></profile-work-edit>
</div>

</template>

<script>
import profileWork from "./profile-work.js"

import profileWorkBox from "./profile-work-box.vue"
import profileWorkEdit from "./profile-work-edit.vue"

export default {
  name: "profile-work",
  components: {
    profileWorkBox,
    profileWorkEdit,
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
    workInfo: {
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

  computed: profileWork.computed, 
  methods: profileWork.methods,
}

</script>

<style lang="scss">
@import "~heya/profile/css/profile-work.scss"
</style>