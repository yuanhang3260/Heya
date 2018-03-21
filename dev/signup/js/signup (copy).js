SignUp.prototype.EMAIL_ERROR_TIP = "Invalid Email address format";
SignUp.prototype.PASSWORD_CONFIRM_MISMATCH = "Different with password";

// When typing, reset all tips and check message.
SignUp.prototype.RestoreInput = function(container, input) {
  container.find(".heya-signup-tip").removeClass("heya-signup-tip-error");
  container.find(".signup-input-check").hide();
  container.find(".signup-input-check-error").hide();
  input.removeClass("inputbox-error");

  this.el.find(".submit-error-msg-container").hide();
};

SignUp.prototype.InputError = function(container, input) {
  input.addClass("inputbox-error");
  container.find(".signup-input-check").hide();
  container.find(".signup-input-check-error").show();
};

SignUp.prototype.InputCorrect = function(container, input) {
  input.removeClass("inputbox-error");
  container.find(".signup-input-check").show();
  container.find(".signup-input-check-error").hide();
};

SignUp.prototype.ValidKeyUp = function(event) {
  var x = event.keyCode || event.which;
  if (x == 13) {
    // Don't respond to Enter key.
    return false;
  } else {
    return true;
  }
}

SignUp.prototype.UserNameKeyup = function(event) {
  if (this.ValidKeyUp(event)) {
    this.RestoreInput(this.username_container, this.username_input);
  }
};

SignUp.prototype.CheckSubmitValid = function() {
  this.submit_enable = (this.username_valid && this.email_valid &&
                        this.password_valid && this.password_confirm_valid);
  if (this.submit_enable) {
    return true;
  } else {
    // return this.checkPasswordConfirm();
    return (this.checkUserName(false) && this.checkEmail(false) &&
            this.checkPassword(false) && this.checkPasswordConfirm(false));
  }
}

// Check username is valid.
SignUp.prototype.checkUserName = function(allowEmpty) {
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
      this.username_container.find(".heya-signup-tip").
          addClass("heya-signup-tip-error");
      return false;
    } else {
      this.username_valid = true;
      this.InputCorrect(this.username_container, this.username_input);
      return true;
    }
  } else if (allowEmpty) {
    this.RestoreInput(this.username_container, this.username_input);
    return false;
  } else {
    this.InputError(this.username_container, this.username_input);
    return false;
  }
};

SignUp.prototype.EmailKeyUp = function(event) {
  if (this.ValidKeyUp(event)) {
    this.email_container.find(".heya-signup-tip").html(this.EMAIL_TIP);
    this.RestoreInput(this.email_container, this.email_input);
  }
};

// Check email is valid.
SignUp.prototype.checkEmail = function(allowEmpty) {
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
      this.email_container.find(".heya-signup-tip").
          html(this.EMAIL_ERROR_TIP);
      this.email_container.find(".heya-signup-tip").
          addClass("heya-signup-tip-error");
      return false;
    } else {
      this.email_valid = true;
      this.InputCorrect(this.email_container, this.email_input);
      return true;
    }
  } else if (allowEmpty) {
    this.RestoreInput(this.email_container, this.email_input);
    return false;
  } else {
    this.InputError(this.email_container, this.email_input);
    return false;
  }
};

SignUp.prototype.checkPasswordMatch = function(password, password_confirm) {
  var tip = this.password_confirm_container.find(".heya-signup-tip");
  if (password_confirm == password) {
    this.password_confirm_valid = true;
    this.InputCorrect(this.password_confirm_container,
                      this.password_confirm_input);
    tip.html("*");
    tip.addClass("heya-signup-tip-hidden");
    return true;
  } else {
    this.password_confirm_valid = false;
    this.InputError(this.password_confirm_container,
                    this.password_confirm_input);
    tip.html(this.PASSWORD_CONFIRM_MISMATCH);
    tip.addClass("heya-signup-tip-error");
    tip.removeClass("heya-signup-tip-hidden");
    return false;
  }
};

SignUp.prototype.PasswordKeyUp = function(event) {
  if (this.ValidKeyUp(event)) {
    this.RestoreInput(this.password_container, this.password_input);
  }
};

// Check password is valid.
SignUp.prototype.checkPassword = function(allowEmpty) {
  var password = this.password_input.val();
  var length = password.length;
  if (password) {
    this.password_valid = true;
    this.InputCorrect(this.password_container, this.password_input);

    // Check with password confirm is it"s not empty.
    var password_confirm = this.password_confirm_input.val();
    if (password_confirm) {
      return this.checkPasswordMatch(password, password_confirm);
    } else {
      // Note we return true when password is given but password_confirm is
      // empty. Non-empty check for password confirm is in
      // checkPasswordConfirm.
      return true;
    }
  } else if (allowEmpty) {
    this.RestoreInput(this.password_container, this.password_input);
    return false;
  } else {
    this.InputError(this.password_container, this.password_input);
    return false;
  }
};

SignUp.prototype.PasswordConfirmKeyUp = function(event) {
  if (this.ValidKeyUp(event)) {
    this.RestoreInput(this.password_confirm_container,
                      this.password_confirm_input);
    this.password_confirm_container.find(".heya-signup-tip").html("*");
    this.password_confirm_container.find(".heya-signup-tip").
        addClass("heya-signup-tip-hidden");

    if (this.password_input.val() &&
        this.password_confirm_input.val() == this.password_input.val()) {
      this.password_confirm_valid = true;
      this.InputCorrect(this.password_confirm_container,
                        this.password_confirm_input);
    } else {
      this.password_confirm_valid = false;
    }
  }
};

// Check password confirm matches password.
SignUp.prototype.checkPasswordConfirm = function(allowEmpty) {
  var password = this.password_input.val();
  var password_confirm = this.password_confirm_input.val();
  if (password && password_confirm) {
    return this.checkPasswordMatch(password, password_confirm);
  } else if (allowEmpty) {
    return false;
  } else {
    return this.checkPasswordMatch(password, password_confirm);
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
      form.find(".form-container").hide();
      form.find(".success-info-container").show();
      me.StartRedirecting();
    } else {
      // Show error message.
      form.find(".submit-error-msg-container").html(
          "<i class=\"fa fa-times-circle\"></i> " + data.error);
      form.find(".submit-error-msg-container").show();
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
    window.location.assign("home");
  }, count_sec * 1000);
};
