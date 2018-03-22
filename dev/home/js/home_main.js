import "bootstrap/dist/css/bootstrap.min.css";
import "heya/common/css/font-awesome.min.css";
import "heya/home/css/home.scss";

import Vue from "vue";

import NavBar from "heya/common/js/navbar.vue";
import Avatar from "heya/home/js/avatar.vue";
import UserInfo from "heya/home/js/userinfo.vue";

Vue.config.productionTip = false

new Vue({
  el: "#navbar",
  components: {
    NavBar,
  }
});

new Vue({
  el: "#avatar",
  components: {
    Avatar,
  }
});

new Vue({
  el: "#userinfo",
  components: {
    UserInfo,
  }
});

// var aa = new Vue(UserInfo);
// aa.debug = true;

// window.setTimeout(() => {
//   aa.hometown = null;
// }, 1000);

// aa.$mount("#userinfo");

// var profileImage = new home.ProfileImage("#profile-image");
