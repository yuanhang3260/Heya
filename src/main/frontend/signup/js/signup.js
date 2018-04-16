import $ from 'jquery';

function checkUsername(allowEmpty) {
  let username = this.username;
  if (username) {
    let valid = true;
    let length = username.length;
    for (let i = 0; i < length; i++) {
      if ((username.charAt(i) >= "a" && username.charAt(i) <= "z") ||
          (username.charAt(i) >= "A" && username.charAt(i) <= "Z") ||
          (username.charAt(i) >= "0" && username.charAt(i) <= "9") ||
          username.charAt(i) == "_") {
        continue;
      } else {
        valid = false;
        break;
      }
    }
    if (!valid) {
      this.usernameValid = false;
    } else {
      this.usernameValid = true;
    }
  } else if (allowEmpty) {
    this.usernameValid = true;
  } else {
    this.usernameValid = false;
  }
  return;
}

function checkEmail(allowEmpty) {
  let email = this.email;
  if (email) {
    let valid = true;
    let at_exist = false;
    let length = email.length;
    for (let i = 0; i < length; i++) {
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
      this.emailValid = false;
    } else {
      this.emailValid = true;
    }
  } else if (allowEmpty) {
    this.emailValid = true;
    return false;
  } else {
    this.emailValid = false;
    return false;
  }
}

function emailTip() {
  return this.emailValid ? "We'll never share your email with anyone else." :
                           "Invalid Email address format";
}

function checkPasswordMatch() {
  let password = this.password;
  let password_confirm = this.passwordConfirm;
  if (password) {
    if (password_confirm && password_confirm !== password) {
      this.passwordMatchValid = false;
    } else {
      this.passwordMatchValid = true;
    }
  } else if (password_confirm) {
    this.passwordMatchValid = false;
  } else {
    this.passwordMatchValid = true;
    return false;
  }
}

function passwordMatchTip() {
  return this.passwordMatchValid ? "Enter password again." : "Different with password";
}

function restoreInput(name) {
  this[name + "Valid"] = true;
}

function doSubmit() {
  this.checkUsername(/*allow-empty=*/false);  
  this.checkEmail(/*allow-empty=*/false);

  if (!this.password) {
    this.passwordValid = false;
  } else if (!this.passwordConfirm) {
    this.passwordMatchValid = false;
  } else {
    this.checkPasswordMatch();
  }

  if (this.usernameValid && this.emailValid &&
      this.passwordValid && this.passwordMatchValid) {
    this.signup();
    //this.mode = "redirect";
    //this.startRedirecting();
  }
}

function signup() {
  let formData = {
    "username" : this.username,
    "email" : this.email,
    "password" : this.password,
  };

  this.disableSubmit = true;

  let me = this;
  $.ajax({
    type : "POST",
    url : "signup/new",
    data : formData,
    dataType : "json",
    encode : true,
  }).done(function(data) {
    // log data to the console so we can see.
    console.log(data);

    me.disableSubmit = false;
    if (data.success) {
      me.mode = "redirect";
      me.startRedirecting();
    } else {
      me.errMsg = data.error || "Signup failed";
    }
  }).fail(function(data) {
    me.disableSubmit = false;
    me.errMsg = "Signup error";
  });;
};

function startRedirecting() {
  let count_sec = 5;

  let me = this;
  let startTime = (new Date()).getTime();
  let remain_time_reader = window.setInterval(function() {
    var remain =
        count_sec - Math.floor(((new Date()).getTime() - startTime) / 1000);
    if (remain >= 0 && this.countDown != remain) {
      me.countDown = remain;
    }
  }, 200);

  var timer = window.setTimeout(function() {
    window.clearInterval(remain_time_reader);
    window.location.assign("/Heya/home");
  }, count_sec * 1000);
};

function signupMode() {
  return this.mode === "signup";
}

export default {
  computed: {
    emailTip: emailTip,
    passwordMatchTip: passwordMatchTip,
    signupMode: signupMode,
  },

  methods: {
    checkUsername: checkUsername,
    checkEmail: checkEmail,
    checkPasswordMatch: checkPasswordMatch,
    restoreInput: restoreInput,
    doSubmit: doSubmit,
    signup: signup,
    startRedirecting: startRedirecting,
  }
}

