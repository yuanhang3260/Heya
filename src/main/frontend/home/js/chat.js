import $ from "jquery";

import debug from "heya/common/js/debug.js";

function loadFriends() {
  if (this.debug) {
    this.friends = debug.friends();
    this.dialogs = debug.dialogs();
  } else {
    // TODO: load friends list from backend.
  }
}

function beforeMount() {
  this.loadFriends();
}

function openDialog(payload) {
  // First, blur all dialog boxes.
  for (let dialog of this.dialogs) {
    dialog.focus = false;
  }

  // If the dialog with this friend is already open, maximize it if it's
  // minimized, and set focus onto it.
  //
  // By default there are only 5 dialog boxes visible in the window. We keep
  // this FIFO array by only displaying the first 5 elements in the dialog list.
  // If we want to open a dialog box with index >=5, we move it to the header
  // of the dialogs list.
  for (let i = 0; i < this.dialogs.length; i++) {
    let dialog = this.dialogs[i];
    if (dialog.friend.username === payload.username) {
      dialog.minimized = false;
      dialog.focus = true;
      if (i >= 5) {
        let newList = [dialog];
        for (let j = 0; j < this.dialogs.length && j != i; j++) {
          newList.push(this.dialogs[j]);
        }
        this.dialogs = newList;
      }
      return;
    }
  }

  // If dialog does not exist, open new dialog with this friend.
  for (let friend of this.friends) {
    if (friend.username === payload.username) {
      this.dialogs = [{
        friend: friend,
        minimized: false,
        focus: true,
      }].concat(this.dialogs);
    }
  }
}

function closeDialog(payload) {
  for (let i = 0; i < this.dialogs.length; i++) {
    let dialog = this.dialogs[i];
    if (dialog.friend.username === payload.username) {
      this.dialogs.splice(i, 1);
      return;
    }
  }
}

function flipMinimize(payload) {
  for (let i = 0; i < this.dialogs.length; i++) {
    let dialog = this.dialogs[i];
    if (dialog.friend.username === payload.username) {
      dialog.minimized = !dialog.minimized;
      return;
    }
  }
}

export default {
  computed: {
  },

  methods: {
    loadFriends: loadFriends,
    openDialog: openDialog,
    closeDialog: closeDialog,
    flipMinimize: flipMinimize,
  },

  beforeMount: beforeMount,
}
