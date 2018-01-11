define(["jquery"], function($) {
  function Login(el) {
    this.el = $(el);
    this.username_input = this.el.find("input[name=username]");
    this.password_input = this.el.find("input[name=password]");

    this.el.keyup($.proxy(this.checkInputNonEmpty, this));
  };

  Login.prototype.checkInputNonEmpty = function() {
    if (this.username_input.val() && this.password_input.val()) {
      this.enable();
    } else {
      this.disable();
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

  return {
    Login: Login,
  }
});
