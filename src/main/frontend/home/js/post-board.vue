<template>

<div class="heya-post-board">
  <post v-for="post in posts" :key="post.pid" :post="post" v-on:view-post-images="viewPostImages"/>

  <image-viewer :id="imageViewerElementId" :uid="uid" :username="username" :debug="debug" :images="imagesToView" :imageIndex.sync="imageViewIndex"></image-viewer>
</div>

</template>

<script>
import post from "./post.vue"
import imageViewer from "./image-viewer.vue"
import postBoard from "./post-board.js"

export default {
  name: "post-board",
  components: {
    post,
    imageViewer,
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
  },
  data () {
    return {
      posts: [],
      imageViewerElementId: postBoard.imageViewerElementId,
      imagesToView: [],
      imageViewIndex: 0,
    }
  },
  computed: postBoard.computed,
  methods: postBoard.methods,
  beforeMount: postBoard.beforeMount,
  created: postBoard.created,
}
</script>

<style lang="scss">
@import "~heya/home/css/post-board.scss"
</style>
