import $ from "jquery";
import debug from "heya/common/js/debug.js";

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
  // TODO: load posts from backend.
  return [];
}

const imageViewerElementId = "post-image-viewer";

function viewPostImages(payload) {
  for (let post of this.posts) {
    if (post.id === payload.postId) {
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

export default {
  computed: {
  },

  methods: {
    getPosts: getPosts,
    loadPosts: loadPosts,
    viewPostImages: viewPostImages,
  },

  beforeMount: beforeMount,
  imageViewerElementId: imageViewerElementId,
}
