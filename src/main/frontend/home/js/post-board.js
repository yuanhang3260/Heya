import $ from "jquery";
import debug from "heya/common/js/debug.js";
import common from "./common.js";

function beforeMount() {
  this.getPosts();
}

function getPosts() {
  if (this.debug) {
    this.posts = debug.posts();
  } else {
    this.loadPosts();
  }
}

function loadPosts() {
  var me = this;
  $.ajax({
    type: "GET",
    url: "post/" + this.username + "/all",
    dataType: "json",
    encode: true,
  }).done(function(data) {
    console.log(data);
    if (data.success) {
      for (let post of data.result.posts) {
        common.generatePost(post, me.uid, me.username);
      }
      me.posts = data.result.posts;
    }
  });
}

const imageViewerElementId = "post-image-viewer";

function viewPostImages(payload) {
  for (let post of this.posts) {
    if (post.pid === payload.postId) {
      this.imagesToView = post.images;
      this.imageViewIndex = payload.imageIndex;

      // Unforturnately, we have to use jquery to modal the image viewer,
      // and give focus to dialog body.
      $("#" + imageViewerElementId).modal("show");
      $("#" + imageViewerElementId).find(".modal-body").focus();
      break;
    }
  }
}

function created() {
  let me = this;
  this.$bus.on("add-new-post", function(payload) {
    me.posts.unshift(payload.post);
  });
}

export default {
  computed: {
  },

  methods: {
    getPosts: getPosts,
    loadPosts: loadPosts,
    viewPostImages: viewPostImages,
  },

  beforeMount: beforeMount,
  created: created,
  imageViewerElementId: imageViewerElementId,
}
