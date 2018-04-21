
function postButtomDisabled() {
  return !this.postTextInput;
}

function clickEditButton(item) {
  if (item === this.editorSelected) {
    this.editorSelected = null;
    return;
  } else {
    this.editorSelected = item;
  }
}

export default {
  computed: {
    postButtomDisabled: postButtomDisabled
  },

  methods: {
    clickEditButton: clickEditButton,
  }
}
