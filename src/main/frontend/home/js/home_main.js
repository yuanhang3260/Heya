import $ from "jquery";
import "bootstrap/dist/css/bootstrap.min.css";
import "heya/common/css/font-awesome.min.css";
import "heya/home/css/home.scss";

import Vue from "vue";
import VueBus from "vue-bus";

import NavBar from "heya/common/js/navbar.vue";
import Avatar from "heya/home/js/avatar.vue";
import Poster from "heya/home/js/poster.vue";
import PostBoard from "heya/home/js/post-board.vue";
import UserInfo from "heya/home/js/userinfo.vue";
import Chat from "heya/home/js/chat.vue";
import ImageViewer from "./image-viewer.vue"

Vue.config.productionTip = false
Vue.use(VueBus);

// propgate mouse-click event.
$(document).mouseup(function(e) {
  Vue.bus.emit("mouse-click", {
    event: e,
  });
});

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

new Vue({
  el: "#poster",
  components: {
    Poster,
  }
});

new Vue({
  el: "#post-board",
  components: {
    PostBoard,
  }
});

new Vue({
  el: "#chat",
  components: {
    Chat,
  }
});

new Vue({
  el: "#image-viewer",
  components: {
    ImageViewer,
  }
});
