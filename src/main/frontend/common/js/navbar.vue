<template>

<nav class="fixed-top navbar heya-navbar">
  <div class="heya-navbar-container" v-bind:style="mainWidth">
    <a class="navbar-brand heya-navbar-brand" href="#" tips="hei">
      <b>Heya</b> <i class="fa fa-leaf heya-logo"></i>
    </a>
    <form v-if="searchBox" method="get" class="form-inline my-lg-0 heya-navbar-search" action="search">
      <div class="input-group">
        <input type="text" class="form-control heya-search-box" name="value" placeholder="Search">
        <div class="input-group-append">
          <button class="btn btn-outline-secondary heya-search-btn" type="submit">
            <i class="fa fa-search"></i>
          </button>
        </div>
      </div>
    </form>

    <div class="navbar-right-box" v-if="notification">
      <accountMangement :uid="uid" :username="username" :debug="debug"/>
    </div>
  </div>
</nav>

</template>

<script>
import accountMangement from "./account-management.vue" 

export default {
  components: {
    accountMangement,
  },
  name: "navbar",
  props: {
    "searchBox": {
      type: Boolean,
      default: true,
    },
    "notification": {
      type: Boolean,
      default: true,
    },
    uid: {
      type: String,
      default: null,
    },
    username: {
      type: String,
      default: null,
    },
    "width": {
      type: Number,
      default: 960,
    },
    "debug": {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {}
  },
  computed: {
    mainWidth: function() {
      return {
        "width": this.width + "px",
      }
    }
  },
  methods: {},
};
</script>

<style lang="scss">
@import "~heya/common/css/navbar.scss";
</style>