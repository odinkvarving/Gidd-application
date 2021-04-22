<template>
    <div id="activity" v-if="activity != null">
        <Info class="comp" id="info" :activity="activity"/>
        <Map class="comp" id="map" :activity="activity"/>
        <Equipment class="comp" id="equipment" :activity="activity"/>
        <button class="comp" id="btnVisible" @click="changeChatVisibility">Åpne chat</button>
        <Chat class="comp chat" id="chat" :activity="activity" v-show="isChatVisible"/>
    </div>
</template>


<script>
import Info from "./Info.vue";
import Map from "./Map.vue";
import Equipment from "./Equipment.vue";
import Chat from "./Chat.vue";
import { userService } from "../../services/UserService.js";

export default {
  name: "ActivityCard",
  components: {
    Info,
    Map,
    Equipment,
    Chat,
  },

  props: {
    activity: {
      type: Object,
      required: true,
    },
  },

    data() {
        return {
            isChatVisible: false,
            isLoggedIn: userService.isLoggedIn()
        }
    },

    methods: {
        changeChatVisibility() {
            this.isChatVisible = !this.isChatVisible;
            const btn = document.getElementById('btnVisible');
            if (this.isChatVisible) {
                btn.childNodes[0].nodeValue = "Skjul chat";
            } else {
                btn.childNodes[0].nodeValue = "Åpne chat";
            }
        }
    }
}
</script>
<style>
    #activity{
        display: grid;
        grid-template-areas: 
        "info map"
        "info equipment"
        "btn equipment"
        "chat chat";
        /*display: flex;
        flex-wrap: wrap;
        flex-direction: row;
        background-color: #F6F6F6;*/
    }
    .comp{
        background-color: white;
        width: 40vw;
        box-shadow: 0px 4px 4px 0px #00000040;
    }
    #info{
        grid-area: info;
        height: 90%;
        text-align: center;
    }
    #map{
        grid-area: map;
        height: 380px;
    }
    #equipment{
        grid-area: equipment;
        text-align: left;
        padding-left: 2%;
        height: 80%;
        margin-top: 2%;
    }
    #btnVisible{
        grid-area: btn;
        border: none;
        background-color: #FFBD3E;
        color: white;
    }
    #chat{
        grid-area: chat;
        width: 87vw;
        margin-top: 1%;
    }
</style>
