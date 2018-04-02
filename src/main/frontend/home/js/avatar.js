import $ from "jquery";
import ImageClipper from "heya/common/js/image-clipper.js";

function profileImageURL() {
  if (!this.debug) {
    return "profileimage/profile/" + this.uid;
  } else {
    return "dist/img/profile.jpg";
  }
}

function coverImageURL() {
  if (!this.debug) {
    return "profileimage/cover/" + this.uid;
  } else {
    return "dist/img/cover.jpg";
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
  formData.append("filename", filename);
  formData.append("imagedata", blob);

  $.ajax({
    type: "POST",
    url: "profileimage/profile/" + this.uid,
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
