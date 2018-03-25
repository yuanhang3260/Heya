import utils from "./utils.js"

function setInput() {
  this.companyInput = this.company;
  this.positionInput = this.position;
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
  if (!this.companyInput) {
    this.errMsg = "company name is required";
    return;
  }

  let payload = {
    cid: this.cid,
    type: this.type,
    company: this.companyInput,
    position: this.positionInput,
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
