<template>

<div class="card chat-top-container">

  <div class="card chat-panel-container">
    <div class="input-group search-box">
      <div class="input-group-prepend">
        <button class="btn btn-outline-secondary" type="button">
          <i class="fa fa-search" />
        </button>
      </div>
      <input type="text" class="form-control" placeholder="Search friend">
    </div>

    <chatListEntry v-for="friend in friends" :key="friend.username" :friend="friend" />
  </div>

  <div chat-dialogs-container>

  </div>

</div>

</template>

<script>
import chatListEntry from "./chat-list-entry.vue";
import chat from "./chat.js";

export default {
  name: "chat",
  components: {
    chatListEntry,
  },
  props: {
    uid: {
      type: String,
      default: null,
    },
    username: {
      type: String,
      default: "default",
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      friends: [],
      dialogs: [],
    }
  },
  computed: chat.computed,
  methods: chat.methods,
  beforeMount: chat.beforeMount,
}
</script>

<style lang="scss">
@import "~heya/home/css/chat.scss"
</style>
