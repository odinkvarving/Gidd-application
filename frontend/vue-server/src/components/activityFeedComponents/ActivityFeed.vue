<template>
    <div id="container">
        <Activity v-for="activity in activities" :key="activity.id" @click="handleActivityClicked(activity)"></Activity>
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
                selectedActivity: null,
                activites: {getActivities()},
                sortKey: "",
            }
        },

        methods: {
            async getActivities() {
                await fetch("localhost:8080/activities")
                .then(data => {
                    console.log(data);
                    this.activites = data;
                })
            },

            handleActivityClicked() {
                this.selectedActivity = activity;
            }
        }
    }
</script>