<template>
  <b-navbar fixed="top">
    <div class="collapse navbar-collapse">
      <ul class="navbar-nav ml-auto">
        <div class="search-field">
          <input class="search-box" placeholder="Søk" />
        </div>
        <div class="menu-item create-activity">
          <a href="#">Opprett Aktivitet</a>
        </div>
        <Dropdown
          class="notification-icon"
          icon="bell.png"
          :items="notifications"
        />
        <Dropdown icon="user.png" :items="user" />
      </ul>
    </div>
  </b-navbar>
</template>

<script>
import Dropdown from "./Dropdown.vue";

export default {
  name: "navbar",
  components: {
    Dropdown,
  },
  data() {
    return {
      user: [
        {
          title: "Min Profil",
          link: "#",
        },
        {
          title: "Innstillinger",
          link: "#",
        },
        {
          title: "Logg Ut",
          link: "/",
        },
      ],
      notifications: [{}],
    };
  },
};
</script>

<style>

.menu-item {
  margin: 0 10px;
}

.menu-item:hover {
  cursor: pointer;
}

.create-activity:hover {
  background-color: #eca82b;
  transition: 0.2s;
}

.menu-item a {
  text-decoration: none;
  cursor: pointer;
}

.search-field {
  padding: 0px 20px;
  display: flex;
  align-content: center;
  align-self: center;
}

.search-box {
  background-color: #d1d1d1;
  opacity: 0.5;
  border: none;
  border-radius: 6px;
  height: 30px;
  width: 180px;
  outline: none;
  padding-left: 10px;
}

.search-box::placeholder {
  color: rgb(50, 50, 50);
}

.create-activity {
  background-color: #ffbd3e;
  border-radius: 6px;
  padding: 6px 20px;
  height: 30px;
  min-width: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto;
  align-self: center;
}

nav .create-activity a {
  color: white;
}
</style>
