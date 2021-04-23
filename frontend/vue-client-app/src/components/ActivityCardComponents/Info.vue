<template>
  <div id="infobox" v-if="activity != null">
    <div class="box" id="top">
      <b-icon
        class="toggle-edit-button"
        icon="pencil"
        @click="toggleEditMode"
      ></b-icon>
      <h1 v-show="!inEditMode">{{ activity.title }}</h1>
      <h1 v-show="inEditMode"><input type="text" :placeholder="title" /></h1>
      <div id="ownerInfo">
        <img :src="require('@/assets/kari.jpg')" />
        <h3 class="txt">{{ activity.creator.email }}</h3>
      </div>
      <p class="txt" v-show="!inEditMode">{{ activity.description }}</p>
      <p class="txt" v-show="inEditMode">
        <input type="text" :placeholder="description" />
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
        <li class="txt">Dødens dal</li>
        <!-- Replace this with actual location when implemented -->
        <li class="txt">{{ activity.startTime }}</li>
        <li class="txt">60 minutter</li>
        <!-- Implement calculation for this -->
        <li class="txt" v-if="!activity.weather != null">
          Strålende sol 17 grader
        </li>
        <!-- IMplement this -->
        <li class="txt" v-else>Ingen værmelding</li>
        <li class="txt">{{ 0 }} / {{ activity.maxParticipants }}</li>
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
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            v-model="startTime"
          ></b-form-timepicker>
        </li>
        <li class="txt">
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            v-model="startTime"
          ></b-form-timepicker>
        </li>
        <li class="txt">Legg til utregning</li>
        <!-- Implement calculation for this -->
        <li class="txt" v-if="!activity.weather != null">
          Legg til vær
        </li>
        <!-- Implement this -->
        <li class="txt" v-else>Ingen værmelding</li>
        <li class="txt">
          <input type="text" placeholder="Antall" v-model="maxParticipants" />
        </li>
      </ul>
    </div>
    <!--<div>
                  <img alt="Participant profile picture" v-for="image in images" :key="image.url" :src="image.url">
              </div>-->
    <button
      id="btn"
      :class="{ full: isFull }"
      @click="handleButtonClick()"
      v-show="!inEditMode"
    >
      <span>{{ checkIfFull() }}</span>
    </button>
    <button v-show="inEditMode" @click="editActivity">
      <span>Fullfør</span>
    </button>
  </div>
</template>
<script>
import { userService } from "../../services/UserService";
export default {
  name: "Info",

  props: {
    activity: {
      type: Object,
      required: true,
    },
  },

  data() {
    return {
      inEditMode: false,
      isFull: false,

      title: this.activity.title,
      type: this.activity.activityType.type,
      equipment: "", //TODO: Legg til riktig funksjonalitet
      location: "Trondheim", //TODO: Sjekk om denne skal være med
      date: this.activity.date,
      startTime: this.activity.startTime,
      endTime: "",
      maxParticipants: this.activity.maxParticipants,
      description: this.activity.description,
      participantValue: "",
    };
  },

  methods: {
    checkIfFull() {
      console.log(this.activity);
      if (this.activity.currentParticipants < this.activity.totalParticipants) {
        return "Bli med";
      } else {
        this.isFull = true;
        return "Fullt";
      }
    },

    load() {},

    handleButtonClick() {
      //Open login/register window or add the user to "participants"
    },

    toggleEditMode() {
      this.inEditMode = !this.inEditMode;
    },

    async editActivity() {
      let accountDetails = await userService.getAccountByEmail();

      let activity = {
        title: this.title,
        description: this.description,
        endTime: `${this.date} ${this.endTime}`,
        latitude: "63.41893", //temporary until map is implemented
        longitude: "10.40658", //temporary until map is implemented
        maxParticipants: this.maxParticipants,
        startTime: `${this.date} ${this.startTime}`,
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
    calculateDuration() {
      //TODO: Legg til utregning
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

.toggle-edit-button {
  cursor: pointer;
}
</style>
