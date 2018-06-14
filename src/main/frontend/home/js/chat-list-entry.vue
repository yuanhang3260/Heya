<template>

<div class="chat-list-entry" v-on:click="openDialog">
  <img class="friend-profile-img" v-bind:src="friend.avatar" />
  <span v-show="friend.hasUnread" class="has-unread-notifier" />
  <div class="friend-name">
    <span>{{friend.username}}</span>
  </div>
  <div class="status">
    <i class="fa fa-circle" v-bind:class="{'online-dot': friend.online}" />
  </div>
</div>

</template>

<script>
import chatListEntry from "./chat-list-entry.js"

export default {
  name: "chat-list-entry",
  components: {
  },
  props: {
    friend: {
      type: Object,
      default: function() { return null; },
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
  computed: chatListEntry.computed,
  methods: chatListEntry.methods,
}
</script>

<style lang="scss">
</style>
