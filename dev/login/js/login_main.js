import "bootstrap/dist/css/bootstrap.min.css";
import "common/css/font-awesome.5.0.6.min.js";
import "login/css/index.scss";

import Vue from "vue";

import Login from "./login.js";
import LoginBox from "./login.vue";
import NavBar from "common/js/navbar.vue";
import Foot from "common/js/foot.vue";

Vue.config.productionTip = false

new Vue({
  el: "#navbar",
  components: {
    NavBar,
  }
});

new Vue({
  el: "#foot",
  components: {
    Foot,  // Do not use footer. It's a reserved html tag.
  }
});

new Vue({
  el: "#login-box",
  components: {
    LoginBox,
  }
});
