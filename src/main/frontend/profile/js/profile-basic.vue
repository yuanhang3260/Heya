<template>

<div class="subpanel profile-basic-panel">
  <ul v-show="mode === 'display'" class="basic-info-display">
    <li v-if="name" class="name-info">
      <i class="basic-info-icon icon-name fa fa-user"></i>
      <span>{{name}}</span>
    </li>
    <li v-if="email" class="email-info">
      <i class="basic-info-icon icon-email fa fa-envelope"></i>
      <span>{{email}}</span>
    </li>
    <li v-if="phone" class="phone-info">
      <i class="basic-info-icon icon-phone fa fa-mobile"></i>
      <span>{{phone}}</span>
    </li>
    <li v-if="birth" class="birth-info">
      <i class="basic-info-icon icon-birth fa fa-birthday-cake"></i>
      <span>{{birth}}</span>
    </li>
    <div v-if="editable" class="edit-button" v-on:click="clickEdit">
      <i class="fa fa-edit"></i>
      <span>Edit profile basic info</span>
    </div>
  </ul>
  <div v-show="mode === 'edit'" class="profile-info-edit basic-info-edit">
    <form method="post">
      <div class="form-group edit-name">
        <label class="profile-edit-label">Name</label>
        <input v-model.lazy="nameInput" type="text" name="name" class="form-control profile-edit-input">
      </div>
      <div class="form-group edit-birth">
        <label class="profile-edit-label">Birth</label>
        <select v-model.lazy="birthYearInput" class="form-control profile-edit-select" name="year">
          <option val="--" :selected="!birthYearInput">--</option>
          <option v-for="year in yearSelectOptions()" :selected="birthYearInput===year" val=year>{{year}}</option>
        </select>
        <select v-model.lazy="birthMonthInput" class="form-control profile-edit-select" name="month">
          <option val="--" :selected="!birthMonthInput">--</option>
          <option v-for="month in monthSelectOptions()" :selected="birthMonthInput===month" val=month>{{month}}</option>
        </select>
        <select v-model.lazy="birthDateInput" class="form-control profile-edit-select" name="date">
          <option val="--" :selected="!birthDateInput">--</option>
          <option v-for="date in dateSelectOptions()" :selected="birthDateInput===date" val=date>{{date}}</option>
        </select>
      </div>
      <div class="form-group edit-email">
        <label class="profile-edit-label">Email</label>
        <input v-model.lazy="emailInput" type="text" name="email" class="form-control profile-edit-input">
      </div>
      <div class="form-group edit-phone">
        <label class="profile-edit-label">Phone</label>
        <input v-model.lazy="phoneInput" type="text" name="phone" class="form-control profile-edit-input">
      </div>
      <div class="button-box">
        <button v-on:click="clickSave" type="button" class="btn btn-success save-btn">Save Changes</button>
        <button v-on:click="clickCancel" type="button" class="btn btn-light cancel-btn">Cancel</button>
      </div>
      <div v-show="errMsg" class="alert alert-danger update-error-msg" role="alert">{{errMsg}}</div>
    </form>
  </div>
</div>

</template>

<script>
import profileBasic from "./profile-basic.js"

export default {
  name: "profile-basic",
  components: {
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
    editable: {
      type: Boolean,
      default: false,
    },
    basicInfo: {
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
      name: null,
      nameInput: null,
      email: null,
      emailInput: null,
      phone: null,
      phoneInput: null,
      birth: null,
      birthYearInput: null,
      birthMonthInput: null,
      birthDateInput: null,
      mode: "display",
      disableButtom: false,
      errMsg: null,
    }
  },
  computed: profileBasic.computed,
  watch: {
    // Watch basicInfo propagated from parent, and update local data.
    basicInfo: {
      handler: function() {
        this.name = this.basicInfo.name;
        this.email = this.basicInfo.email;
        this.phone = this.basicInfo.phone;
        this.birth = this.basicInfo.birth;
      },
    }
  },
  methods: profileBasic.methods,
  // beforeCreate: function() { console.log("basic beforeCreate"); },
  // created: function() { console.log("basic created"); },
  // beforeMount: function() { console.log("basic beforeMount"); },
  // mounted: function() { console.log("basic mounted"); },
}

</script>

<style lang="scss">
@import "~heya/profile/css/profile-basic.scss"
</style>