import "bootstrap/dist/css/bootstrap.min.css";
import "heya/common/css/font-awesome.min.css";
import "heya/signup/css/index.scss";

import Vue from "vue";

import NavBar from "heya/common/js/navbar.vue";
import SignupBox from "heya/signup/js/signup.vue";

Vue.config.productionTip = false

new Vue({
  el: "#navbar",
  components: {
    NavBar,
  }
});

new Vue({
  el: "#signup-box",
  components: {
    SignupBox,
  }
});
