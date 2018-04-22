<template>

<div class="modal heya-image-viewer" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-body" tabindex="0" autofocus v-on:keydown.left.stop="prevImage" v-on:keydown.right.stop="nextImage">
        <div v-if="images.length > 1" class="image-viewer-sidebar image-viewer-sidebar-left" v-on:click.stop="prevImage">
          <i class="fa fa-angle-left image-viewer-angle" />
        </div>
        <div class="image-container">
          <img v-if="images.length > 0" :src="images[imageIndex].url" />
        </div>
        <div v-if="images.length > 1" class="image-viewer-sidebar image-viewer-sidebar-right" v-on:click.stop="nextImage">
          <i  class="fa fa-angle-right image-viewer-angle" />
        </div>
      </div>
    </div>
  </div>
</div>

</template>

<script>
import imageViewer from "./image-viewer.js"

export default {
  name: "image-viewer",
  components: {
  },
  props: {
    uid: {
      type: String,
      default: null,
    },
    username: {
      type: String,
      default: null,
    },
    debug: {
      type: Boolean,
      default: false,
    },
    images: {
      type: Array,
      default: function() { return []; },
    },
    imageIndex: {
      type: Number,
      default: 0,
    }
  },
  data () {
    return {
    }
  },
  computed: imageViewer.computed,
  methods: imageViewer.methods,
}
</script>

<style lang="scss">
@import "~heya/home/css/image-viewer.scss"
</style>
