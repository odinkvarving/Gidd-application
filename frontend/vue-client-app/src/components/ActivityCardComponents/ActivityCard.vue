<template>
    <div id="container" v-if="activity != null">
        <Info class="comp" id="info" :activity="activity"/>
        <Map class="comp alt" id="map" :activity="activity"/>
        <Equipment class="comp" id="equipment" :activity="activity"/><!-- class="comp alt"-->
        <button class="comp" id="btn" @click="isChatVisible = !isChatVisible">Åpne chat</button>
        <Chat class="comp chat" id="chat" :activity="activity" /><!-- v-show="isChatVisible" -->
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
                isChatVisible: false,
            }
        }
    }
</script>
<style>
    #container{
        display: grid;
        grid-template-areas: 
        "info map"
        "info equipment"
        "btn equipment"
        "chat chat";
        background-color: #F6F6F6;
        height: 100%;
    }
    .comp{
        background-color: white;
        width: 40vw;
        box-shadow: 0px 4px 4px 0px #00000040;
    }
    .alt{
        height: 30vh;
    }
    #info{
        grid-area: info;
        height: 95%;
        text-align: center;
    }
    #map{
        grid-area: map;
        border: 1px solid black;
        height: 350px;
    }
    #equipment{
        grid-area: equipment;
        text-align: left;
        padding-left: 2%;
        height: 75%;
        margin-top: 5%;
    }
    #btn{
        grid-area: btn;
    }
    #chat{
        grid-area: chat;
        width: 87.5;
        margin-top: 2%;
    }
</style>
