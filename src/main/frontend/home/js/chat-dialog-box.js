
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

function enterPressed() {
  console.log("send-message");
  this.$emit("send-message", {
    username: this.friend.username,
    message: this.messageInput,
  });
  this.messageInput = null;
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
  },

}