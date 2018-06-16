<template>

<div class="signup-container">
  <h3 class="login-title">Create New Account</h3>
  <form method="post" action="signup">
    <div v-show="signupMode" class="form-container">
      <div class="form-group">
        <label>Username</label>
        <input v-model="username" v-on:focusout="checkUsername(true)" v-on:input="restoreInput('username')" v-bind:class="{'inputbox-error': !usernameValid}" class="form-control heya-signup-input" type="text" name="username"  maxlength="32" placeholder="username">
        <span v-show="!usernameValid" class="fa fa-times-circle signup-input-check-error"></span>
        <small class="form-text heya-signup-tip" v-bind:class="{'heya-signup-tip-error': !usernameValid }">
          Letter Aa-Zz, digit 0-9 and underscore allowed.
        </small>
      </div>

      <div class="form-group">
        <label>Email address</label>
        <input v-model="email" v-on:focusout="checkEmail(true)" v-on:input="restoreInput('email')" v-bind:class="{'inputbox-error': !emailValid}" class="form-control heya-signup-input" type="email" name="email" maxlength="64" placeholder="email address">
        <span v-show="!emailValid"  class="fa fa-times-circle signup-input-check-error"></span>
        <small class="form-text heya-signup-tip" v-bind:class="{'heya-signup-tip-error': !emailValid }">{{emailTip}}</small>
      </div>

      <div class="form-group">
        <label>Password</label>
        <input v-model="password" v-on:focusout="checkPasswordMatch()" v-on:input="restoreInput('password')" v-bind:class="{'inputbox-error': !passwordValid}" type="password" name="password" class="form-control heya-signup-input" maxlength="32" placeholder="password">
        <span v-show="!passwordValid" class="fa fa-times-circle signup-input-check-error"></span>
        <small v-bind:class="{'heya-signup-tip-error': !passwordValid }" class="form-text heya-signup-tip">
          Use letter Aa-Zz, digit 0-9 and any special character.
        </small>
      </div>

      <div class="form-group">
        <label>Confirm Password</label>
        <input v-model="passwordConfirm" v-on:focusout="checkPasswordMatch()" v-on:input="restoreInput('passwordMatch')" v-bind:class="{'inputbox-error': !passwordMatchValid}" type="password" name="password-confirm" class="form-control heya-signup-input" maxlength="32" placeholder="confirm password">
        <span v-show="!passwordMatchValid" class="fa fa-times-circle signup-input-check-error"></span>
        <small v-bind:class="{'heya-signup-tip-error': !passwordMatchValid }" class="form-text heya-signup-tip">
          {{passwordMatchTip}}
        </small>
      </div>

      <button v-on:click="doSubmit" v-bind:disabled="disableSubmit" type="button" class="btn btn-success signup-btn mb-3">Submit</button>
      <span v-show="errMsg" class="submit-error-msg-container mb-3">{{errMsg}}</span>
    </div>

    <div v-show="!signupMode" class="success-info-container">
      <p class="success-title">New user is successfully created <i class="fa fa-smile-o new-user-smile"></i></p>
      <p>Redirecting to <a href="home" class="home-link">Home</a> page in<span class="remain-time"> {{countDown}} </span>s</p>
    </div>
  </form>
</div>

</template>

<script>
import signup from "./signup.js"

export default {
  name: "signup-box",
  props: {},
  data () {
    return {
      username: null,
      usernameValid: true,
      email: null,
      emailValid: true,
      password: null,
      passwordValid: true,
      passwordConfirm: null,
      passwordMatchValid: true,
      disableSubmit: false,
      mode: "signup",
      countDown: 5,
      errMsg: null,
    }
  },
  computed: signup.computed,
  methods: signup.methods,
};
</script>

<style scoped lang="scss">
@import "~heya/signup/css/signup.scss"
</style>
