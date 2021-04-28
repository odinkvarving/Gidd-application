<template>
  <div id="container">
    <div id="feed">
      <div class="sortingContainer">
        <div class="sortingDropdown">
          <b-dropdown text="Sortering" class="sortingBox">
            <b-dropdown-item href="#">Ledige plasser høy-lav</b-dropdown-item>
            <b-dropdown-item href="#">Ledige plasser lav-høy</b-dropdown-item>
            <b-dropdown-item href="#">Antall påmeldte høy-lav</b-dropdown-item>
            <b-dropdown-item href="#">Antall påmeldte lav-høy</b-dropdown-item>
          </b-dropdown>
        </div>
        <div class="filteringDropdown">
          <b-dropdown text="Filtrering" class="filteringBox">
            <b-dropdown text="Aktivitetstype" class="m-2">
                <b-dropdown-item href="#">Tur</b-dropdown-item>
                <b-dropdown-item href="#">Tur</b-dropdown-item>
            </b-dropdown>

            <b-dropdown-item href="#">Nivå</b-dropdown-item>
            <b-dropdown-item href="#">Kategori</b-dropdown-item>
          </b-dropdown>
        </div>
      </div>

      <div
        v-for="a in activities"
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
      activities: {},
      joinedActivities: {},
      sortKey: "",
      isLoggedIn: false,
      modifiedActivities: {},
      filter: [],
      filterKey: "",
    };
  },
  mounted() {
    this.getActivities();
    if (userService.isLoggedIn()) {
      this.isLoggedIn = true;
      this.getJoinedActivities();
    }
  },
  methods: {
    getActivities() {
      const requestOptions = {
        method: "GET",
      };

      // Get all registered activites from database
      fetch("http://localhost:8080/activities/", requestOptions)
        .then((response) => response.json())
        .then((data) => {
          this.activities = data;
          console.log(data[0]);
        })
        .catch((error) => console.log(error));
    },

    modifyActivities() {
      let listFiltered = this.filterActivities();
      let listSorted = this.sortActivities(listFiltered);
      this.modifiedActivities = listSorted;
    },

    filterActivities() {
      //if(this.filterKey === )
    },

    sortActivities() {},

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
          let sorted = data.sort((x,y) => x.startTime - y.startTime);
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

.sortingContainer {
    width: 100%;
    display: flex;
    padding-bottom: 20px;
    padding-left: 55px;
}

.sortingDropdown {
  display: inline-block;
  padding-right: 10px;
  padding-left: 10px;
}

.sortingBox {
    width: 180px;
}

.filteringDropdown {
  display: inline-block;
  padding-right: 10px;
  padding-left: 10px;
}

.filteringBox {
    width: 180px;
}
</style>
