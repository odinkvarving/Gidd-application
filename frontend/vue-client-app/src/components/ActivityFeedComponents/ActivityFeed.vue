<template>
    <div id="container">
        <div id="feed">
            <div v-for="a in activities" :key="a.id" @click="handleActivityClicked(a)">
                <Activity :activity="a"/>
            </div>
            <!-- <div v-for="a in activities" :key="a.id" @click="handleActivityClicked(a)">
                <Activity :activity="a"/>
            </div> -->
        </div>
        <div id="calendar">
            
        </div>
    </div>
</template>
<script>
    import Activity from './Activity.vue'
    import {weatherService} from '../../services/WeatherService.js'    

    export default {
        name: "ActivityFeed",

        components: {
            Activity,
        },

        data() {
            return {
                //WeatherService: require('../../services/WeatherService.js'),
                selectedActivity: null,
                activities: this.getActivities(),
                /*activities: [
                    {
                        id: 0,
                        name: "Fotball i Dødens dal",
                        ownerImage: "ola.jpg",
                        ownerName: "Ola Nordmann",
                        description: "Fotballllllllll",
                        //time: "01.07.2021, kl 18:00",
                        time: "2021-05-01T18:00:00",
                        duration: "90 min",
                        type: "Fotball",
                        location: "Dødens dal",
                        latitude: 63.419213,
                        longitude: 10.406178,
                        //weather: "Sol, 20 C°",
                        //weather: this.WeatherService.getWeather(this.latitude, this.longitude, this.time),
                        //weather: WeatherService.getWeather(this.latitude, this.longitude, this.time),
                        weather: this.getWeather(),
                        currentParticipants: 10,
                        totalParticipants: 22,
                        equipment: [{
                            equipmentType: "Fotballsko",
                        },
                        {
                            equipmentType: "T-skjorte",
                        },
                        {
                            equipmentType: "Shorts",
                        },
                        {
                            equipmentType: "Drikkeflaske",
                        },
                        {
                            equipmentType: "Godt humør",
                        }],
                    },
                    {
                        id: 1,
                        name: "Gåtur i Bymarka",
                        ownerImage: "kari.jpg",
                        ownerName: "Kari Nordmann",
                        description: "Tuuuuuuur",
                        //time: "01.06.2021, kl 12:00",
                        time: "2021-06-01T12:00:00",
                        duration: "60 min",
                        type: "Tur",
                        location: "Bymarka",
                        latitude: 63.417873,
                        longitude: 10.240582,
                        //weather: "Sol, 20 C°",
                        //weather: this.WeatherService.getWeather(this.latitude, this.longitude, this.time),
                        //weather: WeatherService.getWeather(this.latitude, this.longitude, this.time),
                        weather: this.getWeather(),
                        currentParticipants: 6,
                        totalParticipants: 6,
                        equipment: [{
                            equipmentType: "Tursko",
                        },
                        {
                            equipmentType: "Turjakke",
                        },
                        {
                            equipmentType: "Turbukse",
                        }],
                    },
                    {
                        id: 2,
                        name: "Skitur i Vassdalen",
                        ownerImage: "berit.jpg",
                        ownerName: "Berit Nordmann",
                        description: "Skituuuuur",
                        //time: "01.01.2021, kl 10:00",
                        time: "2021-01-01T10:00:00",
                        duration: "120 min",
                        type: "Skitur",
                        location: "Jonsvatnet",
                        latitude: 63.398937,
                        longitude: 10.577261,
                        //weather: "Sol, 20 C°",
                        //weather: this.WeatherService.getWeather(this.latitude, this.longitude, this.time),
                        //weather: WeatherService.getWeather(this.latitude, this.longitude, this.time),
                        weather: this.getWeather(),
                        currentParticipants: 5,
                        totalParticipants: 10,
                        equipment: [{
                            equipmentType: "Skiutstyr",
                        },
                        {
                            equipmentType: "Ull",
                        },
                        {
                            equipmentType: "Kvikk-Lunsj",
                        },
                        {
                            equipmentType: "Drikke",
                        }],
                    }
                ],*/
                sortKey: "",
            }
        },

        methods: {
            async getActivities() {
                return await fetch("http://localhost:8080/activities")
                .then(data => {
                    console.log(data);
                    //this.activities = data;
                })
            },

            async getWeather() {
                return await weatherService.getWeather(this.latitude, this.longitude, this.time);
            },

            handleActivityClicked(activity) {
                this.selectedActivity = activity;
                console.log(this.selectedActivity.name);
                this.$emit('activityClicked', this.selectedActivity);
                this.$router.push({ name: 'Activity', params: { id: activity.id }});
            },
        }
    }
</script>
<style>
    #container{
        background-color: #F6F6F6;
    }
    #feed{
        border: 1px solid black;
        margin-left: 4vw;
        width: 70vw;
        display: flex;
        flex-flow: row wrap;
        align-items: center;
        justify-content: center;
        background-color: #F6F6F6;
        padding: 200px 0;
        /*display: flex;
        flex-direction: column;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;*/
    }

</style>
