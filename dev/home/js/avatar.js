import $ from "jquery";
import format from "string-format";
import ImageClipper from "heya/common/js/image-clipper.js";

function profileImageURL() {
  if (!this.debug) {
    return format("profileimage?uid={0}&type=profile", this.uid);
  } else {
    return "img/profile.jpg";
  }
}

function coverImageURL() {
  if (!this.debug) {
    return format("profileimage?uid={0}&type=cover", this.uid);
  } else {
    return "img/cover.jpg";
  }
}

var clipper = null;

function clickProfileImage() {
  if (!clipper) {
    clipper = new ImageClipper({
      title: "Update Profile Image",
      callback: this.updateProfileImage,
      outputFormat: "jpeg",
    });
  }

  clipper.open();
}

function updateProfileImage(filename, blob) {
  var formData = new FormData();
  formData.append("uid", this.uid);
  formData.append("action", "update");
  formData.append("type", "profile");
  formData.append("filename", filename);
  formData.append("imagedata", blob);

  $.ajax({
    type: "POST",
    url: "profileimage",
    data: formData,
    processData: false,
    contentType: false,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
  });
}

function friendlyNum(num) {
  if (num <= 100 * 1000) {
    return num;
  } else if (num <= 1000 * 1000) {
    return (num / 1000).toFixed(0) + "K";
  } else {
    return (num / 1000000).toFixed(0) + "M";
  }
}

function followersDisplay() {
  return friendlyNum(this.followers);
}

function followingDisplay() {
  return friendlyNum(this.following);
}

export default {
  computed: {
    profileImageURL: profileImageURL,
    coverImageURL: coverImageURL,
    followingDisplay,
    followersDisplay,
  },

  methods: {
    clickProfileImage: clickProfileImage,
    updateProfileImage: updateProfileImage,
  }
}
