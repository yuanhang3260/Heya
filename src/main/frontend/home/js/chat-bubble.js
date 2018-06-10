import common from "./common.js";

function profileImageURL() {
  return common.profileImageURL.apply(this, []);
}

export default {
  computed: {
    profileImageURL: profileImageURL,
  },

  methods: {
  },
}
