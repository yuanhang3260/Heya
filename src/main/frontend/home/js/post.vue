<template>

<div class="card heya-post">
  <div class="post-header">
    <img class="post-owner-avatar" v-bind:src="post.avatar">
    <div class="header-title">
      <p class="post-owner">{{post.username}}</p>
      <p class="post-time">{{postCreateDate}}</p>
    </div>
    <i class="fa fa-ban delete-post-button" />
  </div>

  <div class="post-content">
    <p>{{post.content}}</p>
  </div>

  <div class="post-gallery">
    <div v-for="index in imageRows" class="post-gallery-row">
      <div v-for="image in post.images.slice(index * 3, index * 3 + 3)" class="post-image-container">
        <img v-bind:src="image.url">
      </div>
    </div>
  </div>
</div>

</template>

<script>
import postBoard from "./post.js"

export default {
  name: "post-board",
  props: {
    debug: {
      type: Boolean,
      default: false,
    },
    post: {
      type: Object,  // Date object
      default: function() { return null; },
    }
  },
  data () {
    return {
    }
  },
  computed: postBoard.computed,
  methods: postBoard.methods,
}
</script>

<style lang="scss">
@import "~heya/home/css/post.scss"
</style>
