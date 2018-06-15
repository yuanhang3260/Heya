import $ from "jquery";

import debug from "heya/common/js/debug.js";

function loadFriends() {
  if (this.debug) {
    this.dialogs = debug.dialogs();
    this.friendsList = debug.friends();
    for (let friend of this.friendsList) {
      this.friends[friend.username] = friend;
    }
    this.connect();
  } else {
    this.getFriendsFromServer();
  }
}

function getFriendsFromServer() {
  var me = this;
  $.ajax({
      type: "GET",
      url: "friends/" + me.username,
      dataType: "json",
      encode: true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);
    if (data.success) {
      // We must set hasUnread here before assigning data.result.friends to
      // friendsList. Otherwise all bindings will not take effect.
      for (let friend of data.result.friends) {
        friend.hasUnread = false;
        friend.online = false;
      }

      me.friendsList = data.result.friends;
      for (let friend of me.friendsList) {
        me.friends[friend.username] = friend;
      }

      me.connect();
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
    if (message.client) {
      return;
    }
    me.processMessage(message);
  };

  ws.onclose = function(event) {
    console.log("Connection closed.");
    // Re-connect after 3s.
    setTimeout(function() {
      me.connect();
    }, 3000);
  };
}

function processMessage(message) {
  console.log(message);
  if (message.type === "NewMessage") {
    this.processNewMessage(message);
  } else if (message.type === "DialogRead") {
    this.processDialogRead(message);
  } else if (message.type === "ChatFriends") {
    this.processChatFriends(message);
  } else if (message.type === "FriendOnline") {
    this.processFriendOnline(message);
  } else if (message.type === "FriendOffline") {
    this.processFriendOffline(message);
  }
}

function processFriendOnline(message) {
  let friendName = message.username;
  if (!friendName) {
    return;
  }

  let friend = this.friends[friendName];
  if (friend) {
    friend.online = true;
  }
}

function processFriendOffline(message) {
  let friendName = message.username;
  if (!friendName) {
    return;
  }

  let friend = this.friends[friendName];
  if (friend) {
    friend.online = false;
  }
}

function processChatFriends(message) {
  if (!message.friends) {
    return;
  }

  for (let chatFriend of message.friends) {
    let friendName = chatFriend.username;
    let friend = this.friends[friendName];
    if (!friend) {
      continue;
    }

    friend.online = chatFriend.online;
    // If there is no dialog open with this friend, create one load all unread
    // message into it. This dialog will be added to inactive dialogs map. 
    if (!this.hasDialogWith(friendName) && chatFriend.unread.length > 0) {
      for (let message of chatFriend.unread) {
        message.to = this.username;
      }

      friend.hasUnread = true;
      let dialog = {
        minimized: true,
        focus: false,
        friend: friend,
        messages: chatFriend.unread,
      }
      this.inactiveDialogs[friendName] = dialog;
    }
  }
}

function processNewMessage(message) {
  if (message.from && message.to === this.username) {
    // Got new message from friends.

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
        minimized: false,
        focus: false,
        friend: friend,
        messages: [message],
      }
      this.addActiveDialog(dialog);
    }
  } else if (message.to && message.from === this.username) {
    // This is a self-sync message.
    if (message.timestamp) {
      message.timestamp = new Date(message.timestamp);
    }

    for (let dialog of this.dialogs) {
      if (dialog.friend.username === message.to) {
        dialog.messages.push(message);
        return;
      }
    }

    // Look up in inactive dialogs.
    let dialog = this.inactiveDialogs[message.to];
    if (dialog) {
      dialog.messages.push(message);
      return;
    }

    // Open new dialog with this friend. This dialog will be added to inactive
    // dialogs.
    let friend = this.friends[message.to];
    if (friend) {
      let dialog = {
        minimized: true,
        focus: false,
        friend: friend,
        messages: [message],
      }
      this.inactiveDialogs[friend.username] = friend;
    }
  }
}

function processDialogRead(dialogReadMsg) {

  function checkDialogRead(dialog, dialogReadMsg) {
    for (let i = dialog.messages.length - 1; i >= 0; i--) {
      let chatMessage = dialog.messages[i];
      if (chatMessage.from === dialogReadMsg.from) {
        if (dialogReadMsg.timestamp >= chatMessage.timestamp) {
          dialog.friend.hasUnread = false;
        }
        return;
      }
    }
  }

  if (dialogReadMsg.from && dialogReadMsg.to === this.username) {
    // Look up in active dialogs.
    for (let dialog of this.dialogs) {
      if (dialog.friend.username === dialogReadMsg.from) {
        checkDialogRead(dialog, dialogReadMsg);
        return;
      }
    }

    // Look up in inactive dialogs.
    let dialog = this.inactiveDialogs[dialogReadMsg.from];
    if (dialog) {
      checkDialogRead(dialog, dialogReadMsg);
      return;
    }
  }
}

function addActiveDialog(dialog) {
  if (this.dialogs.length == 5) {
    // Move the last active dialog to inactive list.
    let last = this.dialogs[4];
    this.inactiveDialogs[last.friend.username] = last;
    this.dialogs.splice(4, 1);
  }
  this.dialogs = [dialog].concat(this.dialogs);
}

function beforeMount() {
  this.loadFriends();
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

function hasDialogWith(friendName) {
  if (this.inactiveDialogs.friendName) {
    return true;
  }
  for (let dialog of this.dialogs) {
    if (dialog.friend.username === friendName) {
      return true;
    }
  }
  return false;
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
    type: "NewMessage",
    from: this.username,
    to: friendName,
    content: payload.content,
    timestamp: (new Date()).getTime(),
  }

  // Send message to websocket.
  this.sendMessageToBackend(message);
}

function dialogBoxClickedHandler(payload) {
  let friendName = payload.username;
  let message = {
    type: "DialogRead",
    from: friendName,
    to: this.username,
    timestamp: (new Date()).getTime(),
  }

  // Send message to websocket.
  this.sendMessageToBackend(message);
}

function sendMessageToBackend(message) {
  message["client"] = true;
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
    processMessage: processMessage,
    processNewMessage: processNewMessage,
    processDialogRead: processDialogRead,
    processChatFriends: processChatFriends,
    processFriendOnline: processFriendOnline,
    processFriendOffline: processFriendOffline,
    dialogBoxClickedHandler: dialogBoxClickedHandler,
    addActiveDialog: addActiveDialog,
    getFriendsFromServer: getFriendsFromServer,
    hasDialogWith: hasDialogWith,
  },

  beforeMount: beforeMount,
}
