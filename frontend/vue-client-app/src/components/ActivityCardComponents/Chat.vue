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
/**
 * userService and ChatElement is imported.
 */
import { userService } from "../../services/UserService";
import ChatElement from "./ChatElement.vue";

/**
 * Chat is a component which represents the chat box in ActivityCard.
 * 
 * @author Endré Hadzalic
 */
export default {
  name: "Chat",

  components: {
    ChatElement,
  },

  props: {
    /**
     * activity is an object passed from ActivityCard which contains the current activity.
     */
    activity: {
      type: Object,
      required: true,
    },
  },

  data() {
    return {
      /**
       * messages is a list containing all messages from the database.
       */
      messages: {},
      /**
       * message represents a new message which will be added to the database and message list.
       */
      message: "",
      /**
       * timerId stores a pointer to the timer.
       */
      timerId: null,
      /**
       * pageRoute represents the current route path.
       */
      pageRoute: "",
      /**
       * userId represents the ID of a user.
       */
      userId: null,
    };
  },

  created() {
    this.pageRoute = this.$route.name;
    this.loadUserId();
    this.startTimer();
  },
  
  methods: {
    /**
     * startTimer is a function which defines timer ID as a pointer to the update interval.
     */
    startTimer() {
      this.timerId = setInterval(() => this.loadMessages(), 1000);
    },

    /**
     * loadUserId is a function which defines userId 
     */
    async loadUserId() {
      await userService.getAccountByEmail().then((data) => {
        this.userId = data.id;
      });
    },

    /**
     * stillOnPage is a function which checks if pageRoute equals the current route path.
     */
    stillOnPage() {
      return this.pageRoute === this.$route.name;
    },

    /**
     * loadMessages is a function which loads all messages with relations to the current activity.
     */
    async loadMessages() {
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
        clearInterval(this.timerId);
      }
    },

    /**
     * sendMessage is a function which runs when send button is clicked.
     * If message is larger than 0 chars and the account is logged in,
     *  the message input is read and sent to backend by POST request.
     */
    async sendMessage() {
      if (this.message.length > 0) {
        if (userService.isLoggedIn()) {
          const data = {
            accountId: this.userId,
            message: this.message,
          };
          this.message = "";
          await fetch(
            "http://localhost:8080/activities/" +
              this.activity.id +
              "/messages",
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

#chat {
  width: 80vw;
  margin-bottom: 100px;
}
</style>
