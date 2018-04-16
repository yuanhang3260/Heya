<template>

<div class="subpanel profile-places-panel">
  <profile-place-box :uid="uid" :username="username" :type="'current'" :initData="current" :debug="debug"></profile-place-box>
  <profile-place-box :uid="uid" :username="username" :type="'hometown'" :initData="hometown" :debug="debug"></profile-place-box>
</div>

</template>

<script>
import profilePlaces from "./profile-places.js"
import profilePlaceBox from "./profile-place-box.vue"

export default {
  name: "profile-places",
  components: {
    profilePlaceBox,
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
    placesInfo: {
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
      current: null,
      hometown: null,
    }
  },
  watch: {
    placesInfo: function() {
      for (let place of this.placesInfo) {
        if (place.current) {
          this.current = place;
        }
        if (place.hometown) {
          this.hometown = place;
        }
      }
    }
  },

  methods: profilePlaces.methods,
  mounted: profilePlaces.mounted,
}

</script>

<style lang="scss">
@import "~heya/profile/css/profile-places.scss"
</style>