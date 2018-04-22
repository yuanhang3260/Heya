import debug from "heya/common/js/debug.js"

function beforeMount() {
  this.getPosts();
}

function getPosts() {
  if (this.debug) {
    this.posts = debug.posts();
  } else {
    this.loadPosts();
  }
}

function loadPosts() {
  // TODO: load posts from backend.
  return [];
}

export default {
  computed: {
  },

  methods: {
    getPosts: getPosts,
    loadPosts: loadPosts,
  },

  beforeMount: beforeMount,
}
