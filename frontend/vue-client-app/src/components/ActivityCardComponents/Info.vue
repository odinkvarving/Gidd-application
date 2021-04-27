<template>
  <div id="infobox" v-if="activity">
    <div class="box" id="top">
      <b-icon
        class="toggle-edit-button"
        icon="pencil"
        @click="toggleEditMode"
      ></b-icon>

      <h1 v-show="!inEditMode">{{ activity.title }}</h1>
      <h1 v-show="inEditMode"><input type="text" :placeholder="title" v-model="title"/></h1>
      <div id="ownerInfo">
        <img :src="require('@/assets/kari.jpg')" />
        <h3 class="txt">{{ activity.creator.email }}</h3>
      </div>
      <p class="txt" v-show="!inEditMode">{{ activity.description }}</p>
      <p class="txt" v-show="inEditMode">
        <input type="text" :placeholder="description" v-model="description"/>
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
        <li class="txt">{{ duration }}</li> <!-- Implement duration -->
        <li class="txt" v-if="weather">
          <img id="icon" alt="weather icon" :src="require('@/assets/weatherIcons/' + weather.icon + '.png')"/>
          {{ weather.temp }} C°
        </li>
        <li class="txt" v-else>Ingen værmelding</li>
        <li class="txt">{{ currentParticipants }} / {{ activity.maxParticipants }}</li>
        <li style="font-size: 13px; opacity: 70%" v-if="participantsInQueue > 0">+ {{ participantsInQueue }} på venteliste</li>
      </ul>

      <ul class="list" id="list2" v-show="inEditMode">
        <li class="txt">
          <input type="text" :placeholder="type" v-model="type" />
        </li>
        <li class="txt">
          <input type="text" :placeholder="location" v-model="location" />
        </li>
        <!-- Replace this with actual location when implemented -->
        <li class="txt">
          <b-form-datepicker
            class="datepicker"
            placeholder="Velg dato"
            v-model="startDate"
          ></b-form-datepicker>
        </li>
        <li class="txt">
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            v-model="startTimeStamp"
          ></b-form-timepicker>
        </li>
        <li class="txt">
          <b-form-datepicker
            class="datepicker"
            placeholder="Velg dato"
            v-model="endDate"
          ></b-form-datepicker>
        </li>
        <li class="txt">
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            v-model="endTimeStamp"
          ></b-form-timepicker>
        </li>
        <li></li>
        <li></li>
        <li class="txt">
          <input type="text" placeholder="Antall" v-model="maxParticipants" />
        </li>
      </ul>
    </div>
    <!--<div>
      <img alt="Participant profile picture" v-for="image in images" :key="image.url" :src="image.url">
    </div>-->
    <button v-if="!isFull && !alreadyParticipating && !inEditMode" id="btn" class="join" @click.stop="joinButtonClicked()">
      <div v-if="showJoinSpinner" class="spinner-border" role="status" style="margin-top: 4px">
        <span class="sr-only">Loading...</span>
      </div>
      <span v-else >{{ getButtonStatus() }}</span>
    </button>
    <button v-else-if="isFull && !alreadyParticipating && !inEditMode" id="btn" class="full" @click.stop="joinButtonClicked()"><span>{{ getButtonStatus() }}</span></button>
    <button v-else-if="!inEditMode" id="btn" :class="{ 'inQueue': isInQueue, 'participating': !isInQueue }" @click.stop="removeParticipantClicked()">
      <div v-if="showRemoveSpinner" class="spinner-border" role="status" style="margin-top: 4px">
        <span class="sr-only">Loading...</span>
      </div>
      <span id="test-id" v-else>{{ isInQueue ? "På venteliste" : "Påmeldt" }}</span>
    </button>
    <button v-if="inEditMode" @click="editActivity">
      <span>Fullfør</span>
    </button>
  </div>
</template>

<script>
import { userService } from "../../services/UserService";
import { activityButtonService } from "../../services/ActivityButtonService"
export default {
  name: "Info",

  props: {
    activity: {
      type: Object,
      required: true,
    },
    weather: {
      type: Object,
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

      title: this.activity.title,
      type: this.activity.activityType.type,
      location: "Trondheim", //TODO: Sjekk om denne skal være med
      startDate: "",
      startTimeStamp: "",
      endDate: "",
      endTimeStamp: "",
      maxParticipants: 10,
      description: this.activity.description,
    }
  },

  mounted(){
    this.getCurrentParticipantsNumber();
    if(this.isLoggedIn){
      this.isAlreadyParticipating();
    }
    this.getDuration();
  },

  methods: {
    checkIfLoggedIn() {
      return userService.isLoggedIn();
    },
    joinButtonClicked(){
      this.showJoinSpinner = true;
      if (activityButtonService.joinButtonClicked()) {
        this.addParticipantToActivity(this.activity);
      }
    },
    
    async removeParticipantClicked(){
      if (activityButtonService.showRemoveAlert()) {
        this.showRemoveSpinner = true;
        const data = await activityButtonService.removeParticipantFromActivity(this.activity);
        if (data) {
          this.showRemoveSpinner = false;
          if (this.currentParticipants === this.activity.maxParticipants) {
            this.participantsInQueue --;
          } else {
            this.currentParticipants --;
          }
          this.alreadyParticipating = false;
          this.isInQueue = false;
          this.$emit('refresh-list', this.activity.id, false);
        }
      }
    },
    
    getButtonStatus() {
      let status = activityButtonService.getButtonStatus(this.alreadyParticipating, this.currentParticipants, this.activity);
      if (status === "Fullt") {
        this.isFull = true;
      }
      return status;
    },
    
    async isAlreadyParticipating() {
      this.alreadyParticipating = await activityButtonService.isAlreadyParticipating(this.activity);
      if (this.alreadyParticipating) {
        this.getQueuePosition();
      }
    },

    async getCurrentParticipantsNumber(){
      // Get number of participators on this activity
      this.currentParticipants = await activityButtonService.getCurrentParticipantsNumber(this.activity);
      if(this.currentParticipants == this.activity.maxParticipants){
        this.participantsInQueue = await activityButtonService.countAccountsInQueue(this.activity);
      }
    },
    
    async addParticipantToActivity(){
      let data = await activityButtonService.addParticipantToActivity(this.activity);
      let accountId = await userService.getAccountByEmail().then(data => accountId = data.id);
      if(data.activityId === this.activity.id && data.accountId === accountId){
        console.log("Joining activity was successful! Changing button style");
        if(this.currentParticipants === this.activity.maxParticipants){
          this.participantsInQueue ++;
          this.isInQueue = true;
        }else{
          this.currentParticipants ++;
        }
        this.alreadyParticipating = true;
        this.showJoinSpinner = false;
        this.$emit('refresh-list', this.activity.id, true);
      }
    },
    
    async countAccountsInQueue(){
      this.participantsInQueue = await activityButtonService.countAccountsInQueue(this.activity);
    },

    async getQueuePosition(){
      this.queuePosition = await activityButtonService.getQueuePosition(this.activity);

      if(this.queuePosition > 0){
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

    test() {
      console.log(this.activity.title);
    },

    async editActivity() {
      let accountDetails = await userService.getAccountByEmail();

      let activity = {
        id: this.activity.id,
        title: this.title,
        description: this.description,
        latitude: "63.41893", //temporary until map is implemented
        longitude: "10.40658", //temporary until map is implemented
        maxParticipants: this.maxParticipants,
        startTime: `${this.startDate} ${this.startTimeStamp}`,
        endTime: `${this.endDate} ${this.endTimeStamp}`,
        activityType: {
          type: this.type,
        },
        creator: accountDetails,
      };

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
        .then((data) => console.log(data))
        .catch((error) => console.log(error));
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
#icon{
  width: 25px;
  height: 25px;
  margin-right: 10px;
}
#btn{
  height: 50px;
  width: 160px;
  border-radius: 6px;
  font-size: 20px;
  cursor: pointer;
  background-color: #FFBD3E;
  color: white;
  border: 0;
  outline: none;
}
#btn:hover {
  background-color: #eca82b;
  transition: 0.2s;
}

#btn.full{
  background-color: #FF5B3E;
}
#btn.full:hover{
  background-color: #91301f;
  transition: 0.2s;
}
#btn.full:hover span{
  display: none;
}
#btn.full:hover:before{
  content: "Venteliste";
}

#btn.participating{
  background-color: #4a934a;
  transition: 0.2s;
}
#btn.participating:hover{
  background-color: #408140;
  transition: 0.2s;
}
#btn.participating:hover span{
  display: none;
}
#btn.participating:hover:before{
  content: "Meld av";
}
#btn.inQueue{
  background-color: #FF5B3E;
}
#btn.inQueue:hover{
  background-color: #91301f;
  transition: 0.2s;
}
#btn.inQueue:hover span{
  display: none;
}
#btn.inQueue:hover:before{
  content: "Meld av";
}
.queue-list{
  opacity: 70%;
}

.box {
  width: 100%;
}

.edit {
  display: flex;
}

.pencil {
  cursor: pointer;
  z-index: 9;
}

.toggle-edit-button {
  cursor: pointer;
}
</style>
