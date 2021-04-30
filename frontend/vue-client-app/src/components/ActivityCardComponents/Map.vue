<template>
    <div id="container">
        <GmapMap
            :center="{lat:latitude, lng:longitude}"
            :zoom="11"
            map-type-id="roadmap"
            style="width: 100%; height: 100%"
            >
        <GmapMarker
            :position="{lat:latitude, lng:longitude}"
            @click="center={lat:latitude, lng:longitude}"
        />
        </GmapMap>
    </div>
</template>
<script>
    /**
     * Map is a component which displays the map of an activity's location.
     * 
     * @author Magnus Bredeli
     */
    export default {
        name: "Map",
        props: {
            /**
             * latitude is an int passed from ActivityCard which represents latitude of current activity.
             */
            latitude: Number,
            /**
             * longitude is an int passed from ActivityCard which represents longitude of current activity.
             */
            longitude: Number
        }
    }
</script>
<style scoped>
    #container{
        width: 40vh;
        height: 40vh;
        position: relative;
    }
</style>