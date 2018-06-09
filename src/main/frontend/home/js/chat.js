import $ from "jquery";

import debug from "heya/common/js/debug.js";

function loadFriends() {
  if (this.debug) {
    this.friends = debug.friends();
  } else {
    // TODO: load friends list from backend.
  }
}

function beforeMount() {
  this.loadFriends();
}

export default {
  computed: {
  },

  methods: {
    loadFriends: loadFriends,
  },

  beforeMount: beforeMount,
}
