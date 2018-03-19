import $ from "jquery";

function Login(el) {
  this.el = $(el);
  this.username_input = this.el.find("input[name=username]");
  this.password_input = this.el.find("input[name=password]");

  this.el.keyup($.proxy(this.checkInputNonEmpty, this));

  this.el.submit($.proxy(this.Submit, this));
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

// Submit login with AJAX.
Login.prototype.Submit = function(event) {
  // Stop the form from submitting the normal way and refreshing the page.
  event.preventDefault();

  var formData = {
      "username" : this.username_input.val(),
      "password" : this.password_input.val(),
  };

  // Disable the submit button.
  this.el.find("button[type=submit]").attr("disabled", "");

  // Process the form.
  var form = this.el;
  var me = this;
  $.ajax({
      type : "POST",
      url : "login",
      data : formData,
      dataType : "json",
      encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);

    form.find("button[type=submit]").removeAttr("disabled");
    if (data.success) {
      form.find(".login-error-msg").hide();
      window.location.assign("home");
    } else {
      // Show error message.
      form.find(".login-error-msg").html(data.error);
      form.find(".login-error-msg").show();
    }
  });
};

export default Login;
