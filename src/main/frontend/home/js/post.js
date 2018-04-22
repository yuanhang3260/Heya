import utils from "heya/common/js/utils.js"

function imageRows() {
  return [...Array(Math.ceil(this.post.images.length / 3)).keys()];
}

function postCreateDate() {
  return utils.formatDate(this.post.time);
}

function viewImage(index) {
  this.$emit("view-post-images", {
    postId: this.post.id,
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
