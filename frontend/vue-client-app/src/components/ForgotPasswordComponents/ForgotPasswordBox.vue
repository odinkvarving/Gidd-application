<template>
  <div class="input-box">
    <router-link to="/"
      ><p class="close-btn-p">
        <b-icon class="text-muted" id="close-btn" icon="x"></b-icon></p
    ></router-link>
    <h2 class="header">Glemt passord? 🤯</h2>
    <div class="info">
      Skriv inn den registrerte e-posten din, så skal vi se om vi kan hjelpe!
    </div>
    <div class="input-container">
      <p v-if="!isEmailValid">Skriv inn en gyldig e-postadresse</p>
      <input
        type="text"
        class="input"
        id="email"
        placeholder="E-post"
        v-model="emailValue"
        v-bind:disabled="sent"
      />
    </div>
    <div v-if="sent" class="show-sent">
      Hvis {{ emailValue }} er koblet til en konto vil en e-post sendes med en
      beskrivelse på hvordan du kan skifte passord.
    </div>
    <button id="submit" @click="postAddress" v-bind:disabled="sent">
      Send
    </button>
    <div class="spacing"></div>
  </div>
</template>

<script>
export default {
  name: "ForgotPasswordBox",
  data() {
    return {
      isEmailValid: true,
      emailValue: "",
      sent: false,
    };
  },
  methods: {
    postAddress() {
      this.isEmailValid = true;
      if (!this.validateEmail()) {
        this.isEmailValid = false;
      }
      if (this.isEmailValid) {
        this.send();
        this.sent = true;
      }
    },
    validateEmail() {
      const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
      return re.test(String(this.emailValue).toLowerCase());
    },
    async send() {
      fetch("http://localhost:8080/reset/" + this.emailValue, {
        method: "POST",
      });
      console.log("Sent password reset request for " + this.emailValue);
    },
  },
};
</script>

<style scoped>
.header {
  margin-top: 50px;
}

.info {
  text-align: center;
}

.show-sent {
  text-align: center;
}

p {
  text-align: center;
}

.input-box {
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

.or-text {
  font-size: 13px;
  opacity: 75%;
  margin-top: 17px;
}

.input-container {
  font-family: "Mulish";
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
  width: 372px;
  height: 50px;
  padding: 0 10px;
  background-color: #f6f6f6;
  border: none;
  outline: none;
}

#submit:disabled {
  opacity: 0.3;
}

#submit {
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

#submit:hover {
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
</style>