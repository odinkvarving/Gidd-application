<template>
  <div class="info-box-container" v-if="activity && isDataReady">
    <div class="box">
      <div class="title-container">
        <h1 class="title" v-show="!inEditMode">{{ activity.title }}</h1>
        <h1 class="edit" v-show="inEditMode">
          <input
            class="title"
            type="text"
            :placeholder="title"
            v-model="title"
            style="heigth: 68px;"
          />
        </h1>
        <div class="edit">
          <b-icon
            class="toggle-edit-button"
            icon="pencil"
            @click="toggleEditMode"
            v-show="isActivityHost && !activity.cancelled"
            font-scale="1.5"
          ></b-icon>
        </div>
      </div>
      <div id="ownerInfo">
        <img :src="require('@/assets/kari.jpg')" />
        <h3 class="txt">{{ activity.creator.email }}</h3>
      </div>
      <p class="txt" v-show="!inEditMode">{{ activity.description }}</p>

      <p class="txt edit" v-show="inEditMode">
        <input
          class="description"
          type="text"
          :placeholder="description"
          v-model="description"
        />
      </p>
    </div>
    <div class="box" id="bottom">
      <h3>Informasjon:</h3>
      <ul class="list" id="list1">
        <li class="txt">Kategori:</li>
        <li class="txt">Sted:</li>
        <li class="txt">Tid:</li>
        <li class="txt">Varighet:</li>
        <li class="txt">Værmelding:</li>
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
        <!-- Replace this with actual location when implemented -->
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
    <!--<div>
      <img alt="Participant profile picture" v-for="image in images" :key="image.url" :src="image.url">
    </div>-->
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
      >Utgått!</b-button
    >
  </div>
</template>

<script>
import { userService } from "../../services/UserService";
import { activityButtonService } from "../../services/ActivityButtonService";
import moment from "moment";
import LocationSearchBar from "../createActivityComponents/LocationSearchBar.vue";
import { notificationService } from "../../services/NotificationService";

export default {
  name: "Info",
  components: {
    LocationSearchBar,
  },

  props: {
    activity: {
      type: Object,
      required: true,
    },
    weather: {
      type: Object,
      required: true,
    },
    isLoggedIn: {
      type: Object,
      required: true,
    },
    isActivityHost: {
      type: Boolean,
      required: true,
    },
  },

  data() {
    return {
      inEditMode: false,
      isFull: false,
      alreadyParticipating: false,
      currentParticipants: 0,
      showJoinSpinner: false,
      showRemoveSpinner: false,
      participantsInQueue: 0,
      queuePosition: 0,
      isInQueue: false,
      duration: "",
      isDataReady: false,
      isExpired: false,

      title: this.activity.title,
      category: this.activity.activityType.type,
      categories: [],
      level: this.activity.level,
      levels: [],
      location: this.activity.location,
      startDate: "",
      startTimeStamp: "",
      endDate: "",
      endTimeStamp: "",
      maxParticipants: this.activity.maxParticipants,
      description: this.activity.description,
      nameState: null,
      categoryState: null,
      levelState: null,
      geometryFound: false,
      newLocation: false,
      center: {},
      startDateState: null,
      endDateState: null,
      participantsState: null,
      descriptionState: null,
      equipmentState: null,
    };
  },

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
    checkIfExpired() {
      const today = Date.now();
      const start = new Date(this.activity.startTime);
      const check = start - today;
      if (check < 0) {
        this.isExpired = true;
      } 
    },

    checkIfLoggedIn() {
      return userService.isLoggedIn();
    },
    joinButtonClicked() {
      this.showJoinSpinner = true;
      if (activityButtonService.joinButtonClicked()) {
        this.addParticipantToActivity(this.activity);
      }
    },

    async removeParticipantClicked() {
      if (activityButtonService.showRemoveAlert()) {
        this.showRemoveSpinner = true;
        const data = await activityButtonService.removeParticipantFromActivity(
          this.activity
        );
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

    async isAlreadyParticipating() {
      this.alreadyParticipating = await activityButtonService.isAlreadyParticipating(
        this.activity
      );
      if (this.alreadyParticipating) {
        this.getQueuePosition();
      }
    },

    async getCurrentParticipantsNumber() {
      // Get number of participators on this activity
      this.currentParticipants = await activityButtonService.getCurrentParticipantsNumber(
        this.activity
      );
      if (this.currentParticipants == this.activity.maxParticipants) {
        this.participantsInQueue = await activityButtonService.countAccountsInQueue(
          this.activity
        );
      }
    },

    async addParticipantToActivity() {
      let data = await activityButtonService.addParticipantToActivity(
        this.activity
      );
      let accountId = await userService
        .getAccountByEmail()
        .then((data) => (accountId = data.id));
      if (
        data.activityId === this.activity.id &&
        data.accountId === accountId
      ) {
        console.log("Joining activity was successful! Changing button style");
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

    async countAccountsInQueue() {
      this.participantsInQueue = await activityButtonService.countAccountsInQueue(
        this.activity
      );
    },

    async getQueuePosition() {
      this.queuePosition = await activityButtonService.getQueuePosition(
        this.activity
      );

      if (this.queuePosition > 0) {
        this.isInQueue = true;
      }
    },

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

    toggleEditMode() {
      this.inEditMode = !this.inEditMode;
      console.log("Edit Mode: " + this.inEditMode);
    },

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
        console.log("Activity updated!");
      }
    },

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

    async editActivity() {
      let accountDetails = await userService.getAccountByEmail();

      let activity = {
        id: this.activity.id,
        title: this.title,
        description: this.description,
        equipment: this.activity.equipment,
        location: this.location,
        latitude: null, //temporary until map is implemented
        longitude: null, //temporary until map is implemented
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
        console.log("No category selected, using previous");
        activity.activityType = this.activity.activityType;
      }

      if (activity.level.description === null) {
        console.log("No level selected, using previous");
        activity.level = this.activity.level;
      }

      if (!this.newLocation) {
        console.log("No location entered, using previous");
        activity.latitude = this.activity.latitude;
        activity.longitude = this.activity.longitude;
        activity.location = this.location;
      }

      if (this.geometryFound) {
        activity.latitude = this.currentLocation.geometry.location.lat();
        activity.longitude = this.currentLocation.geometry.location.lng();
        activity.location = this.currentLocation.name;
        console.log("this.location: " + this.location);
        console.log("this.currentLocation.name: " + this.currentLocation.name);
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
            let result = notificationService.sendNotificationToAllParticipants(
              this.activity.id
            );
            if (result === true) {
              console.log(
                "Sucessfully notified all participants about the edit!"
              );
            } else {
              console.log(
                "Error! Something went wrong when notifying participants!"
              );
            }
          }
        })
        .catch((error) => console.log(error));
    },

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

    checkIfFull() {
      if (this.activity.currentParticipants < this.activity.totalParticipants) {
        return "Bli med";
      } else {
        this.isFull = true;
        return "Fullt";
      }
    },
  },
};
</script>

<style>
#infobox {
  display: grid;
  grid-template-areas:
    "top"
    "bottom";
  height: 50vh;
  text-align: center;
}
/*.box{
        margin: 2vh 2vw 2vh 2vw;
    }*/
.txt {
  font-size: 20px;
}
#top {
  grid-area: top;
  display: grid;
  grid-template-areas:
    "title"
    "ownerInfo"
    "desc";
}
#top h1 {
  grid-area: title;
}
#ownerInfo {
  grid-area: ownerInfo;
  display: grid;
  grid-template-areas: "image name";
  margin: auto;
  width: 50%;
}
#ownerInfo img {
  grid-area: image;
  width: 70px;
  height: 70px;
  border-radius: 35px;
  box-shadow: 0px 4px 4px 0px #0000001a;
}
#ownerInfo h3 {
  grid-area: name;
  margin: auto;
}
#top p {
  grid-area: desc;
  height: 100px;
}
.list {
  list-style-type: none;
}
#bottom {
  grid-area: bottom;
  display: grid;
  grid-template-areas:
    "infoTitle infoTitle"
    "list1 list2"
    "btn btn";
}
#bottom h3 {
  grid-area: infoTitle;
}
#list1 {
  grid-area: list1;
  text-align: left;
  margin-left: 0;
}
#list2 {
  grid-area: list2;
  text-align: right;
  margin-right: 40px;
}
#icon {
  width: 25px;
  height: 25px;
  margin-right: 10px;
}
#btn {
  height: 50px;
  width: 160px;
  border-radius: 6px;
  font-size: 20px;
  cursor: pointer;
  background-color: #ffbd3e;
  color: white;
  border: 0;
  outline: none;
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

.title-container {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-left: 12px;
}

.expired-button {
  height: 50px;
  width: 160px;
}
</style>
