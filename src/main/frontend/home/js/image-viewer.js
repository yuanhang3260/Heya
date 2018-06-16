import $ from "jquery";

function nextImage() {
  if (this.imageIndex + 1 >= this.images.length) {
    this.imageIndex = 0;
    // this.$emit("update:imageIndex", 0);
  } else {
    this.imageIndex++;
    // this.$emit("update:imageIndex", this.imageIndex + 1);
  }
}

function prevImage() {
  if (this.imageIndex === 0) {
    this.imageIndex = this.images.length - 1;
    // this.$emit("update:imageIndex", this.images.length - 1);
  } else {
    this.imageIndex--;
    // this.$emit("update:imageIndex", this.imageIndex - 1);
  }
}

function created() {
  let me = this;
  this.$bus.on("view-images", function(payload) {
    me.images = payload.images;
    me.imageIndex = payload.imageIndex;

    $(me.$el).modal("show");
    $(me.$el).find(".modal-body").focus();
  });
}

export default {
  computed: {
  },

  methods: {
    prevImage: prevImage,
    nextImage: nextImage,
  },

  created: created,
}
