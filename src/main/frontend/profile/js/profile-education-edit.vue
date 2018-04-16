<template>

<div class="profile-info-edit">
  <form method="post">
    <div class="form-group edit-school">
      <label class="profile-edit-label">School</label>
      <input v-model="schoolInput" type="text" name="school" class="form-control profile-edit-input">
    </div>
    <div class="form-group edit-year">
      <label class="profile-edit-label">Year</label>
      <select v-model="yearStartInput" class="form-control profile-edit-select" name="start">
        <option val="--" :selected="!yearStartInput">--</option>
        <option v-for="year in yearSelectOptions()" :selected="yearStartInput===year" val=year>{{year}}</option>
      </select>
      <span class="year-to">to</span>
      <select v-model="yearEndInput" class="form-control profile-edit-select" name="end">
        <option val="--" :selected="!yearEndInput">--</option>
        <option v-for="year in yearSelectOptions()" :selected="yearEndInput===year" val=year>{{year}}</option>
      </select>
    </div>
    <div class="form-group edit-major">
      <label class="profile-edit-label">Major</label>
      <input v-model="majorInput" type="text" name="major" class="form-control profile-edit-input">
    </div>
    <div class="button-box">
      <button v-on:click="clickSave" type="button" class="btn btn-success save-btn">Save Changes</button>
      <button v-on:click="clickCancel" type="button" class="btn btn-light cancel-btn">Cancel</button>
    </div>
    <div v-show="errMsg" class="alert alert-danger update-error-msg" role="alert">{{errMsg}}</div>
  </form>
</div>

</template>

<script>
import profileEducation from "./profile-education-edit.js"

export default {
  name: "profile-education-edit",
  components: {
  },
  props: {
    sid: {  // school id
      type: String,
      default: null,
    },
    type: {
      type: String,
      default: "modify",
    },
    show: {
      type: Boolean,
      default: false,
    },
    school: {
      type: String,
      default: null,
    },
    major: {
      type: String,
      default: null,
    },
    yearStart: {
      type: Number,
      default: null,
    },
    yearEnd: {
      type: Number,
      default: null,
    },
  },
  data () {
    return {
      schoolInput: null,
      majorInput: null,
      yearStartInput: null,
      yearEndInput: null,
      errMsg: null,
    }
  },
  computed: profileEducation.computed,
  watch: {
    show: function(newVal, oldVal) {
      if (newVal) {
        this.setInput();
      }
      this.errMsg = null;
    }
  },
  methods: profileEducation.methods,
}

</script>

<style scoped lang="scss">
</style>