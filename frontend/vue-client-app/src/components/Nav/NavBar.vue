<template>
  <nav class="nav-bar">
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
    <Dropdown icon="berit.jpg" :items="user" />
  </nav>
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
          link: "#",
        },
      ],
      notifications: [{}],
    };
  },
};
</script>

<style>
.nav-bar {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  border: 1px solid black;
}

nav .menu-item {
  color: #000;
  padding: 10px 20px;
  position: relative;
  text-align: center;
  display: flex;
  margin: 0 10px;
}

nav .notification-icon {
  margin-right: -10px;
}

nav .menu-item:hover {
  cursor: pointer;
}

nav .notification-icon:hover {
  background-color: none;
}

nav .create-activity:hover {
  background-color: #eca82b;
  transition: 0.1s;
}

nav .menu-item a {
  color: inherit;
  text-decoration: none;
  cursor: pointer;
}

nav .search-field {
  padding: 0px 20px;
  display: flex;
  align-content: center;
}

nav .search-box {
  background-color: #d1d1d1;
  opacity: 0.5;
  border: none;
  border-radius: 6px;
  height: 30px;
  width: 180px;
  outline: none;
  padding-left: 10px;
}

nav .search-box::placeholder {
  color: rgb(50, 50, 50);
}

nav .create-activity {
  background-color: #ffbd3e;
  border-radius: 6px;
  padding: 6px 20px;
  height: 30px;
  min-width: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto;
}

nav .create-activity a {
  color: white;
}
</style>
