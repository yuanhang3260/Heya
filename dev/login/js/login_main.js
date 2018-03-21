import "bootstrap/dist/css/bootstrap.min.css";
import "heya/common/css/font-awesome.5.0.6.min.js";
import "heya/login/css/index.scss";

import Vue from "vue";

import LoginBox from "heya/login/js/login.vue";
import NavBar from "heya/common/js/navbar.vue";
import Foot from "heya/common/js/foot.vue";

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
