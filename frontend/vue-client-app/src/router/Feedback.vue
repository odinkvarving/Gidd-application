<template>
  <div class="container">
    <h1 style="padding-bottom: 30px">Gi oss din tilbakemelding</h1>
    <div class="textarea-container">
      <b-form-textarea
        id="textarea"
        v-model="text"
        placeholder="Gi oss din tilbakemelding..."
        rows="3"
        max-rows="6"
        style="height: 60vh; min-height: 350px"
      ></b-form-textarea>
    </div>
    <div class="rating-container">
      <b-form-rating
        class="rating"
        v-model="rating"
        color="#FFBD3E"
        inline
        value="4"
        :no-border="isRated"
        size="lg"
      ></b-form-rating>
    </div>
    <div class="button-container">
      <b-button class="submit" @click="onClickSendButton" :disabled="disableButton">Send inn</b-button>
    </div>
      <p v-show="disableButton" style="color: green;">Takk for din tilbakemelding</p>
  </div>
</template>

<script>
import { userService } from "../services/UserService";
export default {
  name: "Feedback",
  data() {
    return {
      rating: 0,
      text: "",
      disableButton: false,
      isRated: true
    };
  },
  methods: {
    onClickSendButton() {
      if (this.rating === 0) {
        this.isRated = false;
        return
      }
      this.isRated = true;
      this.disableButton = true;
      this.send();
    },

    send() {
      let feedbackData = {
        sender: userService.getAccountDetails().sub,
        text: this.text,
        rating: this.rating,
      };

      const requestOptions = {
        method: "POST",
        headers: {
          'Content-Type': 'application/json',
          'Authorization': userService.getTokenString(),
        },
        body: JSON.stringify( feedbackData ),
      }

      return fetch("http://localhost:8080/feedback", requestOptions)
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.log(error));
    },
  },
};
</script>

<style>
.custom-control:focus {
  outline: none !important;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px 0;
}

.container > div {
  margin: 6px;
}

.rating-container {
  display: flex;
}

.rating.form-control {
  border-color: red;
}

#textarea.form-control {
  width: 40vw;
  min-width: 300px;
}
</style>
