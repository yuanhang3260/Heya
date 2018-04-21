<template>

<div class="image-clipboard">
  <label v-show="mode === 'add'" class="add-button">
    <i class="fa fa-plus"></i>
    <input v-on:change="addImage" type="file" />
  </label>

  <img v-show="mode === 'imageLoaded'" v-bind:src="imageSrc" class="image-preview" />
  <load-animation v-if="mode === 'loading'" :width="40" :height="40" :color="'lightgray'"></load-animation>
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
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      mode: "add",
      imageLoaded: false,
      imageSrc: null,
    }
  },
  computed: poster.computed,
  methods: poster.methods,
}
</script>

<style scoped lang="scss">
@import "~heya/home/css/image-clipboard.scss"
</style>
