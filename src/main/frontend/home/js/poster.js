import $ from "jquery";
import b64ToBlob from "b64-to-blob";
import common from "./common.js";
import popups from "heya/common/js/popups.js";
import uuid from "uuid/v1";

function enablePostButtom() {
  return this.postTextInput || (this.images.length > 0 && this.allImagesLoaded);
}

function clickEditButton(item) {
  if (item === this.editorSelected) {
    this.editorSelected = null;
    return;
  } else {
    this.editorSelected = item;
  }
}

function loadImages(payload) {
  let files = payload.files;
  if (files.length + this.images.length > this.maxImages) {
    popups.alert("No more than 9 pictures allowed.");
    return;
  }

  let me = this;

  this.allImagesLoaded = false;
  for (let file of files) {
    this.images.push({
      id: uuid(),
      name: file.name,
      url: null,
    });
    let index = this.images.length - 1;

    let reader = new FileReader();
    reader.onload = function() {
      setTimeout(function() {
        me.images[index].url = reader.result;
        me.checkAllImagesLoaded();
      }, 200);
    };
    reader.readAsDataURL(file);
  }
}

function checkAllImagesLoaded() {
  let allLoaded = true;
  for (let image of this.images) {
    if (!image.url) {
      allLoaded = false;
    }
    break;
  }
  this.allImagesLoaded = allLoaded;
}

function deleteImage(payload) {
  for (let i = 0; i < this.images.length; i++) {
    let image = this.images[i];
    if (image.id === payload.id) {
      this.images.splice(i, 1);
      break;
    }
  }
}

function doPost() {
  var formData = new FormData();
  if (this.postTextInput) {
    formData.append("content", this.postTextInput);
  }

  for (let image of this.images) {
    var ext = (image.url.split(";")[0]).split(":")[1]
    var blob = b64ToBlob(image.url.split(",")[1], ext);
    formData.append("image-" + image.id, blob);
  }

  this.submitting = true;

  let me = this;
  $.ajax({
    type: "POST",
    url: "post/" + this.username,
    data: formData,
    processData: false,
    contentType: false,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      let post = data.result.post;
      common.generatePost(post, me.username);
      me.$bus.emit("add-new-post", {
        post: post,
      });

      // TODO: remove the delay.
      window.setTimeout(function() {
        me.postTextInput = null;
        me.images = [];
        me.editorSelected = null;
        me.submitting = false;
      }, 300);
    }
  }).fail(function() {
    popups.alert("Failed to add post");
    me.submitting = false;
  });
}

export default {
  computed: {
    enablePostButtom: enablePostButtom,
  },

  methods: {
    clickEditButton: clickEditButton,
    loadImages: loadImages,
    checkAllImagesLoaded: checkAllImagesLoaded,
    deleteImage: deleteImage,
    doPost: doPost,
  }
}
