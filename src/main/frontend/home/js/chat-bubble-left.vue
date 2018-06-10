<template>

<div class="chat-bubble-left">
  <img class="bubble-avatar" v-bind:src="friend.avatar" />
  <div class="message-box">
    <p>{{content}}</p>
    <div class="small-triangle"></div>
  </div>
</div>

</template>

<script>
import chatBubble from "./chat-bubble.js"

export default {
  name: "chat-bubble-left",
  components: {
  },
  props: {
    friend: {
      type: Object,
      default: function() { return null; },
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
