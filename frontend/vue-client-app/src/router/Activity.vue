<template>
  <div id="card">
    <NavBar id="navbar"/>
    <ActivityCard
      id="card"
      :activity="activity"
      :weather="weather"
      v-if="isDataReady"
    />
  </div>
</template>
<script>
/**
 * NavBar and ActivityCard are components which are used for displaying a navbar and activity feed.
 * userService and weatherService are imported for efficient use of different functions which will be useful.
 */
import NavBar from "../components/Nav/NavBar.vue"
import ActivityCard from "../components/ActivityCardComponents/ActivityCard.vue";
import { userService } from "../services/UserService.js";
import { weatherService } from "../services/WeatherService.js";

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
       * activity is an object which represents the clicked activity in the activity feed.
       */
      activity: {},
      /**
       * weather is an object containing weather at the activity's location if startdate is within 7 days
       */
      weather: {},
      /**
       * isDataReady is a flag which tells us when fetched data is ready for utilization.
       */
      isDataReady: false,
    };
  },

  /**
   * mounted is a function which runs before Activity renders.
   * activity and weather are defined in mounted function
   */
  async mounted() {
    await this.getActivity();
    await this.getWeather();
    this.isDataReady = true;
  },

  methods: {
    /**
     * getActivity is a function which receives an activity when requesting an activity with a given ID.
     */
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
     * getWeather is a function which returns weather at the activity location if startTime is within 7 days of current time.
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
  #card{
    background-color: #F6F6F6;
  }
</style>
