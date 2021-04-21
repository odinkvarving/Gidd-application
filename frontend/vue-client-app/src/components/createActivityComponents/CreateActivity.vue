<template>
  <div class="create-activity-container">
    <ul>
      <li class="menu-item name-field">
        <input class="name-field" type="text" placeholder="Navn på aktivitet" />
      </li>
      <div class="line"></div>
      <li class="menu-item equipment">
        <label>
          utstyr:
        </label>
        <input class="input-field" type="text" />
      </li>
      <div class="line"></div>
      <li class="menu-item location">
        <label>
          sted:
        </label>
        <input class="input-field" type="text" />
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
        <label for="timepicker">Tid:</label>
        <b-form-timepicker
          class="timepicker"
          placeholder="Velg tid"
          size="sm"
          v-model="time"
        ></b-form-timepicker>
      </li>
      <div class="line"></div>
      <li class="menu-item participants">
        <label>
          deltakere:
        </label>
        <input class="input-field" type="number" min="0" />
      </li>
      <div class="line"></div>
      <li class="menu-item description">
        <label>
          beskrivelse:
        </label>
        <input class="input-field" type="text" />
      </li>
      <div class="line"></div>
      <li class="menu-item visibility">
          <div class="participation-container">
            <label>
            Hvem kan delta:
            </label>
            <div>
                <b-dropdown class="participant-selector" :text="participantValue" size="sm">
                    <b-dropdown-item @click="participantValue = 'Alle'">Alle</b-dropdown-item>
                    <b-dropdown-item @click="participantValue = 'Privat'">Privat</b-dropdown-item>
                </b-dropdown>
            </div>          
        </div>
      </li>
      <li class="menu-item btn">
        <button><span>Opprett Aktivitet</span></button>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  name: "CreateActivity",
  data() {
    return {
      date: "",
      time: "",
      participantValue: "..."
    };
  },
};
</script>

<style>
.create-activity-container {
  display: flex;
  border: 1px solid rgba(0, 0, 0, 0.15);
  width: 300px;
  height: 500px;
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
  border: 1px solid rgba(0, 0, 0, 0.10);
  outline: none;
  font-size: 20px;
  background-color: hsla(0, 0%, 77%, 0.10);
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
}

.participation-container {
    display: flex;
    align-items: center;
    justify-content: space-between;
    width: 100%;
}
</style>
