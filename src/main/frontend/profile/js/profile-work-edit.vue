<template>

<div class="profile-info-edit">
  <form method="post">
    <div class="form-group edit-company">
      <label class="profile-edit-label">company</label>
      <input v-model="companyInput" type="text" name="company" class="form-control profile-edit-input">
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
    <div class="form-group edit-position">
      <label class="profile-edit-label">position</label>
      <input v-model="positionInput" type="text" name="position" class="form-control profile-edit-input">
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
import profileWorkEdit from "./profile-work-edit.js"

export default {
  name: "profile-work-edit",
  components: {
  },
  props: {
    cid: {  // company id
      type: Number,
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
    company: {
      type: String,
      default: null,
    },
    position: {
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
      companyInput: null,
      positionInput: null,
      yearStartInput: null,
      yearEndInput: null,
      errMsg: null,
    }
  },
  computed: profileWorkEdit.computed,
  watch: {
    show: function(newVal, oldVal) {
      if (newVal) {
        this.setInput();
      }
      this.errMsg = null;
    }
  },
  methods: profileWorkEdit.methods,
}

</script>

<style lang="scss">
</style>