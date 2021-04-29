<template>
  <div class="input-box">
    <router-link to="/"
      ><p class="close-btn-p">
        <b-icon class="text-muted" id="close-btn" icon="x"></b-icon></p
    ></router-link>

    <h2 class="header">Bytt passord 🔒</h2>
    <div class="info">
      Hei, skriv inn ett nytt passord til brukeren koblet til {{ this.email }}.
    </div>

    <div class="input-container">
      <p v-if="!isPasswordValid">Passord må være mellom 5 og 16 tegn</p>
      <input
        type="password"
        class="input"
        id="password"
        placeholder="Nytt passord"
        v-model="passwordValue"
        v-on:input="testPasswords"
        v-bind:disabled="submitted"
        v-on:keyup.enter="submitPassword"
      />
    </div>

    <div class="input-container">
      <p v-if="!isPasswordsEqual">Passordene må være like</p>
      <input
        type="password"
        class="input"
        id="confirm-password"
        placeholder="Bekreft nytt passord"
        v-model="confirmPasswordValue"
        v-on:input="testPasswords"
        v-bind:disabled="submitted"
        v-on:keyup.enter="submitPassword"
      />
    </div>

    <button
      id="submit"
      v-if="testPasswords"
      @click="submitPassword"
      v-bind:disabled="submitted"
    >
      Bytt Passord
    </button>

    <div class="spacing"></div>

    <ConfirmModal
      name="success-modal"
      header="Success!"
      info="Brukerens passord ble oppdatert, sender deg til login"
      buttonText="OK"
    />
    <ErrorModal
      name="error-modal"
      header="Error"
      info="Noe gik galt under oppdatering av passord"
      buttonText="OK"
    />
  </div>
</template>

<script>
import ConfirmModal from "../PopUpComponents/ConfirmModal.vue";
import ErrorModal from "../PopUpComponents/ErrorModal.vue";

export default {
  name: "ResetPasswordBox",
  components: {
    ConfirmModal,
    ErrorModal,
  },
  data() {
    return {
      submitted: false,
      isPasswordValid: true,
      isPasswordsEqual: true,
      didCheck: false,
      passwordValue: "",
      confirmPasswordValue: "",
      email: null,
      resetSuffix: this.$route.params.passwordSuffix,
    };
  },
  async created() {
    await fetch("http://localhost:8080/reset/" + this.resetSuffix, {
      method: "GET",
    })
      .then((response) => response.json())
      .then((data) => {
        this.email = data.email;
        if (this.email === null) {
          alert(
            "The password link is either wrong or timed out, redirecting to login"
          );
          this.$router.push({ path: "/login" });
        }
      })
      .catch((error) => console.error("Error:", error));
  },
  methods: {
    testPasswords() {
      this.didCheck = true;
      this.isPasswordsEqual = this.passwordValue === this.confirmPasswordValue;
      let pLength = this.passwordValue.length;
      this.isPasswordValid = pLength >= 5 && pLength <= 16;
    },
    async submitPassword() {
      this.submitted = true;
      if (this.isPasswordValid && this.isPasswordsEqual && this.didCheck) {
        await fetch("http://localhost:8080/reset/" + this.resetSuffix, {
          method: "PUT",
          body: this.passwordValue,
        })
          .then((response) => (this.submitted = response))
          .catch((error) => console.error("ERROR: ", error))
          .then(() => {
            if (this.submitted) {
              this.$bvModal.show("success-modal");
            } else {
              this.$bvModal.show("error-modal");
            }
            setTimeout(() => this.$router.push({ path: "/login" }), 3000);
          });
      } else {
        this.submitted = false;
      }
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

#submit:disabled {
  opacity: 0.3;
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