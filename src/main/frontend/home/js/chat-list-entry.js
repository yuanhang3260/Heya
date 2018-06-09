function openDialog() {
  this.$emit("open-dialog", {
    username: this.friend.username,
  });
}


export default {
  computed: {
  },

  methods: {
    openDialog: openDialog
  },

}
