define(["jquery"], function($) {
  function Login(el) {
    this.el = $(el);
    this.username_input = this.el.find("input[name=username]");
    this.password_input = this.el.find("input[name=password]");

    this.username_input_ever = false;

    this.el.keyup($.proxy(this.checkInputNonEmpty, this));

    this.username_input.focusout(
        $.proxy(this.checkUserNameNonEmpty, this));
    this.username_input.focus(
        $.proxy(this.restoreUsernameInput, this));
  };

  Login.prototype.checkInputNonEmpty = function() {
    if (this.username_input.val()) {
      this.username_input_ever = true;
      if (this.password_input.val()) {
        this.enable();
        return;
      }
    }
    this.disable();
  };

  Login.prototype.checkUserNameNonEmpty = function() {
    if (this.username_input_ever && !this.username_input.val()) {
      // this.username_input.attr("placeholder", "Error");
      this.username_input.addClass("inputbox-error");
    }
  };

  Login.prototype.disable = function() {
    this.el.find("button[type=submit]").attr("disabled", "");
    this.el.find("input[name=rememberme]").attr("disabled", "");
  }

  Login.prototype.enable = function() {
    this.el.find("button[type=submit]").removeAttr("disabled");
    this.el.find("input[name=rememberme]").removeAttr("disabled");
  }

  Login.prototype.restoreUsernameInput = function() {
    // this.username_input.attr("placeholder", "Username");
    this.username_input.removeClass("inputbox-error");
  }

  return {
    Login: Login,
  }
});
