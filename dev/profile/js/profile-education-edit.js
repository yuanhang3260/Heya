import utils from "./utils.js"

function setInput() {
  this.schoolInput = this.school;
  this.majorInput = this.major;
  this.yearStartInput = this.yearStart;
  this.yearEndInput = this.yearEnd;
}

function clickCancel() {
  this.$emit("cancel-edit");
}

function clickSave() {
  this.emitChanges();
}

function emitChanges() {
  if (!this.schoolInput) {
    this.errMsg = "School name is required";
    return;
  }

  let payload = {
    sid: this.sid,
    type: this.type,
    school: this.schoolInput,
    major: this.majorInput,
    year: {
      start: (this.yearStartInput && !isNaN(this.yearStartInput)) ?
             +this.yearStartInput : null,
      end: (this.yearEndInput && !isNaN(this.yearEndInput)) ?
           +this.yearEndInput : null,
    }
  };
  this.$emit("save-changes", payload);
}

export default {
  computed: {
  },

  methods: {
    setInput: setInput,
    clickSave: clickSave,
    clickCancel: clickCancel,
    emitChanges: emitChanges,
    yearSelectOptions: utils.yearSelectOptions,
  },
}
