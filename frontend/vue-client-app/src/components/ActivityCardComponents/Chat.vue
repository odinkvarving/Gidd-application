<template>
  <div id="chat">
    <h4>Kommentarfelt:</h4>
    <div v-for="message in messages" :key="message.id">
      <ChatElement
        :chatMessage="message"
        :loggedInAccountId="userId"
        :creatorId="activity.creator.id"
      />
    </div>
    <div class="chat-input">
      <textarea
        type="text"
        class="input"
        id="message-field"
        placeholder="Legg til en kommentar"
        rows="2"
        v-model="message"
      />
      <button @click="sendMessage">✔</button>
    </div>
  </div>
</template>
<script>
import { userService } from "../../services/UserService";
import ChatElement from "./ChatElement.vue";

export default {
  props: {
    activity: {
      type: Object,
      required: true,
    },
  },
  name: "Chat",
  components: {
    ChatElement,
  },
  data() {
    return {
      messages: {},
      message: "",
      timerId: null,
      pageRoute: "",
      userId: null,
    };
  },
  created() {
    this.pageRoute = this.$route.name;
    this.loadUserId();
    this.startTimer();
  },
  methods: {
    startTimer() {
      this.timerId = setInterval(() => this.loadMessages(), 1000);
    },
    async loadUserId() {
      await userService.getAccountByEmail().then((data) => {
        this.userId = data.id;
      });
      console.log("loaded user: " + this.userId);
    },
    stillOnPage() {
      return this.pageRoute === this.$route.name;
    },
    async loadMessages() {
      console.log("Refreshing chat-box");
      if (this.stillOnPage()) {
        await fetch(
          "http://localhost:8080/activities/" + this.activity.id + "/messages"
        )
          .then((response) => response.json())
          .then((data) => (this.messages = data))
          .catch((error) =>
            console.error("error while trying to fetch chat messages: ", error)
          );
      } else {
        console.log("Stopped refreshing chat box");
        clearInterval(this.timerId);
      }
    },
    async sendMessage() {
      if (userService.isLoggedIn()) {
        console.log("sending message: " + this.message);
        console.log("account id: " + this.userId);
        const data = {
          accountId: this.userId,
          message: this.message,
        };
        this.message = "";
        await fetch(
          "http://localhost:8080/activities/" + this.activity.id + "/messages",
          {
            headers: {
              "Content-Type": "application/json",
            },
            method: "POST",
            body: JSON.stringify(data),
          }
        ).catch((error) =>
          console.error("error during posting of message: ", error)
        );
      } else {
        console.error("User must be logged in to post message");
      }
    },
  },
};
</script>
<style>
.input {
  padding: 0 10px;
  border: solid 2px #eca82b;
  outline: none;
}
#message-field {
  resize: none;
}
.chat-input {
  display: grid;
  grid-template-columns: 75% auto;
  margin-right: 8%;
  display: fixed;
}
</style>
