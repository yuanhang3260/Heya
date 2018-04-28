<template>

<div class="card heya-post">
  <div class="post-header">
    <img class="post-owner-avatar" v-bind:src="post.avatar">
    <div class="header-title">
      <p class="post-owner">{{post.username}}</p>
      <p class="post-time">{{postCreateDate}}</p>
    </div>
    <i v-on:click="deletePost" class="fa fa-ban delete-post-button" />
  </div>

  <div class="post-content">
    <p>{{post.content}}</p>
  </div>

  <div v-if="post.images && post.images.length > 0" class="post-gallery">
    <div v-for="row in imageRows" class="post-gallery-row" :class="{singleImageRow: post.images.length === 1}">
      <div v-for="(image, column) in post.images.slice(row * 3, Math.min(row * 3 + 3, post.images.length))" class="post-image-container">
        <img v-bind:src="image.url" v-on:click="viewImage(row * 3 + column)">
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
