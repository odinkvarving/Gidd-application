<template>
  <div id="infobox" v-if="activity">
    <div class="box" id="top">
      <b-icon
        class="toggle-edit-button"
        icon="pencil"
        @click="toggleEditMode"
      ></b-icon>

      <h1 v-show="!inEditMode">{{ activity.title }}</h1>
      <h1 v-show="inEditMode">
        <input type="text" :placeholder="title" v-model="title" />
      </h1>
      <div id="ownerInfo">
        <img :src="require('@/assets/kari.jpg')" />
        <h3 class="txt">{{ activity.creator.email }}</h3>
      </div>
      <p class="txt" v-show="!inEditMode">{{ activity.description }}</p>
      <p class="txt" v-show="inEditMode">
        <input type="text" :placeholder="description" v-model="description" />
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
        <li class="txt">Legg til lokasjon</li>
        <li class="txt">{{ activity.startTime }}</li>
        <li class="txt">60 minutter</li>
        <!-- Implement calculation for this -->
        <li class="txt">
          Legg til vær
        </li>
        <li class="txt">{{ 0 }} / {{ activity.maxParticipants }}</li>
      </ul>

      <ul class="list" id="list2" v-show="inEditMode">
        <li class="txt">
          <input type="text" :placeholder="type" v-model="type" />
        </li>
        <li class="txt">
          <LocationSearchBar />
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
        <li class="txt">Legg til utregning</li>
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
    <button v-show="inEditMode" @click="onClickSaveButton">
      <span>Lagre</span>
    </button>
  </div>
</template>

<script>
import { userService } from "../../services/UserService";
import moment from "moment";
import LocationSearchBar from "../createActivityComponents/LocationSearchBar.vue";

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
  },

  data() {
    return {
      inEditMode: false,
      isFull: false,

      title: this.activity.title,
      type: this.activity.activityType.type,
      location: "Trondheim", //TODO: Sjekk om denne skal være med
      startDate: "",
      startTimeStamp: "",
      endDate: "",
      endTimeStamp: "",
      maxParticipants: 10,
      description: this.activity.description,
    };
  },

  mounted() {
    this.getCategories();
    this.getLevels();
  },

  methods: {
    toggleEditMode() {
      this.inEditMode = !this.inEditMode;
      console.log("Edit Mode: " + this.inEditMode);
    },

    onClickSaveButton() {
      this.name === "" ? (this.nameState = false) : (this.nameState = true);
      this.category === null
        ? (this.categoryState = false)
        : (this.categoryState = true);
      this.level === null
        ? (this.levelState = false)
        : (this.levelState = true);
      this.location === "" || this.placeState === null
        ? (this.placeState = false)
        : (this.placeState = true);
      this.validStartAndEndDate();
      this.description === ""
        ? (this.descriptionState = false)
        : (this.descriptionState = true);

      if (
        this.nameState === true &&
        this.categoryState === true &&
        this.levelState === true &&
        this.placeState === true &&
        this.startDateState === true &&
        this.endDateState === true &&
        this.descriptionState === true
      ) {
        this.editActivity();
        console.log("Activity updated!");
      }
    },

    async editActivity() {
      let accountDetails = await userService.getAccountByEmail();

      let activity = {
        id: this.activity.id,
        title: this.title,
        description: this.description,
        equipment: this.activity.equipment,
        location: this.
        latitude: "63.41893", //temporary until map is implemented
        longitude: "10.40658", //temporary until map is implemented
        maxParticipants: this.maxParticipants,
        startTime: `${this.startDate} ${this.startTimeStamp}`,
        endTime: `${this.endDate} ${this.endTimeStamp}`,
        activityType: {
          type: this.type,
        },
        level: {
          description: this.level
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
