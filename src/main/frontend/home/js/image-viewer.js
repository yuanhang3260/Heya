
function nextImage() {
  if (this.imageIndex + 1 >= this.images.length) {
    // this.imageIndex = 0;
    this.$emit("update:imageIndex", 0);
  } else {
    // this.imageIndex++;
    this.$emit("update:imageIndex", this.imageIndex + 1);
  }
}

function prevImage() {
  if (this.imageIndex === 0) {
    this.$emit("update:imageIndex", this.images.length - 1);
    // this.imageIndex = this.images.length - 1;
  } else {
    this.$emit("update:imageIndex", this.imageIndex - 1);
    // this.imageIndex--;
  }
}

export default {
  computed: {
  },

  methods: {
    prevImage: prevImage,
    nextImage: nextImage,
  }
}
