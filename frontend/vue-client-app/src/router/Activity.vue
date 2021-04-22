<template>
    <div id="card">
        <NavBar/> <!-- Navigation bar -->
        <ActivityCard id="card" :activity="activity"/> <!-- Activity -->
    </div>
</template>
<script>
    import ActivityCard from '../components/ActivityCardComponents/ActivityCard.vue'
    import NavBar from '../components/Nav/NavBar.vue'
    import {userService} from '../services/UserService'
    
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
                activity: this.findActivity(),
            }
        },

        async mounted(){
            this.activity = await this.findActivity()
        },

        methods: {
            /**
             * getActivities() is an asynchronous function which returns all activities registered in the database.
             * A GET request is sent to the Spring Boot server, and the server returns all activities
             */
            async getActivities() {
                const requestOptions = {
                    method: 'GET',
                    Headers: userService.authorizationHeader() //Using an authorization header to get access
                }
                return await fetch("http://localhost:8080/activities", requestOptions)
                .then(response => response.json())
                .then(data => {
                    console.log(data);
                })
                .catch(error => console.log(error));
            },

            /**
             * findActivity is an asynchronous function which returns clicked activity in activity feed.
             * The way this is done is by finding activity with an ID.
             * The ID is passed from Dashboard to Activity.vue through dynamic routing.
             */
            async findActivity() {
                let activities = await this.getActivities();
                let act = activities.find((a) => a.id === parseInt(this.$route.params.id)); //this.$route.params.id is the ID which is passed through the routing path
                return act;

                /*console.log("test");
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
                return act;*/
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