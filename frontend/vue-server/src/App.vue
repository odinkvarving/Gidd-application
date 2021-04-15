<template>
  <!--<img alt="Vue logo" src="./assets/logo.png">
  <HelloWorld msg="Welcome to Your Vue.js App"/>-->
  <!--<ActivityCard></ActivityCard>-->
  <ActivityFeed></ActivityFeed>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'
import ActivityCard from './components/activityCardComponents/ActivityCard.vue'
import ActivityFeed from './components/activityFeedComponents/ActivityFeed.vue'

export default {
  name: 'App',
  components: {
    HelloWorld,
    ActivityCard,
    ActivityFeed
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
