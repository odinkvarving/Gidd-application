<template>
    <div id="container">
        <Info :activity="activity" id="info"></Info>
        <Map :activity="activity" id="map"></Map>
        <Equipments :activity="activity" id="equipments"></Equipments>
        <Chat :activity="activity" id="chat"></Chat>
    </div>
</template>
<script>
    import Info from './Info.vue'
    import Map from './Map.vue'
    import Equipments from './Equipments.vue'
    import Chat from './Chat.vue'

    export default {
        name: "ActivityCard",
        components: {
            Info,
            Map,
            Equipments,
            Chat
        },

        props: {
            activity: {
                type: Object,
                required: true
            },
        },

        data() {
            return {
                //activity: null,
            }
        }
    }
</script>
<style scoped>
    #container{
        display: grid;
        grid-template-areas: 
        "info map"
        "info equipments"
        "chat chat";
    }
    #info{
        grid-area: info;
    }
    #map{
        grid-area: map;
    }
    #equipments{
        grid-area: equipments;
    }
    #chat{
        grid-area: chat;
    }
</style>
