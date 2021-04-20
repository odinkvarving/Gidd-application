<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>

export default {
  name: 'App',

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

@import url('https://fonts.googleapis.com/css2?family=Mulish&display=swap');

<style scoped>
  #app {
    font-family: 'Mulish', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin: 0;
    padding: 0;
    background-color: #F6F6F6;
  }
</style>
