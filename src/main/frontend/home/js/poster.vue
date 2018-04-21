<template>

<div class="card poster-container">
  <textarea v-model="postTextInput" class="poster-text-area" placeholder="Share your minds"></textarea>

  <div v-show="editorSelected" class="editors-container">
    <div v-show="editorSelected === 'image'" class="editor-panel image-editor-panel">
      <image-clipboard></image-clipboard>
      <image-clipboard></image-clipboard>
    </div>

    <div v-show="editorSelected === 'video'" class="editor-panel image-editor-panel">
      <image-clipboard></image-clipboard>
    </div>
  </div>

  <div class="bottom-container">
    <button v-on:click="clickEditButton('image')" type="button" class="btn edit-button" v-bind:class="{'edit-button-selected': editorSelected === 'image'}">
      <i class="fa fa-image"></i>
      <span>Image</span>
    </button>
    <button v-on:click="clickEditButton('video')" type="button" class="btn edit-button" v-bind:class="{'edit-button-selected': editorSelected === 'video'}">
      <i class="fa fa-film"></i>
      <span>Video</span>
    </button>
    <button type="button" class="btn btn-success post-button" v-bind:disabled="postButtomDisabled">Go !</button>
  </div>
</div>

</template>

<script>
import poster from "./poster.js"
import imageClipboard from "./image-clipboard.vue"

export default {
  name: "poster",
  components: {
    imageClipboard,
  },
  props: {
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      postTextInput: null,
      editorSelected: null,
    }
  },
  computed: poster.computed,
  methods: poster.methods,
}
</script>

<style lang="scss">
@import "~heya/home/css/poster.scss"
</style>
