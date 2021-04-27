<template>
    <div id="container">
        <NavBar />
        <ActivityFeed @activityClicked="onSelectedActivity()" id="activityFeed"/>
    </div>
</template>
<script>
    import ActivityFeed from "../components/ActivityFeedComponents/ActivityFeed.vue"
    import NavBar from "../components/Nav/NavBar.vue"

    export default {
        name: "Dashboard",
        components: {
            ActivityFeed,
            NavBar
        },

        data() {
            return {
                selectedActivity: null,
            }
        },

        methods: {
            onSelectedActivity() {
                
            }
        }
    }
</script>