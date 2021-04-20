<template>
    <div id="container" v-if="activity != null">
        <Info class="comp" id="info" :activity="activity"/>
        <Map class="comp alt" id="map" :activity="activity"/>
        <Equipment class="comp alt" id="equipment" :activity="activity"/>
        <Chat class="comp chat" id="chat" :activity="activity"/>
    </div>
</template>
<script>
    import Info from './Info.vue'
    import Map from './Map.vue'
    import Equipment from './Equipment.vue'
    import Chat from './Chat.vue'

    export default {
        name: "ActivityCard",
        components: {
            Info,
            Map,
            Equipment,
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

            }
        }
    }
</script>
<style scoped>
    #container{
        display: grid;
        grid-template-areas: 
        "info map"
        "info equipment"
        "chat chat";
        background-color: #F6F6F6;
        height: 100%;
    }
    .comp{
        background-color: white;
        width: 30vw;
        box-shadow: 0px 4px 4px 0px #00000040;
    }
    .alt{
        height: 30vh;
    }
    #info{
        grid-area: info;
        height: 80vh;
    }
    #map{
        grid-area: map;
    }
    #equipment{
        grid-area: equipment;
    }
    #chat{
        grid-area: chat;
    }
</style>
