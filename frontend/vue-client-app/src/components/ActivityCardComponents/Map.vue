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
    export default {
        name: "Map",
        data(){
            return{

            }
        },
        props: {
            latitude: String,
            longitude: String
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