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
    <div class="messages-box"></div>

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

export default {
  name: "chat-dialog-box",
  components: {
  },
  props: {
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
