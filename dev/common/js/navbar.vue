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
  </div>
</nav>

</template>

<script>
export default {
  name: "navbar",
  props: {
    "searchBox": {
      type: Boolean,
      default: true,
    },
    "width": {
      type: Number,
      default: 960,
    }
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

<style scoped lang="scss">
@import "~heya/common/css/navbar.scss";
</style>