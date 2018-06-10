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

    <chatListEntry v-for="friend in friendsList" :key="friend.username" :friend="friend" v-on:open-dialog="openDialog" :debug="debug" />
  </div>

  <div class="chat-dialogs-container">
    <chatDialogBox v-for="dialog in dialogs" :key="dialog.friend.username" :dialog="dialog" :friend="dialog.friend" :messages="dialog.messages" :username="username" :uid="uid" v-on:close-dialog="closeDialog" v-on:flip-minimize="flipMinimize" v-on:send-message="sendMessage" v-on:dialog-box-clicked="dialogBoxClickedHandler" :debug="debug" />
  </div>

</div>

</template>

<script>
import chatListEntry from "./chat-list-entry.vue";
import chatDialogBox from "./chat-dialog-box.vue";
import chat from "./chat.js";

export default {
  name: "chat",
  components: {
    chatListEntry,
    chatDialogBox,
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
    }
  },
  data () {
    return {
      friends: {},
      friendsList: [],
      dialogs: [],
      inactiveDialogs: {},
      webSocket: null,
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
