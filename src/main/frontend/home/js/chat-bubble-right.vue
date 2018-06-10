<template>

<div class="chat-bubble-right">
  <div class="message-box">
    <p>{{content}}</p>
    <div class="small-triangle"></div>
  </div>
  <img class="bubble-avatar" v-bind:src="profileImageURL" />
</div>

</template>

<script>
import chatBubble from "./chat-bubble.js"

export default {
  name: "chat-dialog-box",
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
    content: {
      type: String,
      default: null,
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
    }
  },
  computed: chatBubble.computed,
  methods: chatBubble.methods,
}
</script>

<style lang="scss">
</style>
