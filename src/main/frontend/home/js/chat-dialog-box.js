import $ from "jquery";

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

function enterPressed(event) {
  if (this.messageInput) {
    let scrollDown = function() {
      // Scroll down to bottom of message box. We pass this function to upper
      // level because it needs to wait for new message to be added into chat
      // history and rendered in message box.
      let target = event.target;
      let $messageBox =
        $(target.parentElement.parentElement).find(".messages-box");
      $messageBox.scrollTop($messageBox.prop("scrollHeight"));
    }

    this.$emit("send-message", {
      username: this.friend.username,
      content: this.messageInput,
      scrollDown: scrollDown,
    });
    this.messageInput = null;
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
  },

}
