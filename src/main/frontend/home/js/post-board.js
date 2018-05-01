import $ from "jquery";
import debug from "heya/common/js/debug.js";
import common from "./common.js";
import popups from "heya/common/js/popups.js";

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
      // Sort desc by time.
      me.posts = data.result.posts.sort(function(x, y) {
        return y.time - x.time;
      });
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

function deletePost(payload) {
  let me = this;
  let pid = payload.postId;
  let index = null;

  let _doDelete = function(resolve, reject) {
    var formData = {
      "_method": "DELETE",  // Restful API for spring mvc backend
    };

    $.ajax({
      type: "POST",
      url : "post/" + me.username + "/" + pid,
      data: formData,
      dataType : "json",
      encode : true,
    }).done(function(data) {
      console.log(data);
      if (data.success) {
        me.posts.splice(index, 1);
      }
      resolve();
    }).fail(function(data) {
      reject("Failed to delete post");
    });
  }

  for (let i = 0; i < this.posts.length; i++) {
    let post = this.posts[i];
    if (post.pid === pid) {
      index = i;
      popups.confirm({
        message: "Are you sure to delete this post?",
        task: _doDelete,
        syncWait: true,
      });
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
    deletePost: deletePost,
  },

  beforeMount: beforeMount,
  created: created,
  imageViewerElementId: imageViewerElementId,
}
