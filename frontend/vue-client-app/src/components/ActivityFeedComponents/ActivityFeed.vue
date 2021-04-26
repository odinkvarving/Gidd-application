<template>
    <div id="container">
        <div id="feed">
            <div v-for="a in activities" :key="a.id" @click="handleActivityClicked(a)">
                <Activity :activity="a" v-on:refresh-list="refreshList"/>
            </div>
        </div>
        <div id="calendar-mini">
            <p class="header">Kommende aktiviteter</p>
            <div class="horizontal-line"/>
            <div id="coming-activities">
                <!-- TODO: refresh joined activity list when activity is joined or removed -->
                <div v-for="a in joinedActivities" :key="a.id" style="width: 100%;">
                    <div class="joined-activity-container">
                        <div class="circle" />
                        <p class="title">{{ a.title }}</p>
                        <div class="time">
                            <p class="date">04.01</p>
                            <p class="clock">16:00</p>
                        </div>
                    </div>
                </div>
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
                //WeatherService: require('../../services/WeatherService.js'),
                selectedActivity: null,
                activities: {},
                joinedActivities: {},
                sortKey: "",
            }
        },
        mounted(){
            this.getActivities();
            this.getJoinedActivities();
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
            async getJoinedActivities() {

                let accountId;
                await userService.getAccountByEmail().then(data => accountId = data.id);
                
                let url = `http://localhost:8080/accounts/${accountId}/activities/`;

                const requestOptions ={
                    method: 'GET',
                    headers: userService.authorizationHeader()
                }

                // Get all registered activites from database
                fetch(url, requestOptions)
                    .then(response => response.json())
                    .then(data => {
                        console.log(data);
                        this.joinedActivities = data;
                    })
                    .catch(error => console.log(error))
            },

            handleActivityClicked(activity) {
                this.selectedActivity = activity;
                console.log(this.selectedActivity.name);
                this.$emit('activityClicked', this.selectedActivity);
                this.$router.push({ name: 'Activity', params: { id: activity.id }});
            },
            getDayAndMonth(){
                // Hente ut dag og månede fra tidspunkt start
            },
            getClockTime(){
                // Hente ut klokkeslett ^
            },
            refreshList(activityId){
                let isInList;
                let index;

                for(let i = 0; i < this.joinedActivities.length; i ++){
                    if(this.joinedActivities[i].id === activityId){
                        isInList = true;
                        index = i;
                    }
                }

                if(isInList){
                    this.joinedActivities.splice(index, 1);
                }else{
                    for(let i = 0; i < this.activities.length; i ++){
                        if(this.activities[i].id === activityId){
                            this.joinedActivities.push(this.activities[i]);
                        }
                    }
                }

            }
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

    #calendar-mini .horizontal-line {
        width: 85%;
        opacity: 40%;
        height: 1px;
        background-color: black;
    }

    .joined-activity-container {
        width: 300px;
        display: flex;
        align-items: center;
        justify-content: space-around;
        margin-top: 20px;
    }
    
    .joined-activity-container p {
        margin: 0;
    }

    .joined-activity-container .circle{
        width: 10px;
        height: 10px;
        background-color: #FFBD3E;
        border-radius: 100%;
    }

    .joined-activity-container .title{
        width: 210px;
        text-align: center;
    }

    .joined-activity-container .time {
        width: 80px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

    }

    .joined-activity-container .time .date {
        font-size: 20px;
        margin: 0;
    }

    .joined-activity-container .time .clock {
        font-size: 14px;
        margin: 0;
    }
</style>
