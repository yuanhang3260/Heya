import $ from "jquery";

import debug from "heya/common/js/debug.js";

function loadFriends() {
  if (this.debug) {
    this.dialogs = debug.dialogs();
    this.friendsList = debug.friends();
    for (let friend of this.friendsList) {
      this.friends[friend.username] = friend;
    }
  } else {
    this.getFriendsFromServer();
  }
}

function getFriendsFromServer() {
  var me = this;
  $.ajax({
      type: "GET",
      url: "friends/" + me.uid,
      dataType: "json",
      encode: true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      me.friendsList = data.result.friends;
      for (let friend of me.friendsList) {
        me.friends[friend.username] = friend;
      }
    }
  });
}

function connect() {
  let serverAddress = "ws://localhost:8080/Heya/chat/" + this.username;
  if (this.debug) {
    serverAddress = "ws://localhost:8081/chat";
  }

  let me = this;
  let ws = this.webSocket = new WebSocket(serverAddress);
  ws.onopen = function(event) { 
    console.log("Connection open ...");
  };

  ws.onmessage = function(event) {
    let message = JSON.parse(event.data);
    me.newMessageReceived(message);
  };

  ws.onclose = function(event) {
    console.log("Connection closed.");
    // Re-connect after 3s.
    setTimeout(function() {
      me.connect();
    }, 3000);
  };
}

function newMessageReceived(message) {
  if (message.from && message.to === this.username) {
    if (message.timestamp) {
      message.timestamp = new Date(message.timestamp);
    }
    message.me = false;

    // Look up in active dialogs.
    for (let dialog of this.dialogs) {
      if (dialog.friend.username === message.from) {
        dialog.messages.push(message);
        dialog.friend.hasUnread = true;
        return;
      }
    }

    // Look up in inactive dialogs.
    let dialog = this.inactiveDialogs[message.from];
    if (dialog) {
      dialog.messages.push(message);
      dialog.friend.hasUnread = true;
      this.addActiveDialog(dialog);
      delete this.inactiveDialogs[message.from];
      return;
    }

    // Open new dialog with this friend.
    let friend = this.friends[message.from];
    if (friend) {
      friend.hasUnread = true;
      let dialog = {
        active: false,
        minimized: false,
        focus: false,
        friend: friend,
        messages: [message],
      }
      this.addActiveDialog(dialog);
    }
  }
}

function addActiveDialog(dialog) {
  if (this.dialogs.length == 5) {
    // Move the last active dialog to inactive list.
    let last = this.dialogs[4];
    last.active = false;
    this.inactiveDialogs[last.username] = last;
    this.dialogs.splice(4, 1);
  }
  this.dialogs = [dialog].concat(this.dialogs);
  dialog.active = true;
}

function beforeMount() {
  this.loadFriends();
  this.connect();
}

function openDialog(payload) {
  // First, blur all dialog boxes.
  for (let dialog of this.dialogs) {
    dialog.focus = false;
  }

  let friendName = payload.username;

  // Look up in active dialogs.
  for (let i = 0; i < this.dialogs.length; i++) {
    let dialog = this.dialogs[i];
    if (dialog.friend.username === friendName) {
      dialog.minimized = false;
      dialog.focus = true;
      return;
    }
  }

  // Look up in inactive dialogs.
  let dialog = this.inactiveDialogs[friendName];
  if (dialog) {
    dialog.minimized = false;
    dialog.focus = true;
    this.addActiveDialog(dialog);
    delete this.inactiveDialogs[friendName];
    return;
  }

  // If dialog does not exist, open new dialog with this friend.
  let friend = this.friends[friendName];
  if (friend) {
    let dialog = {
      active: false,
      minimized: false,
      focus: true,
      friend: friend,
      messages: [],
    }
    this.addActiveDialog(dialog);
  }
}

function closeDialog(payload) {
  // Do not actually remove the dialog. Move it to the "inactive" list.
  for (let i = 0; i < this.dialogs.length; i++) {
    let dialog = this.dialogs[i];
    if (dialog.friend.username === payload.username) {
      dialog.active = false;
      this.inactiveDialogs[payload.username] = dialog;
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

function sendMessage(payload) {
  let friendName = payload.username;
  let message = {
    me: true,
    from: this.username,
    to: friendName,
    content: payload.content,
    timestamp: new Date(),
  }

  let dialog = this.findDialogByFriend(friendName);
  if (dialog) {
    // Send message to websocket.
    this.sendMessageToBackend(message);
    dialog.messages.push(message);
  }
}

function sendMessageToBackend(message) {
  this.webSocket.send(JSON.stringify(message));
}

function findDialogByFriend(username) {
  for (let dialog of this.dialogs) {
    if (dialog.friend.username === username) {
      return dialog;
    }
  }
  return null;
}

function dialogBoxClickedHandler(payload) {
  let dialog = this.findDialogByFriend(payload.username);
  if (dialog) {
    // Send message to websocket.
    dialog.friend.hasUnread = false;
  }
}

export default {
  computed: {
  },

  methods: {
    loadFriends: loadFriends,
    connect: connect,
    openDialog: openDialog,
    closeDialog: closeDialog,
    flipMinimize: flipMinimize,
    sendMessage: sendMessage,
    findDialogByFriend: findDialogByFriend,
    sendMessageToBackend: sendMessageToBackend,
    newMessageReceived: newMessageReceived,
    dialogBoxClickedHandler: dialogBoxClickedHandler,
    addActiveDialog: addActiveDialog,
    getFriendsFromServer: getFriendsFromServer,
  },

  beforeMount: beforeMount,
}
