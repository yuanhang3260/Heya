<template>

<div class="image-clipboard">
  <label v-if="imageToAdd" class="add-button">
    <i class="fa fa-plus"></i>
    <input v-on:change="addImages" multiple="multiple" type="file" />
  </label>

  <div v-if="imageLoaded" class="preivew-image-container">
    <img v-bind:src="url" class="image-preview">
    <i v-on:click="deleteImage" class="fa fa-minus-circle delete-button"></i>
  </div>
  <load-animation v-if="imageLoading" :width="40" :height="40" :color="'lightgray'"></load-animation>
</div>

</template>

<script>
import poster from "./image-clipboard.js"
import loadAnimation from "heya/common/js/load-animation.vue"

export default {
  name: "image-clipboard",
  components: {
    loadAnimation,
  },
  props: {
    id: {
      type: String,
      default: null,
    },
    name: {
      type: String,
      default: null,
    },
    url: {
      type: String,
      default: null,
    },
  },
  data () {
    return {
    }
  },
  computed: poster.computed,
  methods: poster.methods,
}
</script>

<style scoped lang="scss">
@import "~heya/home/css/image-clipboard.scss"
</style>
