<template>

<div class="heya-post-board">
  <post v-for="post in posts" :key="post.pid" :post="post" :editable="editable" v-on:delete-post="deletePost"/>
</div>

</template>

<script>
import post from "./post.vue"
import postBoard from "./post-board.js"

export default {
  name: "post-board",
  components: {
    post,
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
    editable: {
      type: Boolean,
      default: false,
    },
    debug: {
      type: Boolean,
      default: false,
    },
  },
  data () {
    return {
      posts: [],
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
