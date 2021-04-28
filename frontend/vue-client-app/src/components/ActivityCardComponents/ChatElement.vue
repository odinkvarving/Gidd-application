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
    <h5 class="name">{{ name }}</h5>
    <p class="message">{{ message }}</p>
    <p class="time">{{ time }}</p>
  </div>
</template>
<script>
export default {
  name: "ChatElement",

  props: {
    chatMessage: {
      type: Object,
      required: true,
    },
    loggedInAccountId: {
      type: Number,
      required: true,
    },
    creatorId: {
      type: Number,
      required: true,
    },
  },

  data() {
    return {
      profilePicUrl: null,

      name: "",

      time: null,

      message: "",
    };
  },
  created() {
    this.getUserInfo();
    this.time = this.chatMessage.timeStamp;
    this.message = this.chatMessage.message;
  },
  methods: {
    sendToProfile() {
      this.$router.push({ route: "/accounts/" + this.chatMessage.accountId });
    },
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
.delete:hover {
  color: red;
}
</style>