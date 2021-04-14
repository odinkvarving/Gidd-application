<template>
  <!--<img alt="Vue logo" src="./assets/logo.png">
  <HelloWorld msg="Welcome to Your Vue.js App"/>-->
  <ActivityCard></ActivityCard>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import ActivityCard from './components/activityCardComponents/ActivityCard.vue'

export default {
  name: 'App',
  components: {
    HelloWorld,
    ActivityCard
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
