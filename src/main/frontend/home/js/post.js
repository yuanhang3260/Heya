import utils from "heya/common/js/utils.js"

function imageRows() {
  return [...Array(Math.ceil(this.post.images.length / 3)).keys()];
}

function postCreateDate() {
  return utils.formatDate(this.post.time);
}

function viewImage(index) {
  this.$emit("view-post-images", {
    postId: this.post.pid,
    imageIndex: index,
  });
}

function deletePost() {
  this.$emit("delete-post", {
    postId: this.post.pid,
  });
}

export default {
  computed: {
    imageRows: imageRows,
    postCreateDate: postCreateDate,
  },

  methods: {
    viewImage: viewImage,
    deletePost: deletePost,
  }
}
