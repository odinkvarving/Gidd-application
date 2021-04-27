<template>
  <div id="card">
    <NavBar />
    <ActivityCard
      id="card"
      :activity="activity"
      :weather="weather"
      v-if="isDataReady"
    />
  </div>
</template>
<script>
import ActivityCard from "../components/ActivityCardComponents/ActivityCard.vue";
import { userService } from "../services/UserService.js";
import { weatherService } from "../services/WeatherService.js";
import NavBar from "../components/Nav/NavBar.vue"

/**
 * Activity is a router, which will display an activity.
 *
 * @author Scott Rydberg Sonen
 */
export default {
  name: "Activity",
  components: {
    ActivityCard,
    NavBar
  },
  data() {
    return {
      /**
       * activity is a variable which represents the clicked activity in the activity feed.
       */
      activity: {},
      activities: {},
      weather: {},
      isDataReady: false,
    };
  },

  async mounted() {
    /**
     * activity is defined here.
     * We use await because we are waiting for getActivity to return an activity based on ID in path.
     */
    await this.getActivity();
    await this.getWeather();
    this.isDataReady = true;
  },

  methods: {
    async getActivity() {
      const activityId = parseInt(this.$route.params.id);
      const requestOptions = {
        method: "GET",
        headers: userService.authorizationHeader(),
      };
      await fetch(
        `http://localhost:8080/activities/${activityId}/`,
        requestOptions
      )
        .then((response) => response.json())
        .then((data) => (this.activity = data))
        .catch((error) => console.log(error));
    },

    /**
     * getWeather is an asynchronous function which returns weather of the activity.
     * The weather object is fetched in services/WeatherService.
     * To find the weather, WeatherService requires latitude, longitude and startTime of activity.
     */
    async getWeather() {
      console.log(this.activity);
      console.log("LATITUDE: " + this.activity.latitude);
      console.log("LONGITUDE: " + this.activity.longitude);
      console.log("STARTTIME: " + this.activity.startTime);
      this.weather = await weatherService.getWeather(
        this.activity.latitude,
        this.activity.longitude,
        this.activity.startTime
      );
    },
  },
};
</script>
<style>
#card {
  background-color: #f6f6f6;
  padding: 2% 5%;
  margin-top: 2%;
}
</style>
