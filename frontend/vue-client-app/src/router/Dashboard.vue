<template>
    <div id="container">
        <ActivityFeed @activityClicked="onSelectedActivity()" id="activityFeed"/>
    </div>
</template>
<script>
    import ActivityFeed from "../components/ActivityFeedComponents/ActivityFeed.vue"

    export default {
        name: "Dashboard",
        components: {
            ActivityFeed
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