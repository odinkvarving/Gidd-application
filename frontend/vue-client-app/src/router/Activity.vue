<template>
    <div id="card">
        <ActivityCard id="card" :activity="activity"/>
    </div>
</template>
<script>
    import ActivityCard from '../components/ActivityCardComponents/ActivityCard.vue'
import { userService } from '../services/UserService'
    import {weatherService} from '../services/WeatherService.js'
    
    export default {
        name: "Activity",
        components: {
            ActivityCard
        },
        data() {
            return {
                test: [1,2,3],
                activity: {},
                activities: {}
                /*weather: {
                    name: WeatherService.getName,
                    temp: WeatherService.getTemp,
                    description: WeatherService.getDescription
                }*/
            }
        },
        async mounted(){
            await this.findActivity();
        },
        methods: {
            async getActivities() { //async when we receive activities from db
                const requestOptions = {
                    method: 'GET',
                    headers: userService.authorizationHeader()
                }
                
                return await fetch("http://localhost:8080/activities/", requestOptions)
                    .then(response => response.json())
                    .then(data => {
                        this.activities = data;
                    })
                    .catch(error => console.log(error));
                
            },

            async getWeather() {
                return await weatherService.getWeather(this.latitude, this.longitude, this.time);
            },

            async findActivity() { //async when we will use getActivities
                console.log("test");
                //await Promise.all(this.getActivities());
                await this.getActivities();
                console.log(this.activities);
                console.log(this.$route.params.id);
                let act;
                for(let i = 0; i < this.activities.length; i ++){
                    console.log(this.activities[i].id);
                    if(this.activities[i].id === parseInt(this.$route.params.id)){
                        act = this.activities[i];
                    }
                }
                console.log(act);
                this.activity = act;
            }
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