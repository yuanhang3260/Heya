<template>

<div class="card poster-container">
  <textarea v-model="postTextInput" class="poster-text-area" placeholder="Share your minds"></textarea>

  <div class="bottom-container">
    <button type="button" class="btn edit-button">
      <i class="fa fa-image"></i>
      <span>Image</span>
    </button>
    <button type="button" class="btn edit-button">
      <i class="fa fa-film"></i>
      <span>Video</span>
    </button>
    <button type="button" class="btn btn-success post-button" v-bind:disabled="postButtomDisabled">Go !</button>
  </div>
</div>

</template>

<script>
import poster from "./poster.js"

export default {
  name: "poster",
  props: {
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      postTextInput: null,
    }
  },
  computed: poster.computed,
  methods: poster.methods,
}
</script>

<style scoped lang="scss">
@import "~heya/home/css/poster.scss"
</style>
