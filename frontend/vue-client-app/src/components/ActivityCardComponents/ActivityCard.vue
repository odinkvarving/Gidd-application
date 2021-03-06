<template>
  <div id="activity" v-if="activity != null">
    <div
      style="
        margin-top: 70px;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
      "
    >
      <div v-show="isActivityHost" class="cancel-button-container">
        <b-button
          :disabled="this.activity.cancelled"
          @click="cancelActivity()"
          class="cancel-button"
          variant="danger"
          >Avlys aktivitet</b-button
        >
      </div>
      <div class="upper-row">
        <Info
          class="comp"
          :activity="activity"
          :weather="weather"
          :isLoggedIn="isLoggedIn"
          :isActivityHost="isActivityHost"
          :creatorImageURL="creatorAccountInfo.imageURL"
        />
        <div class="map-equipment-container">
          <Map
            class="comp"
            id="map"
            :latitude="activity.latitude"
            :longitude="activity.longitude"
          />
          <Equipment
            class="comp"
            id="equipment"
            :activity="activity"
            :isActivityHost="isActivityHost"
          />
        </div>
      </div>
      <button
        class="btn btn-lg"
        :disabled="this.activity.cancelled"
        id="btnVisible"
        @click="changeChatVisibility"
      >
        Skjul kommentarfelt
      </button>
      <Chat
        class="chat"
        id="chat"
        :activity="activity"
        v-show="isChatVisible"
      />
    </div>
    <ConfirmModal
      name="cancelling-success"
      header="Vellyket!"
      info="Avlysing av aktivitet var vellykket!"
      buttonText="OK"
    />
    <ErrorModal
      name="cancelling-error"
      header="Error"
      info="Avlysing av aktivitet gikk galt!"
      buttonText="OK"
    />
  </div>
</template>
<script>
import Info from "./Info.vue";
import Map from "./Map.vue";
import Equipment from "./Equipment.vue";
import Chat from "./Chat.vue";
import { userService } from "../../services/UserService.js";
import ErrorModal from "../PopUpComponents/ErrorModal.vue";
import ConfirmModal from "../PopUpComponents/ConfirmModal.vue";
//import {weatherService} from '../../services/WeatherService.js'

/**
 * ActivityCard is a component which represents a clicked activity.
 * The component is found in router/Activity.
 * The component itself contains four other components: Info, Map, Equipment and Chat.
 * Each of these components represents their own part of an activity.
 *
 * @author Scott Rydberg Sonen
 * @author Magnus Bredeli
 */
export default {
  name: "ActivityCard",
  components: {
    Info,
    Map,
    Equipment,
    Chat,
    ErrorModal,
    ConfirmModal,
  },

  props: {
    /**
     * activity is an object passed from router/Activity.
     * The activity represents the clicked activity in activity feed.
     */
    activity: {
      type: Object,
      required: true,
    },
    /**
     * weather is an object passed from router/Activity.
     * The object represents weather at the activity's location.
     */
    weather: {
      type: Object,
      required: true,
    },
  },

  data() {
    return {
      /**
       * isChatVisible is a boolean which changes each time the button (btnVisible) is clicked.
       * When isChatVisible changes to false, the chat box will be not be displayed.
       */
      isChatVisible: true,
      /**
       * isLoggedIn is a boolean to check if the account is logged in or not.
       */
      isLoggedIn: false,
      /**
       * isActivityHost is a boolean which tells us if the account is the creator of the activity.
       */
      isActivityHost: false,

      creatorAccountInfo: {},
    };
  },

  /**
   * mounted runs when ActivityCard renders and defines two important variables.
   */
  async mounted() {
    this.isLoggedIn = await userService.isLoggedIn();
    this.isActivityHost = await this.checkIfActivityHost();
    this.creatorAccountInfo = await this.getCreatorAccountInfo(
      this.activity.creator.id
    );
    if (
      this.creatorAccountInfo.imageURL === null ||
      !this.creatorAccountInfo.imageURL.includes(
        "http://localhost:8080/profilepictures/"
      )
    ) {
      this.creatorAccountInfo.imageURL =
        "http://localhost:8080/profilepictures/";
    }
  },
  methods: {
    /**
     * changeChatVisibility is a function which changes the state of chat visibility.
     * When btnVisible is clicked, it will change value, depending on if the chat is visible or not.
     */
    changeChatVisibility() {
      this.isChatVisible = !this.isChatVisible;
      const btn = document.getElementById("btnVisible");
      if (this.isChatVisible) {
        btn.childNodes[0].nodeValue = "Skjul kommentarfelt";
      } else {
        btn.childNodes[0].nodeValue = "Vis kommentarfelt";
      }
    },

    /**
     * checkIfActivityHost is a function which checks if the account is the creator of the current activity.
     */
    async checkIfActivityHost() {
      if (this.isLoggedIn) {
        let accountId = await userService.getAccountByEmail().then((data) => {
          return data.id;
        });

        return this.activity.creator.id === accountId;
      } else {
        return false;
      }
    },

    /**
     * cancelActivity is a function which sends a PUT request to backend which then updates the activity.
     * Activity's cancelled-variable is then changed to true.
     */
    cancelActivity() {
      let url = `http://localhost:8080/activities/${this.activity.id}/cancel`;

      const requestOptions = {
        method: "PUT",
        headers: userService.authorizationHeader(),
      };

      fetch(url, requestOptions)
        .then((response) => response.json())
        .then((data) => {
          if (data === true) {
            this.$bvModal.show("cancelling-success");
            this.activity.cancelled = true;
            this.isChatVisible = false;
          } else {
            this.$bvModal.show("cancelling-error");
          }
        })
        .catch((error) => console.log(error));
    },
    async getCreatorAccountInfo(creatorId) {
      let url = `http://localhost:8080/accounts/${creatorId}/info`;

      const requestOptions = {
        method: "GET",
      };

      return await fetch(url, requestOptions)
        .then((response) => response.json())
        .then((data) => {
          return data;
        })
        .catch((error) => console.log(error));
    },
  },
};
</script>
<style scoped>
.cancel-button-container {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.cancel-button {
  width: 200px;
  height: 40px;
  margin: 20px;
}

#activity {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  background-color: #f6f6f6;
}

.upper-row {
  display: flex;
  flex-direction: row;
  margin: 50px 0;
}
.comp {
  background-color: white;
  box-shadow: 0px 4px 4px 0px #00000040;
}
#info {
  width: 40vw;
}

.map-equipment-container {
  display: flex;
  flex-flow: column;
  justify-content: space-between;
  margin-left: 40px;
}

#map {
  width: 40vw;
  height: 40vh;
  margin: 0 0 30px 0;
}
#equipment {
  text-align: left;
  width: 40vw;
  height: 40vh;
}
#btnVisible {
  border: none;
  background-color: #ffbd3e;
  color: white;
  margin-bottom: 20px;
}
#chat {
  width: 80vw;
  margin-top: 1%;
}

@media (max-width: 1200px) {
  .upper-row {
    flex-direction: column;
  }

  #info {
    width: 80vw;
  }

  .map-equipment-container {
    margin-left: 0;
    align-items: center;
    width: 80vw;
    margin: 30px 0;
  }

  #map {
    width: 80vw;
    margin: 30px 0;
  }

  #equipment {
    width: 80vw;
  }
}
</style>