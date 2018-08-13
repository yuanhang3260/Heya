import $ from "jquery"
import common from "heya/home/js/common.js"

function logout() {
  if (this.debug) {
    window.location.assign("/Heya");
    return;
  }

  var me = this;
  $.ajax({
    type: "POST",
    url: "logout",
    dataType: "json",
    encode: true,
  }).done(function(data) {
    if (data.success) {
      window.location.assign("/Heya");
    }
  });
}

function clickMenu() {
  this.$emit("click-right-menu", {
    rightMenu: "account",
  });
}

export default {
  methods:{
    profileImageURL: common.profileImageURL,
    logout: logout,
    clickMenu: clickMenu,
  }

}
