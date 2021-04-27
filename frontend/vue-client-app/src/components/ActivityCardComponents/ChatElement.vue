<template>
  <div class="box">
    <img v-bind:src="profilePicUrl" alt="profilepic" />
    <h5 class="name">{{ name }}</h5>
    <p class="time">{{ time }}</p>
    <p class="message">{{ message }}</p>
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
  },

  data() {
    return {
      profilePicUrl: "",

      name: "",

      time: null,

      message: "",
    };
  },
  mounted() {
    this.getUserInfo();
    this.time = this.chatMessage.timeStamp;
    this.message = this.chatMessage.message;
  },
  methods: {
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
        .catch((error) => console.error("error: ", error));
    },
  },
};
</script>
<style scoped>
.box {
  background-color: white;
  padding-left: 15px;
}
.time {
  opacity: 0.5;
  padding-left: 15px;
}
.message {
  padding-bottom: 15px;
  padding-left: 15px;
}
</style>