<template>
  <div id="infobox" v-if="activity && isDataReady">
    <div class="header-description-section">
      <b-icon
        class="toggle-edit-button"
        icon="pencil"
        @click="toggleEditMode"
        v-show="isActivityHost && !activity.cancelled"
      ></b-icon>

      <h1 v-show="!inEditMode">{{ activity.title }}</h1>
      <h1 class="edit" v-show="inEditMode">
        <input class="title" type="text" :placeholder="title" v-model="title" />
      </h1>
      <div id="ownerInfo">
        <img :src="require('@/assets/kari.jpg')" />
        <h3 class="txt">{{ activity.creator.email }}</h3>
      </div>
      <div class="description-container">
        <p class="txt" v-show="!inEditMode">{{ activity.description }}</p>
      </div>

      <p class="txt edit" v-show="inEditMode">
        <input
          class="description"
          type="text"
          :placeholder="description"
          v-model="description"
        />
      </p>
    </div>
    <h3>Informasjon:</h3>
    <div class="box" id="bottom">
      <ul class="list" id="list1">
        <li class="txt">Kategori:</li>
        <li class="txt">Sted:</li>
        <li class="txt" v-show="!inEditMode">Tid:</li>
        <li class="txt" v-show="inEditMode">Start tid:</li>
        <li class="txt" v-show="inEditMode">Slutt tid:</li>
        <li class="txt" v-show="!inEditMode">Varighet:</li>
        <li class="txt" v-show="!inEditMode">Værmelding:</li>
        <li class="txt">Nivå:</li>
        <li class="txt">Deltakere:</li>
      </ul>
      <ul class="list" id="list2" v-show="!inEditMode">
        <li class="txt">{{ activity.activityType.type }}</li>
        <li class="txt">{{ activity.location }}</li>
        <li class="txt">{{ activity.startTime }}</li>
        <li class="txt">{{ duration }}</li>
        <li class="txt" v-if="weather">
          <img
            id="icon"
            alt="weather icon"
            :src="require('@/assets/weatherIcons/' + weather.icon + '.png')"
          />
          {{ weather.temp }} C°
        </li>
        <li class="txt" v-else>Ingen værmelding</li>
        <li class="txt" v-show="!inEditMode">{{ activity.level.description }}</li>
        <li class="txt">
          {{ currentParticipants }} / {{ activity.maxParticipants }}
        </li>
        <li
          style="font-size: 13px; opacity: 70%"
          v-if="participantsInQueue > 0"
        >
          + {{ participantsInQueue }} på venteliste
        </li>
      </ul>

      <ul class="list" id="list2" v-show="inEditMode">
        <li class="txt">
          <b-form-select
            size="sm"
            :state="categoryState"
            v-model="category"
            :options="categories"
            style="width: 80%;"
          >
            <template #first>
              <b-form-select-option :value="null" disabled
                >-- Velg en kategori --</b-form-select-option
              >
            </template>
          </b-form-select>
        </li>
        <li class="txt">
          <LocationSearchBar @setPlace="setLocation" />
        </li>
        <li class="txt">
          <b-form-datepicker
            class="datepicker"
            placeholder="Velg dato"
            v-model="startDate"
            size="sm"
          ></b-form-datepicker>
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            v-model="startTimeStamp"
            size="sm"
          ></b-form-timepicker>
        </li>

        <li>
          <b-form-datepicker
            class="datepicker"
            placeholder="Velg dato"
            v-model="endDate"
            size="sm"
          ></b-form-datepicker>
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            v-model="endTimeStamp"
            size="sm"
          ></b-form-timepicker>
        </li>
        <b-form-select
          :state="levelState"
          v-model="level"
          :options="levels"
          style="width:80%"
        >
          <template #first>
            <b-form-select-option :value="null" disabled
              >-- Velg et nivå --</b-form-select-option
            >
          </template>
        </b-form-select>
        <li class="txt">
          <input type="text" placeholder="Antall" v-model="maxParticipants" />
        </li>
      </ul>
    </div>
    <div v-show="!activity.cancelled && !isExpired">
      <div v-show="!inEditMode">
        <button
          v-if="!isFull && !alreadyParticipating"
          id="btn"
          class="join"
          @click.stop="joinButtonClicked()"
        >
          <div
            v-if="showJoinSpinner"
            class="spinner-border"
            role="status"
            style="margin-top: 4px"
          >
            <span class="sr-only">Loading...</span>
          </div>
          <span v-else>{{ getButtonStatus() }}</span>
        </button>
        <button
          v-else-if="isFull && !alreadyParticipating"
          id="btn"
          class="full"
          @click.stop="joinButtonClicked()"
        >
          <span>{{ getButtonStatus() }}</span>
        </button>
        <button
          v-else
          id="btn"
          :class="{ inQueue: isInQueue, participating: !isInQueue }"
          @click.stop="removeParticipantClicked()"
        >
          <div
            v-if="showRemoveSpinner"
            class="spinner-border"
            role="status"
            style="margin-top: 4px"
          >
            <span class="sr-only">Loading...</span>
          </div>
          <span id="test-id" v-else>{{
            isInQueue ? "På venteliste" : "Påmeldt"
          }}</span>
        </button>
      </div>
      <button v-show="inEditMode" @click="onClickSaveButton">
        <span>Lagre</span>
      </button>
    </div>
    <b-button
      disabled
      v-show="activity.cancelled && !isExpired"
      @click="cancelActivity()"
      class="cancel-button"
      variant="danger"
      >Aktivitet avlyst!</b-button
    >
    <b-button
      disabled
      v-show="isExpired"
      class="expired-button"
      style="margin-bottom: 30px"
      >Utgått!</b-button
    >
  </div>
</template>
<script>
/**
 * Useful services and components are imported for efficient use and required functions.
 */
import { userService } from "../../services/UserService";
import { activityButtonService } from "../../services/ActivityButtonService";
import moment from "moment";
import LocationSearchBar from "../createActivityComponents/LocationSearchBar.vue";
import { notificationService } from "../../services/NotificationService";

/**
 * Info is a component which displays all relevant information about an activity.
 * 
 * @author Scott Rydberg Sonen
 * @author Mattias Agentoft Eggen
 * @author Magnus Bredeli
 */
export default {
  name: "Info",
  components: {
    LocationSearchBar,
  },

  props: {
    /**
     * activity is an object passed from ActivityCard which contains clicked activity.
     */
    activity: {
      type: Object,
      required: true,
    },
    /**
     * weather is an object passed from ActivityCard which contains weather at activity location.
     */
    weather: {
      type: Object,
      required: true,
    },
    /**
     * isLoggedIn is a boolean passed from ActivityCard representing an account's login state.
     */
    isLoggedIn: {
      type: Boolean,
      required: true,
    },
    /**
     * isActivityHost is a boolean passed from ActivityCard which tells us if an account is the creator of an activity.
     */
    isActivityHost: {
      type: Boolean,
      required: true,
    },
  },

  data() {
    return {
      /**
       * inEditMode represents the state of edit mode.
       */
      inEditMode: false,
      /**
       * isFull tells us if the activity is full or not.
       */
      isFull: false,
      /**
       * alreadyParticipating tells us whether the account is participating in the activity or not.
       */
      alreadyParticipating: false,
      /**
       * currentParticipants represents the number of current participants in the activity.
       */
      currentParticipants: 0,
      /**
       * showJoinSpinner represents the visibility of join spinner.
       */
      showJoinSpinner: false,
      /**
       * showRemoveSpinner represents the visibility of remove spinner.
       */
      showRemoveSpinner: false,
      /**
       * participantsInQueue represents number of participants in an activity's queue.
       */
      participantsInQueue: 0,
      /**
       * queuePosition represents the queue position of the account.
       */
      queuePosition: 0,
      /**
       * isInQueue represents the state of an account's existence in the queue.
       */
      isInQueue: false,
      /**
       * duration represents the duration of an activity as a string.
       */
      duration: "",
      /**
       * isDataReady is a flag which tells us when fetched data is ready for utilization.
       */
      isDataReady: false,
      /**
       * isExpired tells us if the activity has expired.
       */
      isExpired: false,
      /**
       * title is a pointer to activity's title and can be edited in edit mode.
       */
      title: this.activity.title,
      /**
       * category is a pointer to activity's category type and can be edited in edit mode.
       */
      category: this.activity.activityType.type,
      /**
       * categories is an array containing all categories from database.
       */
      categories: [],
      /**
       * level is a pointer to activity's level and can be edited in edit mode.
       */
      level: this.activity.level,
      /**
       * levels is an array containing all levels from database.
       */
      levels: [],
      /**
       * location is a pointer to activity's location and can be edited in edit mode.
       */
      location: this.activity.location,
      /**
       * startDate represent the start date of an activity and can be edited in edit mode.
       */
      startDate: "",
      /**
       * startTimeStamp represents the clock time of start time and can be edited in edit mode.
       */
      startTimeStamp: "",
      /**
       * endDate represents the end date of an activity and can be edited in edit mode.
       */
      endDate: "",
      /**
       * endTimeStamp represents the clock time of end time and can be edited in edit mode.
       */
      endTimeStamp: "",
      /**
       * maxParticipants is a pointer to activity's max number of participants and can be edited in edit mode.
       */
      maxParticipants: this.activity.maxParticipants,
      /**
       * description is a pointer to activity's description and can be edited in edit mode.
       */
      description: this.activity.description,
      /**
       * nameState represents the state of name change.
       */
      nameState: null,
      /**
       * categoryState represents the state of category change.
       */
      categoryState: null,
      /**
       * levelState represents the state of level change.
       */
      levelState: null,
      /**
       * geometryFound tells us if geometry is found.
       */
      geometryFound: false,
      /**
       * newLocation tells us if activity will be updated with new location or former version.
       */
      newLocation: false,
      /**
       * center represents a center point of the location, containing latitude and longitude of activity.
       */
      center: {},
      /**
       * startDateState represents the state of start date change.
       */
      startDateState: null,
      /**
       * endDateState represents the state of end date change.
       */
      endDateState: null,
      /**
       * participantsState represents the state of participants change.
       */
      participantsState: null,
      /**
       * descriptionState represents the state of description change.
       */
      descriptionState: null,
      /**
       * equipmentState represents the state of equipment change.
       */
      equipmentState: null,
    };
  },

  /**
   * mounted is a function which runs during render.
   * Here all important and relevant data will be found and requested.
   */
  async mounted() {
    await this.getCategories();
    await this.getLevels();
    await this.getCurrentParticipantsNumber();
    if (this.isLoggedIn) {
      await this.isAlreadyParticipating();
      if ((this.currentParticipants === this.maxParticipants) && (this.queuePosition > 0)) {
        this.isInQueue = true;
      }
    }
    this.getDuration();
    this.checkIfExpired();
    this.isDataReady = true;
  },

  methods: {
    /**
     * checkIfExpired is a function which checks if start time is before current time.
     * If it is, it means that the activity has expired, and it is not possible to join it anymore.
     */
    checkIfExpired() {
      const today = Date.now();
      const start = new Date(this.activity.startTime);
      const check = start - today;
      if (check < 0) {
        this.isExpired = true;
      } 
    },

    /**
     * joinButtonClicked is a function which runs on join button click
     *  and adds account as a participant of the activity.
     */
    joinButtonClicked() {
      this.showJoinSpinner = true;
      if (activityButtonService.joinButtonClicked()) {
        this.addParticipantToActivity(this.activity);
      }
    },

    /**
     * removeParticipantClicked is a function which runs removeParticipantFromActivity function in activityButtonService
     *  if 'OK' is clicked in a confirmation box.
     * The function also changes number of current participants and queue participants for display purpose.
     */
    async removeParticipantClicked() {
      if (activityButtonService.showRemoveAlert()) {
        this.showRemoveSpinner = true;
        const data = await activityButtonService.removeParticipantFromActivity(this.activity);
        if (data) {
          this.showRemoveSpinner = false;
          if ((this.currentParticipants === this.activity.maxParticipants) && (this.queuePosition > 0)) {
            this.participantsInQueue--;
          } else {
            this.currentParticipants--;
          }
          this.alreadyParticipating = false;
          this.isInQueue = false;
          this.$emit("refresh-list", this.activity.id, false);
        }
      }
    },

    /**
     * getButtonStatus is a function which runs getButtonStatus function in activityButtonService
     *  and receives button status.
     */
    getButtonStatus() {
      let status = activityButtonService.getButtonStatus(
        this.alreadyParticipating,
        this.currentParticipants,
        this.activity
      );
      if (status === "Fullt") {
        this.isFull = true;
      }
      return status;
    },

    /**
     * isAlreadyParticipating is a function which runs isAlreadyParticipating function in activityButtonService.
     * If account is present in activity_account table in the database, 
     *  the function also checks the queue position of account.
     * Queue position remains 0 if user is not present in the queue.
     */
    async isAlreadyParticipating() {
      this.alreadyParticipating = await activityButtonService.isAlreadyParticipating(this.activity);
      if (this.alreadyParticipating) {
        this.getQueuePosition();
      }
    },

    /**
     * getCurrentParticipantsNumber runs getCurrentParticipantsNumber function in activityButtonService.
     * If current participants equals max limit, 
     *  the function also checks how many accounts the queue contains.
     * Lastly, the function emits currentparticipants back to ActivityFeed,
     *  along with activity ID as a key.
     */
    async getCurrentParticipantsNumber() {
      this.currentParticipants = await activityButtonService.getCurrentParticipantsNumber(this.activity);
      if (this.currentParticipants == this.activity.maxParticipants) {
        this.participantsInQueue = await activityButtonService.countAccountsInQueue(
          this.activity
        );
      }
    },

    /**
     * addParticipantToActivity runs addParticipantToActivity function in activityButtonService.
     * If the called function returns correct values,
     *  the connection was correctly added to the database.
     * Different variables are modified when the function confirms that the account is successfully participating.
     */
    async addParticipantToActivity() {
      let data = await activityButtonService.addParticipantToActivity(this.activity);
      let accountId = await userService.getAccountByEmail().then((data) => (accountId = data.id));
      if (data.activityId === this.activity.id && data.accountId === accountId) {
        if (this.currentParticipants === this.activity.maxParticipants) {
          this.participantsInQueue++;
          this.isInQueue = true;
        } else {
          this.currentParticipants++;
        }
        this.alreadyParticipating = true;
        this.showJoinSpinner = false;
        this.$emit("refresh-list", this.activity.id, true);
      }
    },

    /**
     * countAccountsInQueue runs countAccountsInQueue function in activityButtonService.
     * Return value equals the number of participants in the queue.
     */
    async countAccountsInQueue() {
      this.participantsInQueue = await activityButtonService.countAccountsInQueue(
        this.activity
      );
    },

    /**
     * getQueuePosition runs getQueuePosition function in activityButtonService.
     * Return value equals queue position of account.
     * If the queue position is larger than 0, it means that the account is in the queue.
     */
    async getQueuePosition() {
      this.queuePosition = await activityButtonService.getQueuePosition(this.activity);

      if (this.queuePosition > 0) {
        this.isInQueue = true;
      }
    },

    /**
     * getDuration is a function which calculates the duration of an activity.
     * Return value is the duration in days, hours, minutes and seconds,
     *  (if each variable is not 0).
     * It is represented as a string.
     */
    getDuration() {
      const start = new Date(this.activity.endTime);
      const end = new Date(this.activity.startTime);
      let diff = Math.abs(end - start); //Difference between start and end

      let days = Math.floor(diff / 86400000); //Calculate whole days
      diff -= days * 86400000; //Subtract whole days from diff
      if (days > 0) this.duration += days + "d, ";

      let hours = Math.floor(diff / 3600000) % 24; //Calculate whole hours
      diff -= hours * 3600000; //Subtract whole hours from diff
      if (hours > 0) this.duration += hours + "t, ";

      let minutes = Math.floor(diff / 60000) % 60; //Calculate whole minutes
      diff -= minutes * 60000; //Subtract whole minutes from diff
      if (minutes > 0) this.duration += minutes + "m, ";

      let seconds = Math.floor(diff % 60); //Calculating whole seconds
      if (seconds > 0) this.duration += seconds + "s, ";
    },

    /**
     * toggleEditMode is a function which changes the state of edit mode.
     */
    toggleEditMode() {
      this.inEditMode = !this.inEditMode;
    },

    /**
     * onClickSaveButton is a function which saves all changes made,
     *  and calls editActivity function to update the activity.
     */
    onClickSaveButton() {
      this.showSpinner = true;
      this.equipmentState = true;
      this.name === "" ? (this.nameState = false) : (this.nameState = true);
      this.description === ""
        ? (this.descriptionState = false)
        : (this.descriptionState = true);
      this.validStartAndEndDate();
      if (this.nameState === true && this.descriptionState === true) {
        this.editActivity();
      }
      this.inEditMode = false;
    },

    /**
     * setLocation is a function which defines new current location.
     */
    setLocation(location) {
      if (location.geometry) {
        this.currentLocation = location;
        this.center = {
          lat: this.currentLocation.geometry.location.lat(),
          lng: this.currentLocation.geometry.location.lng(),
        };
        this.geometryFound = true;
        this.newLocation = true;
      } else {
        this.geometryFound = false;
        this.newLocation = false;
        this.currentLocation = location;
      }
    },

    /**
     * editActivity is a function which updates activity with new changes.
     * The account do not a actually need to update all variables.
     * All unedited variables will stay the same as the previous version.
     * A PUT request is sent to backend with the updated activity.
     */
    async editActivity() {
      let accountDetails = await userService.getAccountByEmail();

      let activity = {
        id: this.activity.id,
        title: this.title,
        description: this.description,
        equipment: this.activity.equipment,
        location: this.location,
        latitude: null, 
        longitude: null, 
        maxParticipants: this.maxParticipants,
        startTime: `${this.startDate} ${this.startTimeStamp}`,
        endTime: `${this.endDate} ${this.endTimeStamp}`,
        activityType: {
          type: this.category,
        },
        level: {
          description: this.level,
        },
        creator: accountDetails,
      };

      if (this.startDate === "" || this.startTimeStamp === "") {
        activity.startTime = this.activity.startTime;
      }

      if (this.endDate === "" || this.endTimeStamp === "") {
        activity.endTime = this.activity.endTime;
      }

      if (activity.activityType.type === null) {
        activity.activityType = this.activity.activityType;
      }

      if (activity.level.description === null) {
        activity.level = this.activity.level;
      }

      if (!this.newLocation) {
        activity.latitude = this.activity.latitude;
        activity.longitude = this.activity.longitude;
        activity.location = this.location;
      }

      if (this.geometryFound) {
        activity.latitude = this.currentLocation.geometry.location.lat();
        activity.longitude = this.currentLocation.geometry.location.lng();
        activity.location = this.currentLocation.name;
      }

      const requestOptions = {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: userService.getTokenString(),
        },
        body: JSON.stringify(activity),
      };

      fetch(
        `http://localhost:8080/activities/${this.activity.id}`,
        requestOptions
      )
        .then((response) => response.json())
        .then((data) => {
          // If update activity was successfull
          if (data !== null) {
            notificationService.sendNotificationToAllParticipants(
              this.activity.id
            );
          }
        })
        .catch((error) => console.log(error));
        location.reload();
    },

    /**
     * getCategories is a function which returns all categories from database by a GET request.
     */
    async getCategories() {
      let categoriesList;
      this.category = null;
      let url = `http://localhost:8080/activityTypes/`;

      await fetch(url, {
        method: "GET",
        headers: userService.authorizationHeader(),
      })
        .then((response) => response.json())
        .then((data) => (categoriesList = data))
        .catch((error) => console.log(error));

      for (let i = 0; i < categoriesList.length; i++) {
        this.categories.push(categoriesList[i].type);
      }
    },

    /**
     * getLevels is a function which returns all levels from database by a GET request.
     */
    async getLevels() {
      let levelsList;
      this.level = null;
      this.participantValue = "Alle";

      let url = `http://localhost:8080/levels/`;

      await fetch(url, {
        method: "GET",
        headers: userService.authorizationHeader(),
      })
        .then((response) => response.json())
        .then((data) => (levelsList = data))
        .catch((error) => console.log(error));

      for (let i = 0; i < levelsList.length; i++) {
        this.levels.push(levelsList[i].description);
      }
    },

    /**
     * validStartAndEndDate is a function which checks if start time and end time is valid.
     * The function also checks if end time is before start time.
     */
    validStartAndEndDate() {
      this.startDate === "" || this.startTime === ""
        ? (this.startDateState = false)
        : (this.startDateState = true);
      this.endDate === "" || this.endTime === ""
        ? (this.endDateState = false)
        : (this.endDateState = true);

      if (this.startDate === "" || this.startTime === "") {
        this.startDateState = false;
      }
      if (this.endDate === "" || this.endTime === "") {
        this.endDateState = false;
      } else {
        if (
          moment(`${this.startDate} ${this.startTime}`).isBefore(
            `${this.endDate} ${this.endTime}`
          )
        ) {
          this.endDateState = true;
          this.startDateState = true;
        } else {
          this.startDateState = false;
          this.endDateState = false;
        }
      }
    },
  },
};
</script>

<style scoped>
#infobox {
  width: 40vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
}
.box{
  text-align: center;
  align-items: center;
}

.header-description-section{
  margin-top: 20px;
  height: 50%;
  text-align: center;
}

h1{
  margin: 10px;
}

.info-section {
  width: 100%;
}

.info-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin: 30px;
}

.txt {
  font-size: 20px;
  text-align: start;
  width: 100%;
}

.description-container {
  margin: 20px;
}

#ownerInfo {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  justify-content: space-around;
  margin: 30px;
}
#ownerInfo img {
  width: 70px;
  height: 70px;
  border-radius: 35px;
  box-shadow: 0px 4px 4px 0px #0000001a;
  margin: 5px;
}
#ownerInfo h3 {
  margin: auto;
}

#icon{
  width: 30px;
  height: 30px;
  margin-right: 10px;
}

#btn{
  height: 50px;
  width: 160px;
  border-radius: 6px;
  font-size: 20px;
  cursor: pointer;
  background-color: #ffbd3e;
  color: white;
  border: 0;
  outline: none;
  margin: 40px;
}
#btn:hover {
  background-color: #eca82b;
  transition: 0.2s;
}

#btn.full {
  background-color: #ff5b3e;
}
#btn.full:hover {
  background-color: #91301f;
  transition: 0.2s;
}
#btn.full:hover span {
  display: none;
}
#btn.full:hover:before {
  content: "Venteliste";
}

#btn.participating {
  background-color: #4a934a;
  transition: 0.2s;
}
#btn.participating:hover {
  background-color: #408140;
  transition: 0.2s;
}
#btn.participating:hover span {
  display: none;
}
#btn.participating:hover:before {
  content: "Meld av";
}
#btn.inQueue {
  background-color: #ff5b3e;
}
#btn.inQueue:hover {
  background-color: #91301f;
  transition: 0.2s;
}
#btn.inQueue:hover span {
  display: none;
}
#btn.inQueue:hover:before {
  content: "Meld av";
}
.queue-list {
  opacity: 70%;
}

.box {
  display: flex;
  width: 100%;
}

.pencil {
  cursor: pointer;
}

.toggle-edit-button {
  cursor: pointer;
}

.edit .title {
  width: 75%;
  text-align: center;
  height: 60px;
}

.edit .description {
  margin: 0 auto;
  width: 75%;
  height: 40px;
}

.datepicker {
  width: 50% !important;
  min-width: 110px !important;
}

.timepicker {
  width: 50% !important;
  min-width: 110px !important;
}

.cancel-button {
  margin: 40px;
}

.expired-button {
  height: 50px;
  width: 160px;
}

@media (max-width: 1200px) {
  
  h1{
    font-size: 24px;
  }

  h3{
    font-size: 20px;
  }

  .header-description-section {
    height: 50%;
  }

  #infobox{
    width: 80vw;
  }

  #list1{
    margin-left: 10vw;
  }

  .txt {
    font-size: 15px;
  }

  .info-row {
    margin: 0;
  }
}
</style>
