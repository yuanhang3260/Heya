import $ from "jquery";

function postButtomDisabled() {
  return !this.postTextInput;
}

export default {
  computed: {
    postButtomDisabled: postButtomDisabled
  },

  methods: {
  }
}
