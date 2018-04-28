import utils from "heya/common/js/utils.js"

function imageRows() {
  return [...Array(Math.ceil(this.post.images.length / 3)).keys()];
}

function postCreateDate() {
  return utils.formatDate(new Date(this.post.time));
}

function viewImage(index) {
  this.$emit("view-post-images", {
    postId: this.post.pid,
    imageIndex: index,
  });
}

export default {
  computed: {
    imageRows: imageRows,
    postCreateDate: postCreateDate,
  },

  methods: {
    viewImage: viewImage,
  }
}
