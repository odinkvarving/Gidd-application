<template>
  <div id="container">
    <div class="title-container">
      <h3 class="title">Utstyr:</h3>
      <div class="edit">
        <b-icon
          v-show="isActivityHost && !activity.cancelled"
          class="pencil"
          icon="pencil"
          @click="edit"
          font-scale="1.5"
        ></b-icon>
      </div>
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
import { notificationService } from "../../services/NotificationService";
import { userService } from "../../services/UserService";
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
    },
  },
  methods: {
    edit() {
      this.inEditMode = !this.inEditMode;
    },

    removeEquipment(index) {
      const requestOptions = {
        method: "DELETE",
        headers: {
          "Content-Type": "application/json",
          Authorization: userService.getTokenString(),
        },
        body: this.equipment[index].description,
      };

      fetch(
        `http://localhost:8080/activities/${this.activity.id}/equipment`,
        requestOptions
      )
        .then((response) => response.json())
        .then((data) => {
          // If update activity was successfull
          if (data !== null) {
            let result = notificationService.sendNotificationToAllParticipants(
              this.activity.id
            );
            if (result === true) {
              console.log(
                "Sucessfully notified all participants about the edit!"
              );
            } else {
              console.log(
                "Error! Something went wrong when notifying participants!"
              );
            }
          }
        })
        .catch((error) => console.log(error));

      this.equipment.splice(index, 1);
    },

    async addEquipment() {
      if (this.newEquipment != "") {
        this.equipment.push({ description: this.newEquipment });

        const requestOptions = {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            Authorization: userService.getTokenString(),
          },
          body: JSON.stringify(this.newEquipment),
        };

        this.newEquipment = "";

        fetch(
          `http://localhost:8080/activities/${this.activity.id}/equipment`,
          requestOptions
        )
          .then((response) => response.json())
          .then((data) => {
            // If update activity was successfull
            if (data !== null) {
              let result = notificationService.sendNotificationToAllParticipants(
                this.activity.id
              );
              if (result === true) {
                console.log(
                  "Sucessfully notified all participants about the edit!"
                );
              } else {
                console.log(
                  "Error! Something went wrong when notifying participants!"
                );
              }
            }
          })
          .catch((error) => console.log(error));
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

.title-container {
  display: flex;
}

.title {
  margin: 10px;
}

.edit {
  align-self: center;
  margin-left: auto;
  margin-right: 20px;
}

.pencil {
}
</style>
