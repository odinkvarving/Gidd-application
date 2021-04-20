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
      <li class="menu-item">
        <label for="datepicker">Dato:</label>
        <b-form-datepicker
          class="datepicker"
          v-model="value"
          size="sm"
          placeholder="Velg dato"
        ></b-form-datepicker>
      </li>
      <li class="menu-item time">
        <label for="timepicker">Tid:</label>
        <b-form-timepicker placeholder="Velg tid" size="sm"></b-form-timepicker>
      </li>
      <div class="line"></div>
      <li class="menu-item participants">
        <label>
          deltakere:
        </label>
        <input class="input-field" type="text" />
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
        <label>
          Hvem kan delta:
        </label>
        <b-dropdown>
          <b-dropdown-item>Alle</b-dropdown-item>
          <b-dropdown-item>Privat</b-dropdown-item>
        </b-dropdown>
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
  border: 1px solid rgba(0, 0, 0, 0.15);
  outline: none;
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

.datepicker {
  width: 1000px;
  height: 10px;
}

</style>
