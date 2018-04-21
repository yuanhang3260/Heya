import popups from "heya/common/js/popups.js"
import utils from "heya/common/js/utils.js"

const maxImageSize = 5 * 1024 * 1024;

function addImages(event) {
  let input = event.target;
  if (!input.files) {
    return;
  }

  for (let file of input.files) {
    if (file.size > maxImageSize) {
      popups.alert(
        "Some image size over limit " + utils.friendlyFileSize(maxImageSize));
      input.value = null;
      return;
    }
  }

  this.$emit("new-images-added", {
    files: input.files,
  });

  input.value = null;
  return;
}

function deleteImage() {
  this.$emit("delete-image", {
    id: this.id,
  });
}

function imageToAdd() {
  return !this.name && !this.url;
}

function imageLoaded() {
  return this.url;
}

function imageLoading() {
  return this.name && !this.url;
}

export default {
  computed: {
    imageToAdd: imageToAdd,
    imageLoading: imageLoading,
    imageLoaded: imageLoaded,
  },

  methods: {
    addImages: addImages,
    deleteImage: deleteImage,
  }
}
