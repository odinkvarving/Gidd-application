<template>
    <div id="container">
        <div id="feed">
            <div v-for="a in activities" :key="a.id" @click="handleActivityClicked(a)">
                <Activity :activity="a"/>
            </div>
        </div>
        <div id="calendar-mini">
            <p class="header">Kommende aktiviteter</p>
            <div class="horizontal-line"/>
            <div id="coming-activities">
                <!-- list med brukerens aktiviteter -->
            </div>
        </div>
    </div>
</template>
<script>
    import Activity from './Activity.vue'
    import { userService } from "../../services/UserService.js"

    export default {
        name: "ActivityFeed",

        components: {
            Activity,
        },

        data() {
            return {
                selectedActivity: null,
                activities: {},
                sortKey: "",
            }
        },
        mounted(){
            this.getActivities();
            
        },
        methods: {
            getActivities() {
                const requestOptions ={
                    method: 'GET',
                    headers: userService.authorizationHeader()
                }

                // Get all registered activites from database
                fetch("http://localhost:8080/activities/", requestOptions)
                    .then(response => response.json())
                    .then(data => {
                        this.activities = data;
                        console.log(data[0]);
                    })
                    .catch(error => console.log(error))
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
        position: relative;
        font-family: "Mulish";

    }
    #feed{
        margin-left: 0;
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
    #calendar-mini {
        position: fixed;
        width: 340px;
        height: 625px;
        top: 200px;
        right: 100px;
        background: white;
        display: flex;
        flex-flow: column nowrap;
        align-items: center;
    }

    #calendar-mini .header{
        font-size: 25px;
        opacity: 80%;
        margin-top: 15px;
    }

</style>
