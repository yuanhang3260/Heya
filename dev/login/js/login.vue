<template>

<div class="main-container-sub login-container">
  <form v-on:submit="doLogin" method="post" action="login">
    <h3 class="login-title">Welcome</h3>
    <div v-if="errMsg" class="alert alert-danger login-error-msg" role="alert">{{errMsg}}</div>
    <div class="input-group mb-2">
      <div class="input-group-prepend">
        <span class="input-group-text"><i class="fa fa-user"></i></span>
      </div>
      <input v-model="username" type="text" class="form-control" name="username" placeholder="Username">
    </div>
    <div class="input-group mb-1">
      <div class="input-group-prepend">
        <span class="input-group-text password-icon"><i class="fa fa-unlock-alt"></i></span>
      </div>
      <input v-model="password" type="password" class="form-control" name="password" placeholder="Password">
    </div>
    <div class="form-check mb-3">
      <label class="form-check-label login-check-label">
        <input v-bind:disabled="buttonDisabled" type="checkbox" class="form-check-input login-checkbox" name="rememberme" checked="true">
        <span>Remember me</span>
      </label>
    </div>
    <button v-bind:disabled="buttonDisabled" type="submit" class="btn btn-success login-btn mb-3">Log In</button>
  </form>
  <form action="signup.html">
    <button class="btn btn-success login-btn mb-2">New User</button>
  </form>
</div>

</template>


<script>
import $ from "jquery";

export default {
  name: "login",
  props: {},
  data() {
    return {
      username: null,
      password: null,
      disableSubmit: false,
      errMsg: null,
    }
  },
  computed: {
    buttonDisabled: function() {
      return !this.username || !this.password || this.disableSubmit;
    }
  },
  methods: {
    doLogin: function(event) {
      event.preventDefault();

      var formData = {
        "username": this.username,
        "password": this.password,
      };

      this.disableSubmit = true;
      let me = this;
      $.ajax({
          type : "POST",
          url : "login",
          data : formData,
          dataType : "json",
          encode : true,
      }).done(function(data) {
        console.log(data);
        me.disableSubmit = false;
        me.errMsg = null;

        if (data.success) {
          window.location.assign("home");
        } else {
          // Show error message.
          me.this.errMsg = data.error;
        }
      }).fail(function(data) {
        me.disableSubmit = false;
        me.errMsg = "Login error";
      });
    }
  }
};
</script>

<style scoped lang="scss">
@import "login/css/login.scss";
</style>
