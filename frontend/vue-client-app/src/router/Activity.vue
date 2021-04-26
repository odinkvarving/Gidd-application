<template>
  <div id="card">
    <ActivityCard
      id="card"
      :activity="activity"
      :weather="weather"
      :isDataReady="isDataReady"
      v-if="isDataReady"
    />
    <!-- Activity  v-if="activity && location && weather && isDataReady" -->
  </div>
</template>
<script>
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
     * We use await because we are waiting for findActivity to return an activity based on a GET request in getActivities.
     */
    await this.getActivity();
    //this.activity = await this.findActivity();
    //await this.getWeather();
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
     * getActivities() is an asynchronous function which returns all activities registered in the database.
     * A GET request is sent to the Spring Boot server, and the server returns all activities
     */
    async getActivities() {
      const requestOptions = {
        method: "GET",
        headers: userService.authorizationHeader(), //Using an authorization header to get access
      };
      await fetch("http://localhost:8080/activities", requestOptions)
        .then((response) => response.json())
        .then((data) => {
          this.activities = data;
        })
        .catch((error) => console.log(error));
    },

    /**
     * findActivity is an asynchronous function which returns clicked activity in activity feed.
     * The way this is done is by finding activity with an ID.
     * The ID is passed from Dashboard to Activity.vue through dynamic routing.
     */
    async findActivity() {
      await this.getActivities();
      let act = this.activities.find(
        (a) => a.id === parseInt(this.$route.params.id)
      ); //this.$route.params.id is the ID which is passed through the routing path
      return act;
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
      //console.log(w.name);
      //return w;
      //return await weatherService.getWeather(this.activity.latitude, this.activity.longitude, this.activity.startTime);
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
