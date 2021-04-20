<template>
    <div id="container">
        <!--<div id="feed" v-for="a in activities" :key="a.id" >
            <activity :activity="a" @click="handleActivityClicked(activity)"></activity>
            <activity :id="this.getActivityIds[i]"/>
        </div>-->
        <!--<p v-for="test in testing" :key="test.title">{{ test.title }}</p>-->
        <div v-for="a in activities" :key="a.id">
            <Activity :activity="a" @click="handleActivityClicked(a)"/>
        </div>
    </div>
</template>
<script>
    import Activity from './Activity.vue'

    export default {
        name: "ActivityFeed",

        components: {
            Activity,
        },

        data() {
            return {
                testing: [
                    {
                        title: "Hei",
                    },
                    {
                        title: "på",
                    },
                    {
                        title: "deg",
                    }
                ],
                selectedActivity: null,
                //activities: null,
                activities: [
                    {
                        id: 0,
                        name: "Fotball i Dødens dal",
                        ownerImage: "/Systemutvikling 2 med smidig prosjekt/ola.jpg",
                        ownerName: "Ola Nordmann",
                        description: "Fotballllllllll",
                        time: "01.07.2021, kl 18:00",
                        duration: "90 min",
                        type: "Fotball",
                        location: "Dødens dal",
                        weather: "Sol, 20C*",
                        currentParticipants: 10,
                        totalParticipants: 22,
                        equipment: "Fotballsko, t-skjorte, shorts, godt humør"
                    },
                    {
                        id: 1,
                        name: "Gåtur i Bymarka",
                        ownerImage: "/Systemutvikling 2 med smidig prosjekt/kari.jpg",
                        ownerName: "Kari Nordmann",
                        description: "Tuuuuuuur",
                        time: "01.06.2021, kl 12:00",
                        duration: "60 min",
                        type: "Tur",
                        location: "Bymarka",
                        weather: "Sol, 20C*",
                        currentParticipants: 6,
                        totalParticipants: 6,
                        equipment: "Tursko, turjakke, turbukse"
                    },
                    {
                        id: 2,
                        name: "Skitur i Vassdalen",
                        ownerImage: "/Systemutvikling 2 med smidig prosjekt/berit.jpg",
                        ownerName: "Berit Nordmann",
                        description: "Skituuuuur",
                        time: "01.01.2021, kl 10:00",
                        duration: "120 min",
                        type: "Skitur",
                        location: "Jonsvatnet",
                        weather: "Sol, 20C*",
                        currentParticipants: 5,
                        totalParticipants: 10,
                        equipment: "Skiutstyr, ull, kvikk-lunsj, drikke"
                    }
                ],
                sortKey: "",
            }
        },

        methods: {
            async getActivities() {
                await fetch("localhost:8080/activities")
                .then(data => {
                    console.log(data);
                    this.activities = data;
                })
            },

            handleActivityClicked(activity) {
                this.selectedActivity = activity;
                console.log(this.selectedActivity.name);
                this.$emit('activityClicked', this.selectedActivity);
            },
        }
    }
</script>
<style scoped>
    #container{
        width: 70vw;
        display: grid;
        grid-template-columns: repeat(2, 1fr);
    }
</style>