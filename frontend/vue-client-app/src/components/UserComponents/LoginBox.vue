<template>
  <div class="login-box">
    <router-link to="/"
      ><p class="close-btn-p">
        <b-icon class="text-muted" id="close-btn" icon="x"></b-icon></p
    ></router-link>
    <h2 class="header">GIDD og logg deg inn da</h2>

    <div class="input-container">
      <p v-if="!isEmailValid">Skriv inn en gyldig e-postadresse</p>
      <input
        type="text"
        class="input"
        id="email"
        placeholder="E-post"
        v-model="emailValue"
        v-on:keyup.enter="loginUser"
      />
    </div>
    <div class="input-container">
      <p v-if="!isPasswordValid">Passord må være mellom 5 og 16 tegn</p>
      <input
        type="password"
        class="input"
        id="password"
        placeholder="Passord"
        v-model="passwordValue"
        v-on:keyup.enter="loginUser"
      />
    </div>

    <button id="Login-btn" @click="loginUser">Logg inn</button>
    <router-link to="/register"
      ><p class="register-text">
        Har du ikke bruker? Klikk her for å registrere deg
      </p></router-link
    >
    <router-link to="/forgotPassword"
      ><p class="register-text">Glemt passord?</p></router-link
    >
    <div v-if="showLoginError" class="invalid-login">
      <p class="invalid-login-text">
        Email or password is not correct, please try again
      </p>
      <p @click="closeInvalidLoginError" class="x-icon">
        <b-icon icon="x"></b-icon>
      </p>
    </div>
    <div class="spacing" v-if="!showLoginError"></div>
  </div>
</template>
<script>
/**
 * userService is imported for efficient use of functions
 */
import { userService } from "../../services/UserService.js";

/**
 * LoginBox is a component which displays login.
 *
 * @author Mattias Agentoft Eggen
 */
export default {
  name: "LoginBox",
  data() {
    return {
      /**
       * isEmailValid tells us if the email is valid
       */
      isEmailValid: true,
      /**
       * isPasswordValid tells us if password is valid
       */
      isPasswordValid: true,
      /**
       * emailValue contains account email
       */
      emailValue: "",
      /**
       * passwordValue contains account password value
       */
      passwordValue: "",
      /**
       * showLoginError tells us if there is a login error
       */
      showLoginError: false,
    };
  },
  methods: {
    /**
     * loginUser is a function which runs account login
     */
    loginUser() {
      this.isEmailValid = true;
      if (!this.validateEmail()) {
        this.isEmailValid = false;
      }
      if (this.isEmailValid) {
        this.login();
      }
    },

    /**
     * validateEmail is a function which validates account email
     */
    validateEmail() {
      const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(String(this.emailValue).toLowerCase());
    },

    /**
     * login is a function which also runs a part of account login
     */
    async login() {
      let response = await userService.login(
        this.emailValue,
        this.passwordValue
      );

      if (response.jwtToken) {
        this.$router.push("/dashboard");
      } else {
        this.showLoginError = true;
      }
    },

    /**
     * closeInvalidLoginError is a function which closes login error box
     */
    closeInvalidLoginError() {
      this.showLoginError = false;
    },
  },
};
</script>

<style scoped>
.header {
  margin-top: 50px;
}

.login-box {
  position: relative;
  width: 510px;
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  justify-content: center;
  margin: 0;
  padding: 0;
  background: white;
}

.input-container {
  font-family: "Mulish";
  width: 70%;
  margin: 10px 0;
  display: flex;
  flex-flow: column;
}

.input-container p {
  margin: 0 0 4px 0;
  font-size: 13px;
  align-self: flex-start;
  color: red;
}

.input {
  width: 100%;
  height: 50px;
  padding: 0 10px;
  background-color: #f6f6f6;
  border: none;
  outline: none;
}

#Login-btn {
  margin-top: 20px;
  font-family: "Mulish";
  font-size: 20px;
  width: 250px;
  height: 42px;
  color: white;
  background: #ffbd3e;
  border: none;
  border-radius: 6px;
  outline: none;
}

#Login-btn:hover {
  background-color: #efb03a;
  cursor: pointer;
}

#close-btn {
  position: absolute;
  top: 5px;
  right: 5px;
}

.close-btn-p {
  font-size: 50px;
}

.register-text {
  opacity: 75%;
  margin-top: 15px;
  font-size: 14px;
  margin-bottom: 20px;
}

.register-text:hover {
  cursor: pointer;
  text-decoration: underline;
}

.invalid-login {
  width: 75%;
  height: 50px;
  background-color: #ffdbdf;
  border: solid 1px #b7666a;
  margin: 20px 0 40px 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.invalid-login .invalid-login-text {
  color: #91595c;
  font-size: 13px;
  margin: 0 0 0 20px;
}

.x-icon {
  font-size: 20px;
  color: #492728;
  margin: 0 10px 0 0;
}

.x-icon:hover {
  cursor: pointer;
}

.spacing {
  margin-bottom: 40px;
}

@media (max-width: 600px) {
  .login-box{
    width: 380px;
  }  

  .header {
    font-size: 28px;
  }
}
</style>