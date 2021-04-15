<template>
  <!--<img alt="Vue logo" src="./assets/logo.png">
  <HelloWorld msg="Welcome to Your Vue.js App"/>-->
  <!--<ActivityCard></ActivityCard>-->
  <ActivityFeed></ActivityFeed>
</template>

<script>
import Index from './router/index.vue'


export default {
  name: 'App',
  components: {
    Index
  }
}
</script>

<style>

@import url('https://fonts.googleapis.com/css2?family=Mulish&display=swap');

#app {
  font-family: 'Mulish', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin: 0;
  padding: 0;
}
</style>
