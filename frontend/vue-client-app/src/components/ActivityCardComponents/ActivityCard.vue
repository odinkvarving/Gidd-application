<template>
  <div id="container" v-if="activity != null">
    <Info class="comp" id="info" :activity="activity" />
    <Map class="comp" id="map" :activity="activity" />
    <Equipment class="comp" id="equipment" :activity="activity" />
    <button
      class="comp"
      id="btnVisible"
      @click="isChatVisible = !isChatVisible"
    >
      Åpne chat
    </button>
    <Chat
      class="comp chat"
      id="chat"
      :activity="activity"
      v-show="isChatVisible"
    /><!-- v-show="isChatVisible" -->
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
      isLoggedIn: userService.isLoggedIn(),
    };
  },
};
</script>
<style>
#container {
  display: grid;
  grid-template-areas:
    "info map"
    "info equipment"
    "btn equipment"
    "chat chat";
  background-color: #f6f6f6;
}
.comp {
  background-color: white;
  width: 40vw;
  box-shadow: 0px 4px 4px 0px #00000040;
}
#info {
  grid-area: info;
  height: 90%;
  text-align: center;
}
#map {
  grid-area: map;
  height: 380px;
}
#equipment {
  grid-area: equipment;
  text-align: left;
  padding-left: 2%;
  height: 80%;
  margin-top: 2%;
}
#btnVisible {
  grid-area: btn;
  border: none;
  background-color: #ffbd3e;
  color: white;
}
#chat {
  grid-area: chat;
  width: 87vw;
  margin-top: 1%;
}
</style>
