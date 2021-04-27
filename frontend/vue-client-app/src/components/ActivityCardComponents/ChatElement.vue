<template>
  <div class="box">
    <img v-bind:src="profilePicUrl" v-bind:alt="name" />
    <h2 class="name">{{ name }}</h2>
    <p></p>
    <p>{{ message }}</p>
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
</style>