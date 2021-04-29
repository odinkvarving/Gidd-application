<template>
  <div class="registration-box">
    <router-link to="/"
      ><p class="close-btn-p">
        <b-icon class="text-muted" id="close-btn" icon="x"></b-icon></p
    ></router-link>
    <h2 style="text-align: center">
      Registrer deg hos GIDD idag<br />Det er bare å GIDDE
    </h2>


    <div class="input-container">
      <p v-if="!nameValid">Ikke gyldig navn!</p>
      <input
        v-on:input="validateName"
        type="text"
        class="input"
        id="name"
        placeholder="Fullt navn"
        v-model="nameValue"
        v-bind:disabled="this.didSend"
      />
    </div>
    <div class="input-container">
      <p v-if="!phoneValid">Ikke gyldig telefon nummmer!(Husk landskode!)</p>
      <input
        v-on:input="validatePhone"
        type="text"
        class="input"
        id="phone"
        placeholder="Telefon"
        v-model="phoneValue"
        v-bind:disabled="this.didSend"
      />
    </div>
    <div class="input-container">
      <p v-if="!emailValid">Vennligst skriv inn en gyldig e-postadresse</p>
      <input
        v-on:input="validateEmail"
        type="text"
        class="input"
        id="email"
        placeholder="E-post"
        v-model="emailValue"
        v-bind:disabled="this.didSend"
      />
    </div>
    <div class="input-container">
      <p v-if="!passwordValid">Passord må være mellom 5 og 16 tegn</p>
      <input
        v-on:input="validatePassword"
        type="password"
        class="input"
        id="password"
        placeholder="Passord"
        v-model="passwordValue"
        v-bind:disabled="this.didSend"
      />
    </div>

    <button
      id="register-btn"
      @click="registerUser"
      v-bind:disabled="this.didSend"
    >
      Register
    </button>

    <ConfirmModal
      name="success-modal"
      header="Success!"
      info="Brukeren er nå registrert, sender deg til dashboard."
      buttonText="OK"
    />
    <ErrorModal
      name="error-modal"
      header="Error"
      info="Registreringen feilet, e-posten er allerede registrert"
      buttonText="OK"
    />
  </div>
</template>

<script>
import { userService } from "../../services/UserService";
import ConfirmModal from "../PopUpComponents/ConfirmModal.vue";
import ErrorModal from "../PopUpComponents/ErrorModal.vue";

export default {
  name: "RegistrationBox",
  components: {
    ConfirmModal,
    ErrorModal,
  },
  data() {
    return {
      didSend: false,
      nameValid: true,
      phoneValid: true,
      emailValid: true,
      passwordValid: true,
      nameValue: "",
      phoneValue: "",
      emailValue: "",
      passwordValue: "",
      showModal: false,
    };
  },
  methods: {
    async registerUser() {
      console.log("Register button clicked");
      if (this.validInputs()) {
        this.didSend = true;
        console.log("All inputs are valid! Creating user...");
        await this.sendNewUserToServer();
        userService.logout();
        await userService.login(this.emailValue, this.passwordValue);
        await setTimeout(() => this.$router.push({ path: "/dashboard" }), 3000);
      }
    },
    validInputs() {
      this.validateName();
      this.validatePhone();
      this.validateEmail();
      this.validatePassword();

      return (
        this.nameValid && this.phoneValid && this.emailValid && this.phoneValid
      );
    },
    validateName() {
      let regName = /^[a-zA-Z]+ [a-zA-Z]+$/;
      this.nameValid = regName.test(this.nameValue);
    },
    validatePhone() {
      let regPhone = /^\+?([0-9]{2})\)?[-. ]?([0-9]{4})[-. ]?([0-9]{4})$/;
      this.phoneValid = regPhone.test(this.phoneValue);
    },
    validateEmail() {
      const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

      this.emailValid = re.test(String(this.emailValue).toLowerCase());
    },
    validatePassword() {
      this.passwordValid =
        this.passwordValue.length >= 6 && this.passwordValue.length <= 16;
    },
    async sendNewUserToServer() {
      let name = this.nameValue.split(" ");

      let user = {
        email: this.emailValue,
        password: this.passwordValue,
        accountInfo: {
          firstname: name[0],
          surname: name[1],
        },
      };

      let url = "http://localhost:8080/accounts/register";
      let options = {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(user),
      };

      await fetch(url, options)
        .then((response) => response.json())
        .then((data) => {
          if (data) {
            console.log("showing success modal");
            this.$bvModal.show("success-modal");
            this.nameValue = "";
            this.phoneValue = "";
            this.emailValue = "";
            this.passwordValue = "";
          } else {
            console.log("email already exists, showing error modal");
            this.didSend = false;
            this.$bvModal.show("error-modal");
          }
        })
        .catch((error) => {
          console.log(error);
        });

      //TODO: make it possible to redirect to login page AND show modal
      // await this.$router.push("/login");
    },
    handleRegisterWithFacebook() {
      //Implement facebook compability
    },
    handleRegisterWithGoogle() {
      //Implement google compability
    },
  },
};
</script>

<style scoped>
.registration-box {
  position: relative;
  width: 563px;
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
  justify-content: center;
  margin: 0;
  padding: 0;
  background: white;
}

#facebook-btn {
  margin: 15px 0;
  width: 394px;
  height: 51px;
  background-color: #3b5998;
  border-radius: 6px;
  border: 1px solid #dfdfe8;
  color: white;
  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;
  align-items: center;
}

#facebook-btn p {
  margin-top: 15px;
  margin-right: 80px;
}
#facebook-btn img {
  margin: 0 10px;
}

#facebook-btn:hover {
  background-color: #2f4779;
  cursor: pointer;
}

#google-btn {
  margin: 15px 0 0 0;
  width: 394px;
  height: 51px;
  border-radius: 6px;
  border: 1px solid #dfdfe8;

  display: flex;
  flex-flow: row nowrap;
  justify-content: space-between;
  align-items: center;
}

#google-btn img {
  margin: 0 10px;
}

#google-btn p {
  margin-top: 15px;
  margin-right: 85px;
}

#google-btn:hover {
  background-color: #f1f1f1;
  cursor: pointer;
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

#register-btn {
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
  margin-bottom: 50px;
}

#register-btn:hover {
  background-color: #efb03a;
  cursor: pointer;
}

#register-btn:disabled {
  opacity: 0.3;
}

#close-btn {
  position: absolute;
  top: 5px;
  right: 5px;
}

.close-btn-p {
  font-size: 50px;
}
</style>