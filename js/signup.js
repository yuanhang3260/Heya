define(["jquery"], function($) {
  function SignUp(el) {
    this.el = $(el)
    
    this.username_container = this.el.find("#signup-username");
    this.username_input = this.username_container.find("input[name=username]");

    this.email_container = this.el.find("#signup-email");
    this.email_input = this.email_container.find("input[name=email]");

    this.password_container = this.el.find("#signup-password");    
    this.password_input = this.password_container.find("input[name=password]");

    this.password_confirm_container = this.el.find("#signup-password-confirm");
    this.password_confirm_input = this.password_confirm_container.find("input[name=password-confirm]");

    this.username_valid = false;
    this.email_valid = false;
    this.password_valid = false;
    this.password_confirm_valid = false;
    this.submit_enable = false;

    // Username input box events.
    this.username_input.keyup($.proxy(this.UserNameKeyup, this));
    this.username_input.focusout($.proxy(this.checkUserName, this));

    // Email input box events.
    this.email_input.keyup($.proxy(this.EmailKeyUp, this));
    this.email_input.focusout($.proxy(this.checkEmail, this));

    // Password input box events.
    this.password_input.keyup($.proxy(this.PasswordKeyUp, this));
    this.password_input.focusout($.proxy(this.checkPassword, this));

    // Password confirm input box events.
    this.password_confirm_input.keyup($.proxy(this.PasswordConfirmKeyUp, this));
    this.password_confirm_input.focusout($.proxy(this.checkPasswordConfirm, this));

    // Submit button.
    this.el.find("button[type=submit]").click($.proxy(this.CheckSubmitValid, this));
    this.el.submit($.proxy(this.Submit, this));

    // this.el.find(".form-container").css("display", "none");
    // this.el.find(".success-info-container").css("display", "block");
    // this.StartRedirecting();
  }

  SignUp.prototype.EMAIL_TIP = "We\"ll never share your email with anyone else";
  SignUp.prototype.EMAIL_ERROR_TIP = "Invalid Email address format";
  SignUp.prototype.PASSWORD_CONFIRM_MISMATCH = "Different with password";

  // When typing, reset all tips and check message.
  SignUp.prototype.RestoreInput = function(container, input) {
    container.find(".heya-signup-tip").removeClass("heya-signup-tip-error");
    container.find(".signup-input-check").css("display", "none");
    container.find(".signup-input-check-error").css("display", "none");
    input.removeClass("inputbox-error");
  };

  SignUp.prototype.restoreInput = function(input) {
    input.removeClass("inputbox-error");
  };

  SignUp.prototype.InputError = function(container, input) {
    input.addClass("inputbox-error");
    container.find(".signup-input-check").css("display", "none");
    container.find(".signup-input-check-error").css("display", "inline-block");
  };

  SignUp.prototype.InputCorrect = function(container, input) {
    input.removeClass("inputbox-error");
    container.find(".signup-input-check").css("display", "inline-block");
    container.find(".signup-input-check-error").css("display", "none");
  };

  SignUp.prototype.UserNameKeyup = function(container, input) {
    this.RestoreInput(this.username_container, this.username_input);
  };

  SignUp.prototype.CheckSubmitValid = function() {
    this.submit_enable = (this.username_valid && this.email_valid &&
                          this.password_valid && this.password_confirm_valid);
    if (this.submit_enable) {
      return true;
    } else {
      return (this.checkUserName() && this.checkEmail() &&
              this.checkPassword() && this.checkPasswordConfirm());
    }
  }

  // Check username is valid.
  SignUp.prototype.checkUserName = function() {
    var username = this.username_input.val();
    var length = username.length;
    if (username) {
      var valid = true;
      for (var i = 0; i < length; i++) {
        if ((username.charAt(i) >= "a" && username.charAt(i) <= "z") ||
            (username.charAt(i) >= "A" && username.charAt(i) <= "Z") ||
            username.charAt(i) == "_") {
          continue;
        } else {
          valid = false;
          break;
        }
      }
      if (!valid) {
        this.username_valid = false;
        this.InputError(this.username_container, this.username_input);
        this.username_container.find(".heya-signup-tip").addClass("heya-signup-tip-error");
        return false;
      } else {
        this.username_valid = true;
        this.InputCorrect(this.username_container, this.username_input);
        return true;
      }
    } else {
      this.RestoreInput(this.username_container, this.username_input);
      return false;
    }
  };

  SignUp.prototype.EmailKeyUp = function(container, input) {
    this.email_container.find(".heya-signup-tip").html(this.EMAIL_TIP);
    this.RestoreInput(this.email_container, this.email_input);
  };

  // Check email is valid.
  SignUp.prototype.checkEmail = function() {
    var email = this.email_input.val();
    var length = email.length;
    if (email) {
      var valid = true;
      var at_exist = false;
      for (var i = 0; i < length; i++) {
        if (email.charAt(i) == "@") {
          if (!at_exist) {
            at_exist = true;
          } else {
            valid = false;
            break;
          }
        }
      }
      if (!at_exist) {
        valid = false;
      }

      if (email.charAt(0) == "@" || email.charAt(length-1) == "@") {
        valid = false;
      }

      if (!valid) {
        this.email_valid = false;
        this.InputError(this.email_container, this.email_input);
        this.email_container.find(".heya-signup-tip").html(this.EMAIL_ERROR_TIP);
        this.email_container.find(".heya-signup-tip").addClass("heya-signup-tip-error");
        return false;
      } else {
        this.email_valid = true;
        this.InputCorrect(this.email_container, this.email_input);
        return true;
      }
    } else {
      this.RestoreInput(this.email_container, this.email_input);
      return false;
    }
  };

  SignUp.prototype.checkPasswordMatch = function(password, password_confirm) {
    if (password_confirm == password) {
      this.password_confirm_valid = true;
      this.InputCorrect(this.password_confirm_container, this.password_confirm_input);
      this.password_confirm_container.find(".heya-signup-tip").html("*");
      this.password_confirm_container.find(".heya-signup-tip").addClass("heya-signup-tip-hidden");
      return true;
    } else {
      this.password_confirm_valid = false;
      this.InputError(this.password_confirm_container, this.password_confirm_input);
      this.password_confirm_container.find(".heya-signup-tip").html(this.PASSWORD_CONFIRM_MISMATCH);
      this.password_confirm_container.find(".heya-signup-tip").addClass("heya-signup-tip-error");
      this.password_confirm_container.find(".heya-signup-tip").removeClass("heya-signup-tip-hidden");
      return false;
    }
  };

  SignUp.prototype.PasswordKeyUp = function(container, input) {
    this.RestoreInput(this.password_container, this.password_input);
  };

  // Check password is valid.
  SignUp.prototype.checkPassword = function() {
    var password = this.password_input.val();
    var length = password.length;
    if (password) {
      this.password_valid = true;
      this.InputCorrect(this.password_container, this.password_input);

      // Check with password confirm is it"s not empty.
      var password_confirm = this.password_confirm_input.val();
      if (password_confirm) {
        return this.checkPasswordMatch(password, password_confirm);
      }
    } else {
      this.RestoreInput(this.password_container, this.password_input);
      return false;
    }
  };

  SignUp.prototype.PasswordConfirmKeyUp = function(container, input) {
    this.RestoreInput(this.password_confirm_container, this.password_confirm_input);
    this.password_confirm_container.find(".heya-signup-tip").html("*");
    this.password_confirm_container.find(".heya-signup-tip").addClass("heya-signup-tip-hidden");

    if (this.password_input.val() &&
        this.password_confirm_input.val() == this.password_input.val()) {
      this.password_confirm_valid = true;
      this.InputCorrect(this.password_confirm_container, this.password_confirm_input);
    } else {
      this.password_confirm_valid = false;
    }
  };

  // Check password confirm matches password.
  SignUp.prototype.checkPasswordConfirm = function() {
    var password = this.password_input.val();
    var password_confirm = this.password_confirm_input.val();
    if (password && password_confirm) {
      return this.checkPasswordMatch(password, password_confirm);
    } else {
      return false;
    }
  };

  // Submit with AJAX.
  SignUp.prototype.Submit = function(event) {
    // Stop the form from submitting the normal way and refreshing the page.
    event.preventDefault();

    var formData = {
        "username" : this.username_input.val(),
        "email" : this.email_input.val(),
        "password" : this.password_input.val(),
    };

    // Disable the submit button.
    this.el.find("button[type=submit]").attr("disabled", "");

    // Process the form.
    var form = this.el;
    var me = this;
    $.ajax({
        type : "POST",
        url : "signup",
        data : formData,
        dataType : "json",
        encode : true,
    }).done(function(data) {
      // log data to the console so we can see.
      console.log(data);

      form.find("button[type=submit]").removeAttr("disabled");
      if (data.success) {
        form.find(".form-container").css("display", "none");
        form.find(".success-info-container").css("display", "block");
        me.StartRedirecting();
      } else {
        // Show error message.
        form.find(".submit-error-msg-container").css("display", "inline-block");
        form.find(".submit-error-msg-container").html(
            "<i class=\"fa fa-times-circle\"></i> " + data.error);
      }
    });
  };

  SignUp.prototype.StartRedirecting = function() {
    var count_sec = 5;
    var container = this.el.find(".success-info-container");
    var remain_time_el = container.find(".remain-time");

    var startTime = (new Date()).getTime();
    var remain_time_reader = window.setInterval(function() {
      var remain =
          count_sec - Math.floor(((new Date()).getTime() - startTime) / 1000);
      if (remain >= 0) {
        remain_time_el.html(" " + remain + " ");
      }
    }, 200);

    var timer = window.setTimeout(function() {
      window.clearInterval(remain_time_reader);
      window.location.assign("home.jsp");
    }, count_sec * 1000);
  };

  return {
    SignUp: SignUp,
  }
});
