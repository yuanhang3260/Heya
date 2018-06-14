<template>

<div class="chat-dialog-box">
  <div class="dialog-box-header" v-on:click="maximize" v-bind:class="{'has-unread-header': friend.hasUnread}">
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

  <div v-show="!dialog.minimized" class="dialog-box-body" v-on:click="clickHandler">
    <div class="messages-box">
      <div v-for="message in dialog.messages">
        <chatBubbleLeft v-if="message.to === username" :key="message.timestamp" :friend="friend" :content="message.content" :debug="debug"/>
        <chatBubbleRight v-if="message.from === username" :key="message.timestamp" :uid="uid" :username="username" :content="message.content" :debug="debug"/>
      </div>
    </div>

    <div class="input-group input-box">
      <div class="input-group-prepend">
        <button class="emoji-button">
          <i class="fa fa-smile-o emoji-icon" />
        </button>
      </div>
      <textarea v-model="messageInput" class="dialog-text-area" placeholder="type" v-on:keydown="typeHandler" v-on:keyup="typeHandler" v-on:keydown.enter.prevent="enterPressed" v-focus="dialog.focus"/>
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
    messages: {
      type: Array,
      default: function() { return []; },
    },
    debug: {
      type: Boolean,
      default: false,
    }
  },
  data () {
    return {
      messageInput: null,
      messagesBox: null,
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
  mounted: chatDialogBox.mounted,
  watch: {
    messages: function() {
      // Note we need to async execute scrollDown to wait for new message to
      // be rendered.
      let me = this;
      setTimeout(function() {
        // TODO: Check if current position is at the bottom of message box.
        me.scrollDown();
      }, 0);
    },
  }
}
</script>

<style lang="scss">
</style>
