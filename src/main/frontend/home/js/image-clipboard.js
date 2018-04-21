import popups from "heya/common/js/popups.js"
import utils from "heya/common/js/utils.js"

const maxImageSize = 5 * 1024 * 1024;

function addImage(event) {
  let input = event.target;
  if (!input.files) {
    return;
  }

  let file = input.files[0];
  console.log(file.name);

  if (file.size > maxImageSize) {
    popups.alert(
      "Image size over limit " + utils.friendlyFileSize(maxImageSize));
    input.value = null;
    return;
  }

  let me = this;

  let reader = new FileReader();
  reader.onload = function() {
    me.imageSrc = reader.result;
    setTimeout(function() {
      me.mode = "imageLoaded";
    }, 200);
  };

  this.mode = "loading";
  reader.readAsDataURL(file);
}

export default {
  computed: {

  },

  methods: {
    addImage: addImage,
  }
}
