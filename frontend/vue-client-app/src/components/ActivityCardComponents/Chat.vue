<template>
  <div id="chat">
    <h4>Chat:</h4>
    <div v-for="message in messages" :key="message.timeStamp">
      <ChatElement :chatMessage="message" />
    </div>
    <input
      type="text"
      class="input"
      id="message-field"
      placeholder="Melding"
      v-model="message"
      v-on:keyup.enter="sendMessage"
    />
    <button @click="sendMessage">✉</button>
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
    };
  },
  created() {
    this.startTimer();
    this.pageRoute = this.$route.name;
  },
  methods: {
    startTimer() {
      this.timerId = setInterval(() => this.loadMessages(), 1000);
    },
    stillOnPage() {
      return this.pageRoute === this.$route.name;
    },
    loadMessages() {
      console.log("Refreshing chat-box");
      if (this.stillOnPage()) {
        fetch(
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
      console.log("sending message: " + this.message);
      let userId = await userService.getAccountByEmail().then((data) => {
        return data.id;
      });
      console.log("account id: " + userId);
      const data = {
        accountId: userId,
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
    },
  },
};
</script>
<style>
#chat {
  height: 300px;
}
.input {
  padding: 0 10px;
  border: none;
  outline: none;
}
</style>
