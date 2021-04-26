<template>
  <div id="container">
    <h3>Utstyr:</h3>
    <div class="edit">
      <b-icon class="pencil" icon="pencil" @click="edit"></b-icon>
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
