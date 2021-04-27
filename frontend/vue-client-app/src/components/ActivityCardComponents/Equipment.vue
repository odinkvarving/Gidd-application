<template>
  <div id="container">
    <h3>Utstyr:</h3>
    <div class="edit">
      <b-icon  v-show="isActivityHost && !activity.cancelled" class="pencil" icon="pencil" @click="edit"></b-icon>
    </div>
    <div class="equipment-list">
      <ul v-for="(e, index) in equipment" :key="e.description">
        <li id="e.description">
          {{ e.description }}
          <div class="remove-button">
            <b-icon
              class="remove-button"
              icon="x-square"
              v-show="inEditMode"
              @click="removeEquipment(index)"
            ></b-icon>
          </div>
        </li>
      </ul>
      <ul>
        <li>
          <input
            type="text"
            v-model="newEquipment"
            placeholder="Nytt utstyr"
            v-show="inEditMode"
          /><button @click="addEquipment" v-show="inEditMode">Legg til</button>
        </li>
      </ul>
    </div>
  </div>
</template>
<script>
import { notificationService } from '../../services/NotificationService';
export default {
  name: "Equipments",

  data() {
    return {
      inEditMode: false,
      equipment: this.activity.equipment,
      newEquipment: "",
    };
  },

  props: {
    activity: {
      type: Object,
      required: true,
    },
    isActivityHost: {
    type: Boolean,
    required: true,
    }
  },
  methods: {
    edit() {
      this.inEditMode = !this.inEditMode
    },
    removeEquipment(index) {
      this.equipment.splice(index, 1);
    },
    addEquipment() {
      if (this.newEquipment != "") {
        this.equipment.push({ description: this.newEquipment });
        this.newEquipment = "";
        let result = notificationService.sendNotificationToAllParticipants(this.activity.id);
        if(result === true){
              console.log("Sucessfully notified all participants about the edit!");
        }else{
          console.log("Error! Something went wrong when notifying participants!");
        }
      }
    },
  },
};
</script>
<style>
#container {
  position: relative;
}

.equipment-list {
  display: flex;
  flex-wrap: wrap;
  flex-direction: column;
  height: 80%;
}

li {
  display: flex;
}

.remove-button {
  margin-left: 2px;
  cursor: pointer;
}

input {
  width: 100px;
}
</style>
