<template>
  <div>
    <NavBar />
    <router-view />
  </div>
</template>

<script>
import NavBar from "./components/Nav/NavBar.vue";

export default {
  name: "App",
  components: {
    NavBar,
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Mulish:wght@300;400&display=swap");

#app {
  font-family: "Mulish", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
  padding: 0;
}

.body {
  font-family: "Mulish";
}

header {
  width: 100wv;
  border: 1px solid black;
  padding: 5px;
}
</style>
