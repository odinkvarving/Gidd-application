<template>
    <div id="activity" v-if="activity != null">
        <div class="upper-row">
            <Info class="comp" id="info" :activity="activity"/>
            <div class="map-equipment-container">
                <Map class="comp" id="map" :latitude="activity.latitude" :longitude="activity.longitude"/>
                <Equipment class="comp" id="equipment" :activity="activity"/>
            </div>
        </div>
        <button id="btnVisible" @click="changeChatVisibility">Åpne chat</button>
        <Chat class="chat" id="chat" :activity="activity" v-show="isChatVisible"/>
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
        display: flex;
        /*display: flex;
        flex-wrap: wrap;
        flex-direction: row;
        background-color: #F6F6F6;*/
    }

    .upper-row{
        display: flex;
        flex-direction: row wrap;
        height: 85vh;

    }
    .comp{
        background-color: white;
        box-shadow: 0px 4px 4px 0px #00000040;
    }
    #info{
        width: 40vw;
        text-align: center;
    }

    .map-equipment-container{
        display: flex;
        flex-flow: column;
        justify-content: space-between;
        margin-left: 40px;
    }

    #map{
        width: 40vw;
        height: 40vh;

    }
    #equipment{
        text-align: left;
        width: 40vw;
        height: 40vh;
    }
    #btnVisible{
        border: none;
        background-color: #FFBD3E;
        color: white;
    }
    #chat{
        width: 87vw;
        margin-top: 1%;
    }
</style>
