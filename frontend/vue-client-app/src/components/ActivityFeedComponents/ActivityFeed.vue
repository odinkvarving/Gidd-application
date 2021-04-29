<template>
  <div id="container">
    <div id="feed">
      <div class="filter-sort-container">
        <div class="category-container">
            <b-form-select v-model="category" :options="categories" id="select-category" class="category-picker">
                <template #first>
                    <b-form-select-option :value="null">Kategori</b-form-select-option>
                </template>
            </b-form-select>
        </div>
        <div class="level-container">
            <b-form-select  v-model="level" :options="levels" id="select-level" class="level-picker">
                <template #first>
                    <b-form-select-option :value="null">Nivå</b-form-select-option>
                </template>
            </b-form-select>
        </div>
        <div class="location-container">
            <select class="location-picker" id="select-location" data-live-search="true">  
                <option data-tokens="Sted">Sted</option>
                <option v-for="item in locations" :key="item.id" data-tokens="" >{{item.value}}</option>                                    
            </select>
        </div>
        <div class="button-container">
            <button class="filter-button" type="button" @click="generateFilteredList()" >filtrer</button> 
        </div>       
        <div class="sorting-container">
            <b-form-select v-model="sort" :options="sorts" @change="modifyActivities()" class="sorting-picker">
                <template #first>
                    <b-form-select-option :value="null">Sorter</b-form-select-option>
                </template>
            </b-form-select>
        </div>
      </div>
      <div
        v-for="a in filteredActivities"
        :key="a.id"
        @click="handleActivityClicked(a)"
      >
        <Activity
          :activity="a"
          v-on:refresh-list="refreshList"
          :isLoggedIn="isLoggedIn"
        />
      </div>
    </div>
    <div id="calendar-mini">
      <p class="header">Kommende aktiviteter</p>
      <div class="horizontal-line" />
      <div id="coming-activities">
        <!-- TODO: refresh joined activity list when activity is joined or removed -->
        <div v-for="a in joinedActivities" :key="a.id" style="width: 100%;">
          <div
            @click="joinedActivityClicked(a.id)"
            class="joined-activity-container"
          >
            <div class="circle" />
            <p class="title">{{ a.title }}</p>
            <div class="time">
              <p class="date">{{ getDayAndMonth(a) }}</p>
              <p class="clock">{{ getClockTime(a) }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>

document.addEventListener('DOMContentLoaded', function () {
   var input = document.getElementById('select-category');
   if (localStorage['select-category']) { // if job is set
       input.value = localStorage['select-category']; // set the value
       this.selectedCategory = input.value;
   }
   input.onchange = function () {
        localStorage['select-category'] = this.value; // change localStorage on change
        this.selectedCategory = this.value;
        console.log("Selected Category: " + this.selectedCategory)
        //MAKE EVENT LISTENER CALL METHOD INSTEAD OF JUST UPDATING VARIABLE?
        //generateFilteredList(this.value)?
        //NO NEED FOR BUTTON?
        //NOTE: MIGHT NOT WORK BECAUSE YOU NEED TO CHECK ALL 3 FILTERS(!)
    }
});



document.addEventListener('DOMContentLoaded', function () {
   var input = document.getElementById('select-level');
   if (localStorage['select-level']) { // if job is set
       input.value = localStorage['select-level']; // set the value
       this.selectedLevel = input.value;
   }
   input.onchange = function () {
        localStorage['select-level'] = this.value; // change localStorage on change
        this.selectedLevel = this.value;
        console.log("Selected Level: " + this.selectedLevel)
    }
});

document.addEventListener('DOMContentLoaded', function () {
   var input = document.getElementById('select-location');
   if (localStorage['select-location']) { // if job is set
       input.value = localStorage['select-location']; // set the value
       this.selectedLocation = input.value;
   }
   input.onchange = function () {
        localStorage['select-location'] = this.value; // change localStorage on change
        this.selectedLocation = this.value;
        console.log("Selected Location: " + this.selectedLocation)
    }
});

import Activity from "./Activity.vue";
import { userService } from "../../services/UserService.js";
import { weatherService } from "../../services/WeatherService.js";
import { activityButtonService } from '../../services/ActivityButtonService';

export default {
  name: "ActivityFeed",

  components: {
    Activity,
  },

  data() {
    return {
      //WeatherService: require('../../services/WeatherService.js'),
      selectedActivity: null,
      activities: {},
      allCategories: {},
      joinedActivities: {},
      currentParticipants: 0,
      sortKey: "",
      isLoggedIn: false,
      filteredActivities: [],
      category: null,
      level: null,
      location: null,
      sort: null,
      newList: [],
      categories: [],
      levels: [],
      locations: [],
      selectedCategory: "",
      selectedLevel: "",
      selectedLocation: "",

      sorts: [
          { value: 1, text: "Ledige plasser høy-lav"},
          { value: 2, text: "Ledige plasser lav-høy"},
          { value: 3, text: "Antall påmeldte høy-lay"},
          { value: 4, text: "Antall påmeldte lav-høy"},
      ],
      filterKey: "",
    };
  },
  async mounted() {
    await this.getActivities();
    await this.getCategories();
    await this.getLevels();
    if (userService.isLoggedIn()) {
      this.isLoggedIn = true;
      await this.getJoinedActivities();
    }
    this.filteredActivities = this.activities;
  },
  methods: {

    async getActivities() {
      const requestOptions = {
        method: "GET",
      };

      // Get all registered activites from database
      await fetch("http://localhost:8080/activities/", requestOptions)
        .then((response) => response.json())
        .then((data) => {
          this.activities = data;
          console.log(data[0]);
        })
        .catch((error) => console.log(error));
        for(let i = 0; i < this.activities.length; i++) {
            this.locations.push({
                value: this.activities[i].location,
                text: this.activities[i].location,
                id: (i+1),
            })
        }
        for(let j = 0; j < this.locations.length; j++) {
            console.log(this.locations[j].value)
        }
    },

    async getCategories() {
        const requestOptions = {
            method: "GET"
        };

        await fetch("http://localhost:8080/activityTypes/", requestOptions)
            .then((response) => response.json())
            .then((data) => {
                for(let i = 0; i < data.length; i++) {
                    console.log(data[i].type)
                    this.categories.push({
                        value: data[i].type,
                        text: data[i].type
                    })
                }
                console.log(data[0]);
            })
            .catch((error) => console.log(error)); 
    },

    async getLevels() {
        const requestOptions = {
            method: "GET"
        };

        await fetch("http://localhost:8080/levels/", requestOptions)
            .then((response) => response.json())
            .then((data) => {
                for(let i = 0; i < data.length; i++) {
                    console.log(data[i].description);
                    this.levels.push({
                        value: data[i].description,
                        text: data[i].description
                    })
                }
                console.log(data[0]);
            })
            .catch((error) => console.log(error));
    },

    filterByCategory(list) {
        
        const filteredList = [];

        if(this.selectedCategory !== "Kategori") {
            for(let i = 0; i < list.length; i++) {
                if(list[i].activityType.type.toLowerCase() === this.selectedCategory.toLowerCase()) {
                    filteredList.push(list[i]);
                }
            }
            return filteredList;
        }else {
            return list;
        }
    },

    filterByLevel(list) {

        const filteredList = [];

        if(this.selectedLevel !== "Nivå") {
            for(let i = 0; i < list.length; i++) {
                if(list[i].level.description.toLowerCase() === this.selectedLevel.toLowerCase()) {
                    filteredList.push(list[i]);
                }
            }
            return filteredList;
        }else {
            return list;
        }
    },

    filterByLocation(list) {

        const filteredList = [];

        if(this.selectedLocation !== "Sted") {
            for(let i = 0; i < list.length; i++) {
                if(list[i].location.toLowerCase() === this.selectedLocation.toLowerCase()) {
                    filteredList.push(list[i]);
                }
            }
            return filteredList;
        }else {
            return list;
        }
    },

    async generateFilteredList() {
        //TRIED PASSING ALL FILTER PARAMETERS IN BUTTON METHOD - DID NOT WORK
        console.log(this.selectedCategory + "SELECTED CATEGORY WHEN BUTTON CLICKED")
        let list = this.activities;

        if(this.selectedCategory !== "") {
            list = this.filterByCategory(list);
        }
        if(this.selectedLevel !== "") {
            list = this.filterByLevel(list);
        }
        if(location !== "") {
            list = this.filterByLocation(list, location);
        }

        this.filteredActivities = list;
    },

    sortActivities(filteredList) {
        //Returnerer dette filteredList(sortert)?
        if(this.sort === 1) {
            filteredList.sort(function(a, b) {
                return (b.maxParticipants - activityButtonService.getCurrentParticipantsNumber(b)) - (a.maxParticipants - activityButtonService.getCurrentParticipantsNumber(a));
            });
        }else if(this.sort === 2) {
            filteredList.sort(function(a, b) {
                return (a.maxParticipants - activityButtonService.getCurrentParticipantsNumber(a)) - (b.maxParticipants - activityButtonService.getCurrentParticipantsNumber(b));
            });
        }else if(this.sort === 3) {
            filteredList.sort(function(a, b) {
                return (activityButtonService.getCurrentParticipantsNumber(b)) - (activityButtonService.getCurrentParticipantsNumber(a))
            });
        }else if(this.sort === 4) {
            filteredList.sort(function(a, b) {
                return (activityButtonService.getCurrentParticipantsNumber(a)) - (activityButtonService.getCurrentParticipantsNumber(b))
            });
        }else {
            return filteredList;
        }
    },

    async getJoinedActivities() {
      let accountId;
      await userService
        .getAccountByEmail()
        .then((data) => (accountId = data.id));

      let url = `http://localhost:8080/accounts/${accountId}/activities/`;

      const requestOptions = {
        method: "GET",
        headers: userService.authorizationHeader(),
      };

      // Get all registered activites from database
      fetch(url, requestOptions)
        .then((response) => response.json())
        .then((data) => {
          console.log(`Joined activities:`);
          console.log(data);
          this.joinedActivities = data;
        })
        .catch((error) => console.log(error));
    },
    joinedActivityClicked(activityId) {
      console.log("clicked");
      this.$router.push({ name: "Activity", params: { id: activityId } });
    },
    async getWeather() {
      return await weatherService.getWeather(
        this.latitude,
        this.longitude,
        this.time
      );
    },

    handleActivityClicked(activity) {
      this.activity = this.selectedActivity = activity;
      console.log(this.selectedActivity.name);
      this.$emit("activityClicked", this.selectedActivity);
      this.$router.push({ name: "Activity", params: { id: activity.id } });
    },
    getDayAndMonth(activity) {
      // Hente ut dag og månede fra tidspunkt start
      let date = activity.startTime.split(" ")[0].split("-");
      return `${date[2]}.${date[1]}`;
    },
    getClockTime(activity) {
      // Hente ut klokkeslett
      let time = activity.startTime.split(" ")[1].split(":");
      return `${time[0]}:${time[1]}`;
    },
    refreshList(activityId, add) {
      let index;

      for (let i = 0; i < this.joinedActivities.length; i++) {
        if (this.joinedActivities[i].id === activityId) {
          index = i;
        }
      }

      if (!add) {
        this.joinedActivities.splice(index, 1);
      } else {
        for (let i = 0; i < this.activities.length; i++) {
          if (this.activities[i].id === activityId) {
            this.joinedActivities.push(this.activities[i]);
          }
        }
      }
      this.sortList();
    },
    sortList() {
      // TODO: Sort list when adding new activity
    },
  },
};
</script>
<style>
#container {
  background-color: #f6f6f6;
  position: relative;
  font-family: "Mulish";
  min-height: 100vh;
}
#feed {
  margin-left: 0;
  width: 70vw;
  display: flex;
  flex-flow: row wrap;
  align-items: center;
  justify-content: center;
  background-color: #f6f6f6;
  padding: 100px 0;
  /*display: flex;
        flex-direction: column;
        flex-wrap: wrap;
        justify-content: center;
        align-items: center;*/
}
#calendar-mini {
  position: fixed;
  width: 340px;
  height: 625px;
  top: 200px;
  right: 100px;
  background: white;
  display: flex;
  flex-flow: column nowrap;
  align-items: center;
}

#calendar-mini .header {
  font-size: 25px;
  opacity: 80%;
  margin-top: 15px;
}

#calendar-mini .horizontal-line {
  width: 85%;
  opacity: 40%;
  height: 1px;
  background-color: black;
}

.joined-activity-container {
  width: 300px;
  display: flex;
  align-items: center;
  justify-content: space-around;
  margin-top: 20px;
  padding-left: 10px;
  transition: 0.3s;
}

.joined-activity-container:hover {
  cursor: pointer;
  background-color: rgb(236, 235, 235);
  transition: 0.3s;
}

.joined-activity-container p {
  margin: 0;
}

.joined-activity-container .circle {
  width: 10px;
  height: 10px;
  background-color: #ffbd3e;
  border-radius: 100%;
}

.joined-activity-container .title {
  width: 210px;
  text-align: center;
}

.joined-activity-container .time {
  width: 80px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.joined-activity-container .time .date {
  font-size: 20px;
  margin: 0;
}

.joined-activity-container .time .clock {
  font-size: 14px;
  margin: 0;
}

.filter-sort-container {
    width: 100%;
    display: flex;
    padding-bottom: 20px;
    padding-left: 55px;
}

.category-container {
  display: inline-block;
  padding-right: 10px;
  padding-left: 10px;
}

.category-picker {
    width: 130px;
}

.level-container {
    display: inline-block;
    padding-right: 10px;
    padding-left: 10px;
}

.level-picker {
    width: 130px;
}

.location-container {
    width: 150px;
    padding-left: 10px;
    padding-right: 10px;
    padding-bottom: 3px;
    padding-top: 1px;
}

.location-picker {
    width: 100%;
    height: 100%;
    color: #495057;
    border: 1px solid #ced4da;
    padding-left: 10px;
    padding-bottom: 2px;
    border-radius: 4px;
}

.button-container {
    padding-top: 5px;
    padding-bottom: 5px;
    padding-left: 1px;
}

.filter-button {
    padding-top: 5px;
    background-color: #ffbd3e;
    color: #495057;
    border: 1px solid #ced4da;
    border-radius: 10%;
    font-family:Cambria, Cochin, Georgia, Times, 'Times New Roman', serif;
}

.sorting-container {
  display: inline-block;
  padding-right: 10px;
  padding-left: 265px;
}

.sorting-picker {
    width: 100%;
    background: #6C757D;
}

</style>
