<template>

<nav class="fixed-top navbar heya-navbar">
  <div class="heya-navbar-container" v-bind:style="mainWidth">
    <a class="navbar-brand heya-navbar-brand" href="#" tips="hei">
      <b>Heya</b> <i class="fa fa-leaf heya-logo"></i>
    </a>
    <form v-if="searchBox" method="get" class="form-inline my-lg-0 heya-navbar-search" action="search">
      <div class="input-group">
        <input type="text" class="form-control heya-search-box" name="q" placeholder="Search">
        <div class="input-group-append">
          <button class="btn btn-outline-secondary heya-search-btn" type="submit">
            <i class="fa fa-search"></i>
          </button>
        </div>
      </div>
    </form>

    <div class="navbar-right-box" v-if="notification">
      <friendNotification :uid="uid" :username="username" :showMenu="rightMenu=='friends'" v-on:click-right-menu="showRightMenu" :debug="debug"/>
      <accountMangement :uid="uid" :username="username" :showMenu="rightMenu=='account'" v-on:click-right-menu="showRightMenu" :debug="debug"/>
    </div>
  </div>
</nav>

</template>

<script>
import $ from "jquery"
import accountMangement from "./account-management.vue"
import friendNotification from "./friend-notification.vue"

export default {
  components: {
    accountMangement,
    friendNotification,
  },
  name: "navbar",
  props: {
    searchBox: {
      type: Boolean,
      default: true,
    },
    notification: {
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
    width: {
      type: Number,
      default: 960,
    },
    debug: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      rightMenu: null,
      rightBox: null,
    }
  },
  computed: {
    mainWidth: function() {
      return {
        width: this.width + "px",
      }
    }
  },
  methods: {
    showRightMenu: function(payload) {
      if (this.rightMenu === payload.rightMenu) {
        this.rightMenu = null;
      } else {
        this.rightMenu = payload.rightMenu;
      }
    },
  },

  mounted: function() {
    this.rightBox = $(this.$el).find(".navbar-right-box");
    let me = this;
    this.$bus.on("mouse-click", function(payload) {
      if (!$.contains(me.rightBox.get(0), payload.event.target)) {
        me.rightMenu = null;
      }
    });
  }
};
</script>

<style lang="scss">
@import "~heya/common/css/navbar.scss";
</style>