<template>

<div class="profile-info-box" v-bind:class="{'hometown': type==='hometown', 'current-live': type==='current'}">
  <div v-show="!place && mode==='display'" v-on:click="clickEdit" class="add-item-button">
    <i class="fa fa-plus-square add-item-icon"></i>
    <span v-if="type==='current'" class="add-item-text">Add current live</span>
    <span v-if="type==='hometown'" class="add-item-text">Add hometown</span>
  </div>
  <div v-show="mode==='edit'" class="profile-info-edit">
    <form method="post">
      <div class="form-group edit-place">
        <label class="profile-edit-label">Place</label>
        <input v-model="placeInput" type="text" name="place" class="form-control profile-edit-input">
      </div>
      <div class="button-box">
        <button v-on:click="clickSave" type="button" class="btn btn-success save-btn">Save Changes</button>
        <button v-on:click="clickCancel" type="button" class="btn btn-light cancel-btn">Cancel</button>
      </div>
      <div v-show="errMsg" class="alert alert-danger update-error-msg" role="alert">{{errMsg}}</div>
    </form>
  </div>
  <div v-show="place && mode==='display'" class="profile-info-display">
    <div class="profile-info">
      <a :href="googleMapURL(place)" class="profile-name place-info" target="_blank">{{place}}</a>
      <p v-if="type==='current'" class="profile-detail">curent living</p>
      <p v-if="type==='hometown'" class="profile-detail">hometown</p>
    </div>
    <i v-if="type==='current'" class="fa fa-map-marker profile-icon"></i>
    <i v-if="type==='hometown'" class="fa fa-home profile-icon"></i>
    <div class="corner-buttons">
      <i v-on:click="clickEdit" class="fa fa-edit profile-edit-button"></i>
      <i v-on:click="clickDelete" class="fa fa-ban profile-delete-button"></i>
    </div>
  </div>
</div>

</template>

<script>
import profilePlaceBox from "./profile-place-box.js"

export default {
  name: "profile-place-box",
  components: {
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
    type: {
      type: String,
      default: "current",
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      mode: "display",
      pid: null,
      place: null,
      placeInput: null,
      errMsg: null,
    }
  },
  computed: profilePlaceBox.computed,
  watch: {
    initData: function() {
      if (this.initData) {
        this.place = this.initData.place;
        this.pid = this.initData.pid;
      }
    }
  },
  methods: profilePlaceBox.methods,
  mounted: profilePlaceBox.mounted,
}

</script>

<style lang="scss">
</style>