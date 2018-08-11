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
    console.log(data);
    if (data.success) {
      window.location.assign("/Heya");
    }
  });
}

function flipMenu() {
  this.showMenu = !this.showMenu
}

export default {
  methods:{
    profileImageURL: common.profileImageURL,
    logout: logout,
    flipMenu: flipMenu,
  }

}
