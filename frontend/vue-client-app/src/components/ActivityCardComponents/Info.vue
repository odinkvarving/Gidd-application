<template>
    <div id="infobox" v-if="activity != null">
        <div class="box" id="top">
            <h1>{{ activity.title }}</h1> <!-- Title of the activity -->
            <div id="ownerInfo">
                <img :src="require('@/assets/kari.jpg')"/> <!-- Profile picture of creator -->
                <h3 class="txt">{{ activity.creator.email }}</h3>
                <!-- <h3 class="txt">{{ activity.creator.accountInfo.firstName }} {{ activity.creator.accountInfo.surName }}</h3> -->
            </div>
            <p class="txt">{{ activity.description }}</p> <!-- Description of the activity -->
        </div>
        <div class="box" id="bottom"> <!-- Lists of other information about the activity -->
            <h3>Informasjon:</h3>
            <ul class="list" id="list1">
                <li class="txt">Kategori:</li>
                <li class="txt">Sted:</li>
                <li class="txt">Tid:</li>
                <li class="txt">Varighet:</li>
                <li class="txt">Værmelding:</li>
                <li class="txt">Deltakere:</li>
            </ul>
            <ul class="list" id="list2">
                <li class="txt">{{ activity.activityType.type }}</li>
                <li class="txt">Dødens dal</li><!-- Replace this with actual location when implemented -->
                <li class="txt">{{ activity.startTime }}</li>
                <li class="txt">60 minutter</li><!-- Implement calculation for this -->
                <li class="txt" v-if="!activity.weather != null">Sol</li><!-- {{ weather.temp }} IMplement this -->
                <li class="txt" v-else>Ingen værmelding</li>
                <li class="txt">{{ 0 }} / {{ activity.maxParticipants }}</li>
            </ul>
        </div>
        <!--<div>
            <img alt="Participant profile picture" v-for="image in images" :key="image.url" :src="image.url">
        </div>-->
        <button id="btn" :class="{ full: isFull }" @click="handleButtonClick()">
          <span>{{ checkIfFull() }}</span>
        </button>
  </div>
</template>
<script>
  //import {weatherService} from '../../services/WeatherService.js'

  /**
   * Info is a component which represents general info about an activity
   * It is used in ActivityCard
   * 
   * @author Scott Rydberg Sonen
   */
  export default {
    name: "Info",

    props: {
      /**
       * activity is a prop passed from ActivityCard to Info.
       * The prop represent an activity found in ActivityCard by ID from ActivityFeed
       */
      activity: {
        type: Object,
        required: true
      },
      location: {
        type: Object,
        required: true
      },
      weather: {
        type: Object,
        required: true
      },
    },

    data() {
      return {
        /**
         * isFull is a boolean which represent the state an activity and its current participants.
         * If isFull is true, it means that the number of participants has reach a max limit.
         * After isFull changes to true, every participant which tries to join will be put in a queue.
         */
        isFull: false,
        /**
         * location is a string which represents the place name of the activity
         */
        //location: {},
        /**
         * weather is an object which represents the weather of the activity.
         * It contains relevant parameters: name, temp, description and icon.
         */
        //weather: {},
        /**
         * isDataReady is a boolean which represents the state of location and weather.
         * Since we acquire these variables through asynchronous calls, we need to have a flag which tells us when the data is ready.
         */
        isDataReady: false,
      }
    },
  
    /*async mounted() {
      this.location = await this.findLocation();
      this.weather = await this.getWeather();
      this.isDataReady = true;
    },*/

    methods: {
      /**
       * checkIfFull is a function which checks if currentParticipants is bigger than max limit.
       * If it is, the isFull-state changes to true.
       */
      checkIfFull() {
          console.log(this.activity);
          if (this.activity.currentParticipants < this.activity.totalParticipants) { 
              return "Bli med";
          } else {
              this.isFull = true;
              return "Fullt";
          }
      },

      /**
       * findLocation is an asynchronous function which returns location of the activity.
       * getWeather function is utilized, and we find the location by using the weather object.
       */
      /*async findLocation() {
          return await this.getWeather().name;
      },*/
        
      /**
       * getWeather is an asynchronous function which returns weather of the activity.
       * The weather object is fetched in services/WeatherService.
       * To find the weather, WeatherService requires latitude, longitude and startTime of activity.
       */
      /*async getWeather() {
        console.log(this.activity);
        console.log("LATITUDE: " + this.activity.latitude);
        console.log("LONGITUDE: " + this.activity.longitude);
        console.log("STARTTIME: " + this.activity.startTime);
        let w = await weatherService.getWeather(this.activity.latitude, this.activity.longitude, this.activity.startTime);
        console.log(w.name);
        return w;
          //return await weatherService.getWeather(this.activity.latitude, this.activity.longitude, this.activity.startTime);
      },*/

      /**
       * handleButtonClick is an event handler for the button in the component.
       * A PUT request will be sent to the Spring Boot server,
       * and the server will check if there is space left for the user to join.
       */
      handleButtonClick() {
          //Open login/register window or add the user to "participants"
      },
    }
  }
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
#btn {
  grid-area: btn;
  height: 5vh;
  width: 8vw;
  border-radius: 6px;
  font-size: 20px;
  cursor: pointer;
  background-color: #ffbd3e;
  color: white;
  border: 0;
  margin: auto;
  outline: none;
}
#btn.full {
  background-color: #ff5b3e;
}
#btn.full:hover span {
  display: none;
}
#btn.full:hover:before {
  content: "Venteliste";
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
</style>
