<template>
    <div id="card">
        <NavBar/>
        <ActivityCard id="card" :activity="activity"/>
    </div>
</template>
<script>
    import ActivityCard from '../components/ActivityCardComponents/ActivityCard.vue'
    import NavBar from '../components/Nav/NavBar.vue'
    import WeatherService from '../services/WeatherService.js'
    
    export default {
        name: "Activity",
        components: {
            NavBar,
            ActivityCard
        },
        data() {
            return {
                test: [1,2,3],
                activity: this.findActivity(),
                /*weather: {
                    name: WeatherService.getName,
                    temp: WeatherService.getTemp,
                    description: WeatherService.getDescription
                }*/
            }
        },

        methods: {
            getActivities() { //async when we receive activities from db
                let activities = [ //FOR TEST PURPOSES
                    {
                        id: 0,
                        name: "Fotball i Dødens dal",
                        ownerImage: "ola.jpg",
                        ownerName: "Ola Nordmann",
                        description: "Fotballllllllll",
                        //time: "01.07.2021, kl 18:00",
                        time: "2021-07-01T18:00:00",
                        duration: "90 min",
                        type: "Fotball",
                        location: "Dødens dal",
                        latitude: 63.419213,
                        longitude: 10.406178,
                        //weather: "Sol, 20 C°",
                        weather: WeatherService.getWeather(this.latitude, this.longitude, this.time),
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
                        weather: WeatherService.getWeather(this.latitude, this.longitude, this.time),
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
                        // weather: "Sol, 20 C°",
                        weather: WeatherService.getWeather(this.latitude, this.longitude, this.time),
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
                ];
                activities.forEach(a => console.log(a.name));
                return activities;
                /*return await fetch("localhost:8080/activities")
                .then(data => {
                    console.log(data);
                    this.activities = data;
                })*/
            },

            findActivity() { //async when we will use getActivities
                console.log("test");
                //let activities = await Promise.all(this.getActivities());
                let activities = this.getActivities();
                let act = activities.find((a) => a.id === this.$route.params.id);
                console.log(act);
                return act;
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