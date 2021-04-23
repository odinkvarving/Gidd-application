<template>
    <div id="card">
        <NavBar/> <!-- Navigation bar -->
        <ActivityCard id="card" :activity="activity"/> <!-- Activity -->
    </div>
</template>
<script>
    import ActivityCard from '../components/ActivityCardComponents/ActivityCard.vue'
    import NavBar from '../components/Nav/NavBar.vue'
    import {userService} from '../services/UserService.js'
    import {weatherService} from '../services/WeatherService.js'
    
    /**
     * Activity is a router, which will display an activity.
     * 
     * @author Scott Rydberg Sonen
     */
    export default {
        name: "Activity",
        components: {
            NavBar,
            ActivityCard
        },
        data() {
            return {
                /**
                 * activity is a variable which represents the clicked activity in the activity feed.
                 */
                activity: {},
                activities: {},
                location: {},
                weather: {},
            }
        },

        async mounted(){
            /**
             * activity is defined here.
             * We use await because we are waiting for findActivity to return an activity based on a GET request in getActivities.
             */
            this.activity = await this.findActivity();
            this.location = await this.getLocation();
            this.weather = await this.getWeather();
        },

        methods: {
            /**
             * getActivities() is an asynchronous function which returns all activities registered in the database.
             * A GET request is sent to the Spring Boot server, and the server returns all activities
             */
            async getActivities() {
                const requestOptions = {
                    method: 'GET',
                    headers: userService.authorizationHeader() //Using an authorization header to get access
                }
                return await fetch("http://localhost:8080/activities", requestOptions)
                .then(response => response.json())
                .then(data => {
                    this.activities = data;
                })
                .catch(error => console.log(error));
            },

            /**
             * findActivity is an asynchronous function which returns clicked activity in activity feed.
             * The way this is done is by finding activity with an ID.
             * The ID is passed from Dashboard to Activity.vue through dynamic routing.
             */
            async findActivity() {
                await this.getActivities();
                let act = this.activities.find((a) => a.id === parseInt(this.$route.params.id)); //this.$route.params.id is the ID which is passed through the routing path
                return act;
            },

            /**
             * getLocation is an asynchronous function which returns location of the activity.
             * getWeather function is utilized, and we find the location by using the weather object.
             */
            async getLocation() {
                return await this.getWeather().name;
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
                let w = await weatherService.getWeather(this.activity.latitude, this.activity.longitude, this.activity.startTime);
                console.log(w.name);
                return w;
                //return await weatherService.getWeather(this.activity.latitude, this.activity.longitude, this.activity.startTime);
            },
        }
    }
</script>
<style>
    #card{
        background-color: #F6F6F6;
        padding: 2% 5%;
        margin-top: 2%;
    }
</style>