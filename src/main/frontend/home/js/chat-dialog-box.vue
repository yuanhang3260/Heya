<template>

<div class="chat-dialog-box">
  <div class="dialog-box-header" v-on:click="maximize">
    <i class="fa fa-comments chat-icon"/>
    <span class="title">{{friend.username}}</span>
    <div class="button-box">
      <span v-on:click.stop="flipMinimize">
        <i class="fa fa-window-minimize minimize-button" />
      </span>
      <span v-on:click.stop="closeDialog">
        <i class="fa fa-times close-button" />
      </span>
    </div>
  </div>

  <div v-show="!dialog.minimized" class="dialog-box-body">
    <div class="messages-box">
      <div v-for="message in dialog.messages">
        <chatBubbleLeft v-if="!message.me" :key="message.timestamp.getTime()" :friend="friend" :content="message.content" :debug="debug"/>
        <chatBubbleRight v-if="message.me" :key="message.timestamp.getTime()" :uid="uid" :username="username" :content="message.content" :debug="debug"/>
      </div>
    </div>

    <div class="input-group input-box">
      <div class="input-group-prepend">
        <button class="emoji-button">
          <i class="fa fa-smile-o emoji-icon" />
        </button>
      </div>
      <textarea v-model="messageInput" class="dialog-text-area" placeholder="type" v-on:keydown="adjustTextAreaHeight" v-on:keyup="adjustTextAreaHeight" v-on:keydown.enter.prevent="enterPressed" v-focus="dialog.focus"/>
    </div>
  </div>
</div>

</template>

<script>
import chatDialogBox from "./chat-dialog-box.js"
import chatBubbleLeft from "./chat-bubble-left.vue"
import chatBubbleRight from "./chat-bubble-right.vue"

export default {
  name: "chat-dialog-box",
  components: {
    chatBubbleLeft,
    chatBubbleRight,
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
    dialog: {
      type: Object,
      default: function() { return null; },
    },
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
      messageInput: null,
    }
  },
  computed: chatDialogBox.computed,
  methods: chatDialogBox.methods,
  directives: {
    focus: {
      inserted: function (el, {value}) {
        if (value) {
          el.focus();
        }
      },
      update: function (el, {value}) {
        if (value) {
          el.focus();
        }
      }
    }
  },
}
</script>

<style lang="scss">
</style>
