<template>
  <div class="box">
    <div
      v-if="
        chatMessage.accountId === loggedInAccountId ||
        creatorId === loggedInAccountId
      "
    >
      <button @click="deleteMessage()" class="delete">×</button>
    </div>

    <img
      v-if="profilePicUrl !== null"
      v-bind:src="profilePicUrl"
      alt="profilepic"
    />
    <img
      v-else
      src="https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png"
      alt="default pic"
    />
    <router-link v-bind:to="profileLink">
      <h5 class="name">{{ name }}</h5>
    </router-link>
    <p class="message">{{ message }}</p>
    <p class="time">{{ time }}</p>
  </div>
</template>
<script>
/**
 * Chat is a component which represents a single chat element containing account and message.
 * 
 * @author Endré Hadzalic
 */
export default {
  name: "ChatElement",

  props: {
    /**
     * chatMessage is an object passed from Chat which represents the message itself.
     */
    chatMessage: {
      type: Object,
      required: true,
    },
    /**
     * loggedInAccountId is an int passed from Chat which represents ID of current logged in account.
     */
    loggedInAccountId: {
      type: Number,
      required: true,
    },
    /**
     * creatorId is an int passed from Chat which represents ID of message creator.
     */
    creatorId: {
      type: Number,
      required: true,
    },
  },

  data() {
    return {
      /**
       * profilePicUrl represents the URL of account profile picture.
       */
      profilePicUrl: null,
      /**
       * name represents the name of the creator of the message.
       */
      name: "",
      /**
       * time represents the timestamp for a created message.
       */
      time: null,
      /**
       * message represents the message itself.
       */
      message: "",
      /**
       * profileLink represents the link to a creators profile.
       */
      profileLink: "",
    };
  },
  created() {
    this.getUserInfo();
    this.time = this.chatMessage.timeStamp;
    this.message = this.chatMessage.message;
    this.profileLink = "/accounts/" + this.chatMessage.accountId;
  },
  methods: {
    /**
     * getUserInfo is a function which returns info about an account with given ID.
     */
    async getUserInfo() {
      await fetch(
        "http://localhost:8080/accounts/" +
          this.chatMessage.accountId +
          "/info",
        {
          method: "GET",
        }
      )
        .then((response) => response.json())
        .then((data) => {
          this.name = data.firstname + " " + data.surname;
          this.profilePicUrl = data.imageURL;
        })
        .catch((error) => console.error("error deleting message: " + error));
    },

    /**
     * deleteMessage is a function which deletes a message related to a delete button.
     */
    async deleteMessage() {
      if (confirm("Vil du virkelig slette denne kommentaren?")) {
        await fetch("http://localhost:8080/messages/" + this.chatMessage.id, {
          method: "DELETE",
        }).catch((error) => console.error("error deleting message: " + error));
      }
    },
  },
};
</script>
<style scoped>
img {
  vertical-align: middle;
  object-fit: scale-down;
  background-color: lightgray;
  width: 75px;
  height: 75px;
  border-radius: 50%;
  margin: 10px;
}

.box {
  margin-bottom: 10px;
  background-color: white;
  padding-left: 15px;
  width: 92%;
  border: solid lightgray 2px;
}
.time {
  opacity: 0.5;
  padding-left: 15px;
  font-size: 12px;
}
.message {
  border-bottom: lightgray 2px solid;
  border-top: 2px lightgray solid;
  padding-bottom: 15px;
  padding-left: 15px;
  margin-right: 15px;
  padding-top: 10px;
  white-space: pre-line;
  line-height: 1;
}

.delete {
  float: right;
  background-color: white;
  color: lightgray;
  border-radius: 12px;
  padding-top: 5px;
  border: none;
  padding-right: 15px;
  font-weight: bold;
  font-size: 25px;
}

.name {
  color: black;
}

.delete:hover {
  color: red;
}
</style>