import "bootstrap/dist/css/bootstrap.min.css";
import "heya/common/css/font-awesome.min.css";
import "heya/profile/css/page.scss";

import Vue from "vue";

import NavBar from "heya/common/js/navbar.vue";
import Profile from "heya/profile/js/profile.vue";

Vue.config.productionTip = false

new Vue({
  el: "#navbar",
  components: {
    NavBar,
  }
});

new Vue({
  el: "#profile",
  components: {
    Profile,
  }
});
