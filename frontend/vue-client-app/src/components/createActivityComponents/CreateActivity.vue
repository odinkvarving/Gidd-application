<template>
  <div class="wrapper">
    <div class="create-activity-container">
      <ul>
        <li class="menu-item name-container">
          <b-form-input
            :state="nameState"
            class="name-field"
            type="text"
            placeholder="Navn på aktivitet"
            v-model="name"
          ></b-form-input>
        </li>
        <div class="line"></div>
        <li class="menu-item category">
          <label>
            Kategori:
          </label>
          <b-form-select  :state="categoryState" v-model="category" :options="categories" style="width: 200px;">
            <template #first>
              <b-form-select-option :value="null" disabled>-- Velg en kategori --</b-form-select-option>
            </template>
          </b-form-select>
        </li>
        <li class="menu-item level">
          <label>
            Nivå:
          </label>
          <b-form-select :state="levelState" v-model="level" :options="levels" style="width: 200px">
            <template #first>
              <b-form-select-option :value="null" disabled>-- Velg et nivå --</b-form-select-option>
            </template>
          </b-form-select>
        </li>
        <div class="line"></div>
        <li class="menu-item equipment">
          <div class="equipment-container">
              <label>
                  Utstyr:
              </label>
              <b-form-tags :state="equipmentState" input-id="tags-basic" remove-on-delete v-model="equipment" placeholder="Legg til utstyr..."></b-form-tags>
          </div>        
        </li>
        <div class="line"></div>
        <li class="menu-item location">
          <label>
            Sted:
          </label>
          
          <!-- Make this appear above everything, result suggestions doesn't show! -->
          <LocationSearchBar />

        <!--  <b-form-input :state="placeState" class="input-field" type="text" v-model="location"></b-form-input> -->
        </li>
        <div class="line"></div>
        <li class="menu-item date">
          <label for="datepicker">Start:</label>
          <b-form-datepicker
            :state="startDateState"
            class="datepicker"
            v-model="startDate"
            size="sm"
            placeholder="Velg dato"
            data-date-format="mm/dd/yyyy"
          ></b-form-datepicker>
          <b-form-timepicker
            :state="startDateState"
            class="timepicker"
            placeholder="Velg tid"
            size="sm"
            v-model="startTime"
          ></b-form-timepicker>
        </li>
        <div class="line"></div>
        <li class="menu-item date">
          <label for="datepicker">Slutt:</label>
          <b-form-datepicker
            :state="endDateState"
            class="datepicker"
            v-model="endDate"
            size="sm"
            placeholder="Velg dato"
            data-date-format="mm/dd/yyyy"
          ></b-form-datepicker>
          <b-form-timepicker
            :state="endDateState"
            class="timepicker"
            placeholder="Velg tid"
            size="sm"
            v-model="endTime"
          ></b-form-timepicker>
        </li>
        <div class="line"></div>
        <li class="menu-item participants">
          <label>
            Deltakere:
          </label>
          <input class="input-field" type="number" min="2" v-model="participants"/>
        </li>
        <div class="line"></div>
        <li class="menu-item description">
          <label>
            Beskrivelse:
          </label>
          <b-form-input :state="descriptionState" class="input-field" type="text" v-model="description"></b-form-input>
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
          <button class="submit-button" @click="createButtonClicked()"><span>Opprett Aktivitet</span></button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import { userService } from "../../services/UserService.js"
import moment from 'moment'
import LocationSearchBar from "./LocationSearchBar.vue"

export default {
  name: "CreateActivity",
  components: {
    LocationSearchBar
  },
  data() {
    return {
      name: "",
      category: "",
      categories: [],
      level: "",
      levels: [],
      equipment: [],
      location: "",
      startDate: "",
      endDate: "",
      startTime: "",
      endTime: "",
      participants: 0,
      description: "",
      participantValue: "",
      isVisible: false,
      nameState: null,
      categoryState: null,
      levelState: null,
      placeState: null,
      startDateState: null,
      endDateState: null,
      participantsState: null,
      descriptionState: null,
      equipmentState: null

    };
  },
  mounted(){
    this.getCategories();
    this.getLevels();
  },
  methods: {
    createButtonClicked(){
      //DOUBLE CHECK IF USER IS LOGGED IN HERE WITH isLoggedIn()

        this.equipmentState = true;

        this.name === '' ? this.nameState = false : this.nameState = true;
        this.category === null ? this.categoryState = false : this.categoryState = true;
        this.level === null ? this.levelState = false : this.levelState = true;
        this.location === '' || this.placeState === null ? this.placeState = false : this.placeState = true;
        this.validStartAndEndDate();
        this.description === '' ? this.descriptionState = false : this.descriptionState = true;

        if(this.nameState === true && this.categoryState === true && this.levelState === true
          && this.placeState === true && this.startDateState === true && this.endDateState === true
          && this.descriptionState === true){
            this.createActivity();
        }

      // TODO: Make confirmation when creating activity!!!!
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
        endTime: `${this.endDate} ${this.endTime}`,
        latitude: "63.41893", //temporary until map is implemented
        longitude: "10.40658", //temporary until map is implemented
        maxParticipants: this.participants,
        startTime: `${this.startDate} ${this.endTime}`,
        activityType: {
          type: this.category
        },
        level: {
          description: this.level
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
    },
    async getCategories(){      
      
      let categoriesList;
      
      this.category = null;

      let url = `http://localhost:8080/activityTypes/`;

      await fetch(url,{
        method: 'GET',
        headers: userService.authorizationHeader()
      })
        .then(response => response.json())
        .then(data => categoriesList = data)
        .catch(error => console.log(error));
      
      for(let i = 0; i < categoriesList.length; i ++){
        this.categories.push(categoriesList[i].type);
      }
    },
    async getLevels(){
      let levelsList;

      this.level = null;
      this.participantValue = "Alle";

      let url = `http://localhost:8080/levels/`;

      await fetch(url,{
        method: 'GET',
        headers: userService.authorizationHeader()
      })
        .then(response => response.json())
        .then(data => levelsList = data)
        .catch(error => console.log(error));
      
      for(let i = 0; i < levelsList.length; i ++){
        this.levels.push(levelsList[i].description);
      }
    },
    validStartAndEndDate(){
      this.startDate === '' || this.startTime === '' ? this.startDateState = false : this.startDateState = true;
      this.endDate === '' || this.endTime === '' ? this.endDateState = false : this.endDateState = true;

      if(this.startDate === '' || this.startTime === ''){
        this.startDateState = false;
      }
      if(this.endDate === '' || this.endTime === ''){
        this.endDateState = false;
      }
      else{
        if(moment(`${this.startDate} ${this.startTime}`).isBefore(`${this.endDate} ${this.endTime}`)){
          this.endDateState = true;
          this.startDateState = true;
        }else{
          this.startDateState = false;
          this.endDateState = false;
        }
      }
    }
  }
};
</script>

<style scoped>
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

label {
  margin: 0 10px 0 0;
}


.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px 0 10px 0;
}

.menu-item label {
  opacity: 0.6;
  margin-bottom: 0;
}

.menu-item .name-field {
  margin: auto;
  width: 100%;
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

.submit-button {
  background-color: #ffbd3e;
  color: white;
  border: none;
  border-radius: 6px;
  padding: 0 20px;
  height: 30px;
  min-width: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
  align-self: center;
  margin: auto;
  margin-bottom: 10px;
}

.submit-button:hover {
  background-color: #eca82b;
  transition: 0.2s;
}
</style>
