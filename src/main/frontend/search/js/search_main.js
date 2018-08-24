import $ from "jquery";
import "bootstrap/dist/css/bootstrap.min.css";
import "heya/common/css/font-awesome.min.css";
import "heya/search/css/search.scss";

import Vue from "vue";
import VueBus from "vue-bus";

import NavBar from "heya/common/js/navbar.vue";

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
