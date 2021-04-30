<template>
  <div id="container">
    <div class="filter-sort-container">
      <div class="dropdownlists">
        <div id="category-container">
          <b-form-select
            v-model="selectedCategory"
            :options="categories"
            id="select-category"
            class="picker"
          >
            <template #first>
              <b-form-select-option
                style="display: none"
                disabled
                selected
                value
                >Kategori</b-form-select-option
              >
            </template>
          </b-form-select>
        </div>
        <div id="level-container">
          <b-form-select
            v-model="selectedLevel"
            :options="levels"
            id="select-level"
            class="picker"
          >
            <template #first>
              <b-form-select-option
                style="display: none"
                disabled
                selected
                value
                >Nivå</b-form-select-option
              >
            </template>
          </b-form-select>
        </div>
        <div id="location-container">
          <b-form-select
            v-model="selectedLocation"
            :options="locations"
            id="select-location"
            class="picker"
            data-live-search="true"
          >
            <template #first>
              <b-form-select-option
                style="display: none"
                disabled
                selected
                value
                >Sted</b-form-select-option
              >
            </template>
          </b-form-select>
        </div>
        <div id="button-container">
          <button
            v-show="!isFiltered"
            id="filter-button"
            type="button"
            @click="generateFilteredList()"
          >
            Filtrer
          </button>
          <button
            v-show="isFiltered"
            id="filter-button"
            type="button"
            @click="generateFilteredList()"
          >
            Reset
          </button>
        </div>
        <div id="sorting-container">
          <b-form-select
            v-model="sort"
            :options="sorts"
            @change="sortActivities()"
            id="select-sort"
            class="picker"
          >
            <template #first>
              <b-form-select-option
                style="display: none"
                disabled
                selected
                :value="null"
                >Sorter</b-form-select-option
              >
            </template>
          </b-form-select>
        </div>
      </div>
    </div>
    <div id="feed">
      <div
        id="feedloop"
        v-for="a in filteredActivities"
        :key="a.id"
        @click="handleActivityClicked(a)"
      >
        <Activity
          :activity="a"
          v-on:refresh-list="refreshList"
          :isLoggedIn="isLoggedIn"
          v-on:currentParticipantsFound="findCurrentParticipants"
        />
      </div>
    </div>
    <div id="calendar-mini">
      <p class="header">Dine aktiviteter</p>
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
import Activity from "./Activity.vue";
import { userService } from "../../services/UserService.js";
import { weatherService } from "../../services/WeatherService.js";

export default {
  name: "ActivityFeed",

  components: {
    Activity,
  },

  data() {
    return {
      //WeatherService: require('../../services/WeatherService.js'),
      selectedActivity: null,
      activities: [],
      allCategories: {},
      joinedActivities: {},
      currentParticipantsAll: [],
      sortKey: "",
      isLoggedIn: false,
      filteredActivities: [],
      sortedActivities: [],
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
      isFiltered: false,

      sorts: [
        { value: 1, text: "Navn A-Å" },
        { value: 2, text: "Navn Å-A" },
        { value: 3, text: "Tidspunkt tidlig-senest" },
        { value: 4, text: "Tidspunkt senest-tidlig" },
        { value: 5, text: "Varighet høy-lav" },
        { value: 6, text: "Varighet lav-høy" },
        { value: 7, text: "Ledige plasser høy-lav" },
        { value: 8, text: "Ledige plasser lav-høy" },
        { value: 9, text: "Antall påmeldte høy-lay" },
        { value: 10, text: "Antall påmeldte lav-høy" },
        { value: 11, text: "Nivå høy-lav" },
        { value: 12, text: "Nivå lav-høy" },
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
          let list = data.filter((a) => new Date(a.startTime) >= Date.now());
          this.activities = list.sort((x, y) => {
            return new Date(x.startTime) - new Date(y.startTime);
          });
        })
        .catch((error) => console.log(error));
      for (let i = 0; i < this.activities.length; i++) {
        this.locations.push({
          value: this.activities[i].location,
          text: this.activities[i].location,
          id: i + 1,
        });
      }
      for (let j = 0; j < this.locations.length; j++) {
        console.log(this.locations[j].value);
      }
    },

    async getCategories() {
      const requestOptions = {
        method: "GET",
      };

      await fetch("http://localhost:8080/activityTypes/", requestOptions)
        .then((response) => response.json())
        .then((data) => {
          for (let i = 0; i < data.length; i++) {
            this.categories.push({
              value: data[i].type,
              text: data[i].type,
            });
          }
        })
        .catch((error) => console.log(error));
    },

    async getLevels() {
      const requestOptions = {
        method: "GET",
      };

      await fetch("http://localhost:8080/levels/", requestOptions)
        .then((response) => response.json())
        .then((data) => {
          for (let i = 0; i < data.length; i++) {
            console.log(data[i].description);
            this.levels.push({
              value: data[i].description,
              text: data[i].description,
            });
          }
        })
        .catch((error) => console.log(error));
    },

    filterByCategory(list) {
      const filteredList = [];

      if (this.selectedCategory !== "Kategori") {
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].activityType.type.toLowerCase() ===
            this.selectedCategory.toLowerCase()
          ) {
            filteredList.push(list[i]);
          }
        }
        return filteredList;
      } else {
        return list;
      }
    },

    filterByLevel(list) {
      const filteredList = [];

      if (this.selectedLevel !== "Nivå") {
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].level.description.toLowerCase() ===
            this.selectedLevel.toLowerCase()
          ) {
            filteredList.push(list[i]);
          }
        }
        return filteredList;
      } else {
        return list;
      }
    },

    filterByLocation(list) {
      const filteredList = [];

      if (this.selectedLocation !== "Sted") {
        for (let i = 0; i < list.length; i++) {
          if (
            list[i].location.toLowerCase() ===
            this.selectedLocation.toLowerCase()
          ) {
            filteredList.push(list[i]);
          }
        }
        return filteredList;
      } else {
        return list;
      }
    },

    generateFilteredList() {
      console.log(
        this.selectedCategory + "SELECTED CATEGORY WHEN BUTTON CLICKED"
      );
      console.log(this.selectedLevel + "SELECTED LEVEL WHEN BUTTON CLICKED");
      console.log(
        this.selectedLocation + "SELECTED LOCATION WHEN BUTTON CLICKED"
      );

      let list = this.activities;

      if (this.selectedCategory !== "") {
        list = this.filterByCategory(list);
      }
      if (this.selectedLevel !== "") {
        list = this.filterByLevel(list);
      }
      if (this.selectedLocation !== "") {
        list = this.filterByLocation(list);
      }

      this.filteredActivities = list;
      this.selectedCategory = "";
      this.selectedLevel = "";
      this.selectedLocation = "";
      this.isFiltered = !this.isFiltered;
    },

    findCurrentParticipants(activityId, currentParticipants) {
      this.currentParticipantsAll.push({
        id: activityId,
        currentParticipants: currentParticipants,
      });
    },

    sortActivities() {
      switch (this.sort) {
        case 1: {
          this.sortedActivities = this.sortByNameAsc();
          break;
        }
        case 2: {
          this.sortedActivities = this.sortByNameDesc();
          break;
        }
        case 3: {
          this.sortedActivities = this.sortByTimeEarliest();
          break;
        }
        case 4: {
          this.sortedActivities = this.sortByTimeLatest();
          break;
        }
        case 5: {
          this.sortedActivities = this.sortByDurationDesc();
          break;
        }
        case 6: {
          this.sortedActivities = this.sortByDurationAsc();
          break;
        }
        case 7: {
          this.sortedActivities = this.sortByFreeSpotsDesc();
          break;
        }
        case 8: {
          this.sortedActivities = this.sortByFreeSpotsAsc();
          break;
        }
        case 9: {
          this.sortedActivities = this.sortByCurrentParticipantsDesc();
          break;
        }
        case 10: {
          this.sortedActivities = this.sortByCurrentParticipantsAsc();
          break;
        }
        case 11: {
          this.sortedActivities = this.sortByLevelDesc();
          break;
        }
        case 12: {
          this.sortedActivities = this.sortByLevelAsc();
          break;
        }
      }
    },

    sortByNameAsc() {
      console.log(">> sortByNameAsc() called");
      return this.filteredActivities.sort((x, y) => {
        if (x.title < y.title) return -1;
        else if (x.title > y.title) return 1;
        return 0;
      });
    },

    sortByNameDesc() {
      console.log(">> sortByNameDesc() called");
      return this.filteredActivities.sort((x, y) => {
        if (x.title < y.title) return 1;
        else if (x.title > y.title) return -1;
        return 0;
      });
    },

    sortByTimeEarliest() {
      console.log(">> sortByTimeEarliest() called");
      return this.filteredActivities.sort((x, y) => {
        return new Date(x.startTime) - new Date(y.startTime);
      });
    },

    sortByTimeLatest() {
      console.log(">> sortByTimeLatest() called");
      return this.filteredActivities.sort((x, y) => {
        return new Date(y.startTime) - new Date(x.startTime);
      });
    },

    sortByDurationDesc() {
      console.log(">> sortByDurationDesc() called");
      return this.filteredActivities.sort((x, y) => {
        let d1 = new Date(y.endTime) - new Date(y.startTime);
        let d2 = new Date(x.endTime) - new Date(x.startTime);
        return d1 - d2;
      });
    },

    sortByDurationAsc() {
      console.log(">> sortByDurationAsc() called");
      return this.filteredActivities.sort((x, y) => {
        let d1 = new Date(x.endTime) - new Date(x.startTime);
        let d2 = new Date(y.endTime) - new Date(y.startTime);
        return d1 - d2;
      });
    },

    sortByFreeSpotsDesc() {
      console.log(">> sortByFreeSpotsDesc() called");
      return this.filteredActivities.sort((x, y) => {
        let current1 = this.currentParticipantsAll.find((a) => a.id === y.id)
          .currentParticipants;
        let current2 = this.currentParticipantsAll.find((a) => a.id === x.id)
          .currentParticipants;
        let free1 = y.maxParticipants - current1;
        let free2 = x.maxParticipants - current2;
        return free1 - free2;
      });
    },

    sortByFreeSpotsAsc() {
      console.log(">> sortByFreeSpotsAsc() called");
      return this.filteredActivities.sort((x, y) => {
        let current1 = this.currentParticipantsAll.find((a) => a.id === x.id)
          .currentParticipants;
        let current2 = this.currentParticipantsAll.find((a) => a.id === y.id)
          .currentParticipants;
        let free1 = x.maxParticipants - current1;
        let free2 = y.maxParticipants - current2;
        return free1 - free2;
      });
    },

    sortByCurrentParticipantsDesc() {
      console.log(">> sortByCurrentParticipantsDesc() called");
      return this.filteredActivities.sort((x, y) => {
        let current1 = this.currentParticipantsAll.find((a) => a.id === y.id)
          .currentParticipants;
        let current2 = this.currentParticipantsAll.find((a) => a.id === x.id)
          .currentParticipants;
        return current1 - current2;
      });
    },

    sortByCurrentParticipantsAsc() {
      console.log(">> sortByCurrentParticipantsAsc() called");
      return this.filteredActivities.sort((x, y) => {
        let current1 = this.currentParticipantsAll.find((a) => a.id === x.id)
          .currentParticipants;
        let current2 = this.currentParticipantsAll.find((a) => a.id === y.id)
          .currentParticipants;
        return current1 - current2;
      });
    },

    sortByLevelDesc() {
      console.log(">> sortByLevelDesc() called");
      return this.filteredActivities.sort((x, y) => {
        return y.level.id - x.level.id;
      });
    },

    sortByLevelAsc() {
      console.log(">> sortByLevelAsc() called");
      return this.filteredActivities.sort((x, y) => {
        return x.level.id - y.level.id;
      });
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
          let sorted = data.sort(
            (x, y) => new Date(x.startTime) - new Date(y.startTime)
          );
          this.joinedActivities = sorted;
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
<style scoped>
#container {
  background-color: #f6f6f6;
  position: relative;
  font-family: "Mulish";
  min-height: 100vh;
}
#feed {
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
#feedloop {
  margin-top: 50px;
}
#calendar-mini {
  position: fixed;
  width: 340px;
  height: 625px;
  top: 150px;
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
  width: 100vw;
  height: 60px;
  display: flex;
  margin-top: 72px;
  z-index: 100;
  position: fixed;
  background-color: #f6f6f6;
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
}

.dropdownlists {
  display: flex;
  padding-left: 60px;
  align-items: center;
}

.picker {
  width: 130px;
  height: 35px;
  cursor: pointer;
}

#category-container {
  display: inline-block;
  padding-right: 10px;
  padding-left: 10px;
}

#level-container {
  display: inline-block;
  padding-right: 10px;
  padding-left: 10px;
  max-width: 150px;
  min-width: 129px;
  height: 35px;
  cursor: pointer;
}

#location-container {
  max-width: 140px;
  min-width: 126px;
  padding-left: 10px;
  padding-right: 10px;
  height: 35px;
  cursor: pointer;
}

#location-picker {
  width: 100%;
  height: 100%;
  padding-left: 10px;
  border-radius: 4px;
  width: 130px;
  height: 35px;
  cursor: pointer;
}

#button-container {
  display: flex;
  align-items: center;
  padding-left: 10px;
}

#filter-button {
  background-color: #ffbd3e;
  color: white;
  border: none;
  border-radius: 6px;
  width: 60px;
  height: 35px;
  text-align: center;
}

#filter-button:hover {
  background-color: #eca82b;
  transition: 0.2s;
}

#sorting-container {
  display: inline-block;
  margin-left: 335px;
  width: 130px;
  height: 35px;
  cursor: pointer;
}

@media (max-width: 1200px) {
  #calendar-mini {
    display: none;
  }

  #feed {
    width: 100vw;
  }

  .dropdownlists {
    display: flex;
    flex-direction: row;
    margin-left: -50px;
  }

  .picker {
    width: 70px;
    margin-left: -10px;
  }

  #select-level {
    margin-left: -17px;
  }

  #select-location {
    margin-left: -73px;
  }

  #sorting-container {
    margin-left: -40px;
  }

  #select-sort {
    width: 80px;
  }

  #filter-button {
    width: 60px;
    margin-left: -115px;
  }
}
</style>
