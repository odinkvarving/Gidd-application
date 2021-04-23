<template>
  <div class="wrapper">
    <div class="create-activity-container">
      <ul>
        <li class="menu-item name-container">
          <input
            class="name-field"
            type="text"
            placeholder="Navn på aktivitet"
            v-model="name"
          />
        </li>
        <div class="line"></div>
        <li class="menu-item category">
          <label>
            Kategori:
          </label>
          <input class="input-field" type="text" v-model="category" />
        </li>
        <div class="line"></div>
        <li class="menu-item equipment">
          <div class="equipment-container">
              <label>
                  utstyr:
              </label>
              <b-form-tags input-id="tags-basic" v-model="equipment" placeholder="Legg til utstyr..."></b-form-tags>
          </div>        
        </li>
        <div class="line"></div>
        <li class="menu-item location">
          <label>
            sted:
          </label>
          <input class="input-field" type="text" v-model="location"/>
        </li>
        <div class="line"></div>
        <li class="menu-item date">
          <label for="datepicker">Dato:</label>
          <b-form-datepicker
            class="datepicker"
            v-model="date"
            size="sm"
            placeholder="Velg dato"
            data-date-format="mm/dd/yyyy"
          ></b-form-datepicker>
        </li>
        <li class="menu-item time">
          <label for="start-timepicker">Start:</label>
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            size="sm"
            v-model="startTime"
          ></b-form-timepicker>
          <label for="timepicker">Slutt:</label>
          <b-form-timepicker
            class="timepicker"
            placeholder="Velg tid"
            size="sm"
            v-model="endTime"
          ></b-form-timepicker>
        </li>
        <div class="line"></div>
        <li class="menu-item participants">
          <label>
            deltakere:
          </label>
          <input class="input-field" type="number" min="0" v-model="participants"/>
        </li>
        <div class="line"></div>
        <li class="menu-item description">
          <label>
            beskrivelse:
          </label>
          <input class="input-field" type="text" v-model="description" />
        </li>
        <div class="line"></div>
        <li class="menu-item visibility">
          <div class="participation-container">
            <label>
              Hvem kan delta:
            </label>
            <div>
              <b-dropdown
                class="participant-selector"
                :text="participantValue"
                size="sm"
              >
                <b-dropdown-item @click="participantValue = 'Alle'"
                  >Alle</b-dropdown-item
                >
                <b-dropdown-item @click="participantValue = 'Privat'"
                  >Privat</b-dropdown-item
                >
              </b-dropdown>
            </div>
          </div>
        </li>
        <li class="menu-item btn">
          <button @click="createButtonClicked()"><span>Opprett Aktivitet</span></button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { userService } from "../../services/UserService.js"

export default {
  name: "CreateActivity",
  data() {
    return {
      name: "",
      category: "",
      equipment: [],
      location: "",
      date: "",
      startTime: "",
      endTime: "",
      participants: 0,
      description: "",
      participantValue: "",
      isVisible: false,
    };
  },
  methods: {
    createButtonClicked(){
      //DOUBLE CHECK IF USER IS LOGGED IN HERE WITH isLoggedIn()

      this.createActivity();
    },
    async createActivity(){

      let accountDetails = await userService.getAccountByEmail();

      let equipmentList = [];
      for(let i = 0; i < this.equipment.length; i ++){
        equipmentList.push({description : this.equipment[i]});
      }

      let activity = {
        title: this.name,
        description: this.description,
        equipment: equipmentList,
        endTime: `${this.date} ${this.endTime}`,
        latitude: "63.41893", //temporary until map is implemented
        longitude: "10.40658", //temporary until map is implemented
        maxParticipants: this.participants,
        startTime: `${this.date} ${this.startTime}`,
        activityType: {
          type: this.category
        },
        creator: accountDetails
      }

      const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': userService.getTokenString()
        },
        body: JSON.stringify(activity) 
      }

      fetch("http://localhost:8080/activities/", requestOptions)
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.log(error));


    }
  }
};
</script>

<style>
.wrapper {
  position: absolute;
  top: calc(100%);
  transform: translateX(37%);
  width: max-content;
  background-color: white;
}

.create-activity-container {
  display: flex;
  border: 1px solid rgba(0, 0, 0, 0.15);
  width: 310px;
  justify-content: center;
}

ul {
  padding: 0;
  list-style-type: none;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 10px 0 10px 0;
}

.menu-item label {
  opacity: 0.6;
  margin-bottom: 0;
}

.menu-item .name-field {
  margin: auto;
  text-align: center;
  border: 1px solid rgba(0, 0, 0, 0.1);
  outline: none;
  font-size: 20px;
  background-color: hsla(0, 0%, 77%, 0.1);
}

.name-container {
    padding: 15px 0 15px 0;
}

.time {
  padding-top: 5px;
}

.date {
  padding-bottom: 5px;
}

.datepicker {
  border: none !important;
  outline: none;
}

.timepicker {
  border: none !important;
  outline: none;
}

.partcipant-selector {
  min-width: 100px;
  margin-left: auto;
}

.input-field {
  width: 100%;
  border: none;
  outline: none;
}

.btn {
  color: blue;
}

.line {
  height: 1px;
  background-color: rgba(0, 0, 0, 0.15);
  margin: 0 auto;
  width: 95%;
}

.participation-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.equipment-container {
  display: flex;
  flex-flow: column;
}

.equipment-container label{
  margin: 7px 0;
}

</style>
