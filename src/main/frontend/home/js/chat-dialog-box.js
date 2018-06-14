import $ from "jquery";

function typeHandler(event) {
  this.adjustTextAreaHeight(event);
}

function adjustTextAreaHeight(event) {
  let target = event.target;
  target.style.height = "1px";
  if (target.scrollHeight >= 120) {
    target.style.height = "120px";
    target.style["overflow-y"] = "scroll";
  } else {
    target.style.height = (target.scrollHeight) + "px";
    target.style["overflow-y"] = "hidden";
  }
}

function created() {
}

function mounted() {
  this.messagesBox = $(this.$el).find(".messages-box");
  this.scrollDown();
}

function scrollTextArea() {
  console.log("scroll");
}

function closeDialog() {
  this.$emit("close-dialog", {
    username: this.friend.username,
  });
}

function flipMinimize() {
  this.$emit("flip-minimize", {
    username: this.friend.username,
  });
}

function maximize() {
  if (this.dialog.minimized) {
    this.$emit("flip-minimize", {
      username: this.friend.username,
    });
  }
}

function scrollHandler() {
  console.log("scroll");
}

function scrollDown() {
  // Scroll down to bottom of message box. We pass this function to upper
  // level because it needs to wait for new message to be added into chat
  // history and rendered in message box.
  if (this.messagesBox) {
    this.messagesBox.scrollTop(this.messagesBox.prop("scrollHeight"));
  }
}

function enterPressed(event) {
  this.$emit("dialog-box-clicked", {
    username: this.friend.username,
  });

  if (this.messageInput) {
    this.$emit("send-message", {
      username: this.friend.username,
      content: this.messageInput,
    });
    this.messageInput = null;
  }
}

function clickHandler() {
  if (this.friend.hasUnread) {
    this.$emit("dialog-box-clicked", {
      username: this.friend.username,
    });
  }
}

export default {
  computed: {
  },

  methods: {
    adjustTextAreaHeight: adjustTextAreaHeight,
    scrollTextArea: scrollTextArea,
    closeDialog: closeDialog,
    flipMinimize: flipMinimize,
    maximize: maximize,
    enterPressed: enterPressed,
    scrollHandler: scrollHandler,
    scrollDown: scrollDown,
    clickHandler: clickHandler,
    typeHandler: typeHandler,
  },

  created: created,
  mounted: mounted,
}
