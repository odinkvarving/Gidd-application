<template>
  <div id="app">
    <ActivityFeed v-show="isActivityFeedVisible" @activityClicked="onActivityClicked"/>
    <ActivityCard v-show="isActivityCardVisible" :activity="selectedActivity"/>
  </div>
</template>

<script>
import ActivityFeed from './components/activityFeedComponents/ActivityFeed.vue'
import ActivityCard from './components/activityCardComponents/ActivityCard.vue'


export default {
  name: 'App',

  components: {
    ActivityCard,
    ActivityFeed
  },

  data() {
    return {
      selectedActivity: null,
      isActivityFeedVisible: true,
      isActivityCardVisible: false,
    }
  },

  methods: {
    onActivityClicked(activity) {
      this.selectedActivity = activity;
      this.isActivityFeedVisible = false;
      this.isActivityCardVisible = true;
    }
  }
}
</script>

<style scoped>
#app {
  font-family: 'Mulish', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  background-color: #F6F6F6;
}
</style>
