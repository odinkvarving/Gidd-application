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
        <div v-for="a in joinedActivities" :key="a.id" style="width: 100%">
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
/**
 * Necessary components and services containing useful functions are imported.
 */
import Activity from "./Activity.vue";
import { userService } from "../../services/UserService.js";

/**
 * ActivityFeed is a component which displays all activities which are not expired.
 * The component also displays filter and sort functionality,
 *  and an overview of each activity the account has participated in or will participate in.
 *
 * @author Scott Rydberg Sonen
 * @author Odin Kvarving
 * @author Magnus Bredeli
 */
export default {
  name: "ActivityFeed",

  components: {
    Activity,
  },

  data() {
    return {
      /**
       * selectedActivity represents the clicked activity.
       */
      selectedActivity: null,
      /**
       * activities is an array containing all activities after current time,
       *  sorted by earliest start time.
       */
      activities: [],
      /**
       * joinedActivities is an array containing all activities an account is participating in.
       */
      joinedActivities: [],
      /**
       * currentParticipantsAll is an array containing all current participants in the activity.
       */
      currentParticipantsAll: [],
      /**
       * isLoggedIn tells us if the account is logged in.
       */
      isLoggedIn: false,
      /**
       * filteredActivities is an array containg activities after filtering by parameters.
       */
      filteredActivities: [],
      /**
       * sortedActivities is an array containing filteredActivities after sorting by sort parameter.
       */
      sortedActivities: [],
      /**
       * sort is a sort key which represents a sorting method.
       */
      sort: null,
      /**
       * categories is an array containing all categories from database.
       */
      categories: [],
      /**
       * levels is an array containing all levels from database.
       */
      levels: [],
      /**
       * locations is an array containing all locations from all activities.
       */
      locations: [],
      /**
       * selectedCategory represents the category filter.
       */
      selectedCategory: "",
      /**
       * selectedLevel represents the level filter.
       */
      selectedLevel: "",
      /**
       * selectedLocation represents the location filter.
       */
      selectedLocation: "",
      /**
       * isFiltered tells us if account has filtered or not.
       */
      isFiltered: false,
      /**
       * sorts is an array containing all sorting methods.
       */
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
    /**
     * getActivities is a function which returns all activities from database.
     * A GET request is sent to backend.
     */
    async getActivities() {
      const requestOptions = {
        method: "GET",
      };

      // Get all registered activities from database
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
    },

    /**
     * getCategories is a function which returns all categories from database.
     * A GET request is sent to backend.
     */
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

    /**
     * getLevels is a function which returns all levels from database.
     * A GET request is sent to backend.
     */
    async getLevels() {
      const requestOptions = {
        method: "GET",
      };

      await fetch("http://localhost:8080/levels/", requestOptions)
        .then((response) => response.json())
        .then((data) => {
          for (let i = 0; i < data.length; i++) {
            this.levels.push({
              value: data[i].description,
              text: data[i].description,
            });
          }
        })
        .catch((error) => console.log(error));
    },

    /**
     * filterByCategory is a function which filters activity list based on the selected category filter.
     */
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

    /**
     * filterByLevel is a function which filters activity list based on the selected level filter.
     */
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

    /**
     * filterByLocation is a function which filters activity list based on the selected location filter.
     */
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

    /**
     * generateFilteredList is a function which generates a filtered list based on different filters.
     */
    generateFilteredList() {
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

    /**
     * findCurrentParticipants returns current participants for an activity with given ID.
     */
    findCurrentParticipants(activityId, currentParticipants) {
      this.currentParticipantsAll.push({
        id: activityId,
        currentParticipants: currentParticipants,
      });
    },

    /**
     * sortActivities is a function which sorts the filtered list based on the sort method chosen.
     */
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

    /**
     * sortByNameAsc is a function which sorts filtered list by name from A to Å.
     */
    sortByNameAsc() {
      return this.filteredActivities.sort((x, y) => {
        if (x.title < y.title) return -1;
        else if (x.title > y.title) return 1;
        return 0;
      });
    },

    /**
     * sortByNameDesc is a function which sorts filtered list by name from Å to A.
     */
    sortByNameDesc() {
      return this.filteredActivities.sort((x, y) => {
        if (x.title < y.title) return 1;
        else if (x.title > y.title) return -1;
        return 0;
      });
    },

    /**
     * sortByTimeEarliest is a function which sorts filtered list by startTime from earliest to latest.
     */
    sortByTimeEarliest() {
      return this.filteredActivities.sort((x, y) => {
        return new Date(x.startTime) - new Date(y.startTime);
      });
    },

    /**
     * sortByTimeLatest is a function which sorts filtered list by startTime from latest to earliest.
     */
    sortByTimeLatest() {
      return this.filteredActivities.sort((x, y) => {
        return new Date(y.startTime) - new Date(x.startTime);
      });
    },

    /**
     * sortByDurationDesc is a function which sorts filtered list by duration from highest to lowest.
     */
    sortByDurationDesc() {
      return this.filteredActivities.sort((x, y) => {
        let d1 = new Date(y.endTime) - new Date(y.startTime);
        let d2 = new Date(x.endTime) - new Date(x.startTime);
        return d1 - d2;
      });
    },

    /**
     * sortByDurationAsc is a function which sorts filtered list by duration from lowest to highest.
     */
    sortByDurationAsc() {
      return this.filteredActivities.sort((x, y) => {
        let d1 = new Date(x.endTime) - new Date(x.startTime);
        let d2 = new Date(y.endTime) - new Date(y.startTime);
        return d1 - d2;
      });
    },

    /**
     * sortByFreeSpotsDesc is a function which sorts filtered list by free spots from highest to lowest.
     */
    sortByFreeSpotsDesc() {
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

    /**
     * sortByFreeSpotsAsc is a function which sorts filtered list by free spots from lowest to highest.
     */
    sortByFreeSpotsAsc() {
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

    /**
     * sortByCurrentParticipantsDesc is a function which sorts filtered list by current participants from highest to lowest.
     */
    sortByCurrentParticipantsDesc() {
      return this.filteredActivities.sort((x, y) => {
        let current1 = this.currentParticipantsAll.find((a) => a.id === y.id)
          .currentParticipants;
        let current2 = this.currentParticipantsAll.find((a) => a.id === x.id)
          .currentParticipants;
        return current1 - current2;
      });
    },

    /**
     * sortByCurrentParticipantsAsc is a function which sorts filtered list by current participants from lowest to highest.
     */
    sortByCurrentParticipantsAsc() {
      return this.filteredActivities.sort((x, y) => {
        let current1 = this.currentParticipantsAll.find((a) => a.id === x.id)
          .currentParticipants;
        let current2 = this.currentParticipantsAll.find((a) => a.id === y.id)
          .currentParticipants;
        return current1 - current2;
      });
    },

    /**
     * sortByLevelDesc is a function which sorts filtered list by level from highest to lowest.
     */
    sortByLevelDesc() {
      return this.filteredActivities.sort((x, y) => {
        return y.level.id - x.level.id;
      });
    },

    /**
     * sortByLevelAsc is a function which sorts filtered list by level from lowest to highest.
     */
    sortByLevelAsc() {
      return this.filteredActivities.sort((x, y) => {
        return x.level.id - y.level.id;
      });
    },

    /**
     * getJoinedActivities is a function which returns all activities an account has relations to
     *  (has participated in or is participating in).
     */
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
          let sorted = data.sort(
            (x, y) => new Date(x.startTime) - new Date(y.startTime)
          );
          this.joinedActivities = sorted;
        })
        .catch((error) => console.log(error));
    },

    /**
     * joinedActivityClicked is a function which runs when a joined activity in mini calendar is clicked.
     * The function redirects the account to router/Activity.vue
     */
    joinedActivityClicked(activityId) {
      this.$router.push({ name: "Activity", params: { id: activityId } });
    },

    /**
     * handleActivityClicked is a function which runs when an activity in the feed is clicked.
     * The function redirects the account to router/Activity.vue
     */
    handleActivityClicked(activity) {
      this.activity = this.selectedActivity = activity;
      this.$emit("activityClicked", this.selectedActivity);
      this.$router.push({ name: "Activity", params: { id: activity.id } });
    },

    /**
     * getDayAndMonth is a function which returns the start date of an activity.
     */
    getDayAndMonth(activity) {
      let date = activity.startTime.split(" ")[0].split("-");
      return `${date[2]}.${date[1]}`;
    },

    /**
     * getClockTime is a function which returns the start timestamp of an activity.
     */
    getClockTime(activity) {
      let time = activity.startTime.split(" ")[1].split(":");
      return `${time[0]}:${time[1]}`;
    },

    /**
     * refreshList is a function which refreshes the joined activity list when an account is joined or removed from activity.
     */
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
  min-height: 100vh;
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

  @media (max-width: 935px) {
  }
}
</style>
