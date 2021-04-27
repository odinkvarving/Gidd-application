 <template>
  <div class="create-activity-container">
      <h1>GIDD å lag en ny aktivitet!</h1>
      <div id="create-activity-form">
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
        <li class="menu-item category">
          <label>
            Kategori<p style="color: red; display: inline; margin-left: 3px">*</p>
          </label>
          <b-form-select  :state="categoryState" v-model="category" :options="categories" style="width: 80%;">
            <template #first>
              <b-form-select-option :value="null" disabled>-- Velg en kategori --</b-form-select-option>
            </template>
          </b-form-select>
        </li>
        <li class="menu-item level">
          <label>

            Nivå<p style="color: red; display: inline; margin-left: 3px">*</p>
          </label>
          <b-form-select :state="levelState" v-model="level" :options="levels" style="width:80%">
            <template #first>
              <b-form-select-option :value="null" disabled>-- Velg et nivå --</b-form-select-option>
            </template>
          </b-form-select>
        </li>
        <li class="menu-item equipment">
              <label>
                  Utstyr
              </label>
              <div class="equipment-container">
                <b-form-tags :state="equipmentState" input-id="tags-basic" remove-on-delete v-model="equipment" placeholder="Legg til utstyr..."></b-form-tags>
              </div>
        </li>
        <li class="menu-item location">
          <div class="location-container">
          <div class="label-search-container">
            <label>
              Sted<p style="color: red; display: inline; margin-left: 3px">*</p>
            </label>
            
            <!-- Make this appear above everything, result suggestions doesn't show! -->
            <div class="location-search-container">
                  <LocationSearchBar @setPlace="setLocation" />
            </div>
          </div>
          <GmapMap
                v-show="geometryFound"
                :center='center'
                :zoom='12'
                style='width:100%;  height: 400px; margin-top: 20px;'
          >
             <GmapMarker
                :position="center"
              />
          </GmapMap>
          </div>

        <!--  <b-form-input :state="placeState" class="input-field" type="text" v-model="location"></b-form-input> -->
        </li>
        <li class="menu-item date">
          <label for="datepicker">Start<p style="color: red; display: inline; margin-left: 3px">*</p></label>
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
        <li class="menu-item date">
          <label for="datepicker">Slutt<p style="color: red; display: inline; margin-left: 3px">*</p></label>
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
        <li class="menu-item participants">
          <label>
            Deltakere<p style="color: red; display: inline; margin-left: 3px">*</p>
          </label>
          <input style="width:80%;" class="input-field" type="number" min="2" v-model="participants"/>
        </li>
        <li class="menu-item description">
            <div class="description-container">
                <label>
                    Beskrivelse<p style="color: red; display: inline; margin-left: 3px">*</p>
                </label>
                <b-form-textarea :state="descriptionState" class="description-input" type="text" v-model="description"  no-resize></b-form-textarea>
          </div>
        </li>
        <li class="menu-item visibility">
          <div class="participation-container">
            <label>
              Hvem kan delta<p style="color: red; display: inline; margin-left: 3px">*</p>
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
          <button class="button" @click="createButtonClicked()">
            <div v-if="showSpinner" class="spinner-border" role="status" style="margin-top: 4px">
                <span class="sr-only">Loading...</span>
            </div>
            <span v-else>Opprett Aktivitet</span>
          </button>
        </li>
      </ul>
        <ConfirmModal name="create-success" header="Vellykket!" info="Aktiviteten kan du nå finne i hovedoversikten!" buttonText="OK" />
      </div>
  </div>
</template>

<script>
import { userService } from "../services/UserService.js"
import moment from 'moment'
import LocationSearchBar from "../components/createActivityComponents/LocationSearchBar"
import ConfirmModal from "../components/PopUpComponents/ConfirmModal.vue"

export default {
    name: "CreateActivity",
    components: {
        LocationSearchBar,
        ConfirmModal
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
      geometryFound: false,
      center: {},
      startDateState: null,
      endDateState: null,
      participantsState: null,
      descriptionState: null,

      equipmentState: null,
      showSpinner: false,

    };
  },
  mounted(){
    this.getCategories();
    this.getLevels();
  },
  methods: {
    createButtonClicked(){
      //DOUBLE CHECK IF USER IS LOGGED IN HERE WITH isLoggedIn()

        this.showSpinner = true;
        this.equipmentState = true;

        this.name === '' ? this.nameState = false : this.nameState = true;
        this.category === null ? this.categoryState = false : this.categoryState = true;
        this.level === null ? this.levelState = false : this.levelState = true;
        this.location === '' ? this.placeState = false : this.placeState = true;
        this.validStartAndEndDate();
        this.description === '' ? this.descriptionState = false : this.descriptionState = true;

        if(this.nameState === true && this.categoryState === true && this.levelState === true
          && this.startDateState === true && this.endDateState === true
          && this.descriptionState === true){
            this.createActivity();
        }else{
          this.showSpinner = false;
        }
        

    },
    setLocation(location){
      if(location.geometry){
        this.currentLocation = location;
        this.center = {lat:this.currentLocation.geometry.location.lat(), lng:this.currentLocation.geometry.location.lng()}
        this.geometryFound = true;
      }else{
        this.geometryFound = false;
        this.currentLocation = location;
      }
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

        location: this.currentLocation.name,
        endTime: `${this.endDate} ${this.endTime}`,
        latitude: null, //temporary until map is implemented
        longitude: null, //temporary until map is implemented
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

      if(this.currentLocation.geometry){
        activity.latitude = this.currentLocation.geometry.location.lat();
        activity.longitude = this.currentLocation.geometry.location.lng();
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
        .then(data => {
          if(data){
            console.log("Activity was successfully created!");
            this.$bvModal.show("create-success");
          }
        })
        .finally(this.showSpinner = false)
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
}
</script>

<style scoped>

    .create-activity-container {
        width: 99vw;
        background-color: #F6F6F6;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        font-family: "Mulish";
    }

    h1 {
      margin: 100px 0 50px 0;
    }

    #create-activity-form {
        padding: 20px;
        width: 40vw;
        background-color: white;
        box-shadow: 0px 4px 4px 0px #0000001A;
        display: flex;
        flex-direction: column;
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
        padding: 20px 0 20px 0;
    }

    .menu-item label {
        opacity: 0.6;
        margin-bottom: 0;
        font-size: 20px;
    }

    .location-container {
      width: 100%;
      display: flex;
      flex-direction: column;
    }

    .label-search-container {
      display: flex;
      flex-direction: row;
      align-items: center;
      justify-content: space-between;
    }

    .menu-item .name-field {
        margin: auto;
        width: 100%;
        text-align: center;
        border: 1px solid rgba(0, 0, 0, 0.1);
        outline: none;
        font-size: 30px;
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
     display: flex;
     flex-direction: row;
     align-items: center;
     justify-content: space-between;
    }

    .datepicker {
        width: 35%;
        border: none !important;
        outline: none;
    }

    .timepicker {
        width: 35%;
        border: none !important;
        outline: none;
    }

    .partcipant-selector {
        min-width: 100px;
        margin-left: auto;
    }

    .input-field {
        width: 100%;
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


    .location-search-container {
        width: 80%;
    }

    .description-container {
        display: flex;
        flex-direction: column;
        width: 100%;
    }

    .description-container input {
        margin-top: 15px;
    }

    .submit-button {
        background-color: #ffbd3e;
        color: white;
        border: none;
        border-radius: 6px;
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

    .button {
        margin: 20px;
        border-radius: 4px;
        font-size: 16px;
        width: 100%;
        height: 50px;
        color: white;
        background-color: #ffbd3e;
        border: none;
        transition: 0.2s;
        font-size: 20px;
    }

</style>

