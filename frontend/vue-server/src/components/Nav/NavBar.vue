<template>
  <nav class="nav-bar">
    <div v-click-outside="onClickOutside"></div>
    <div class="search-field"><input class="search-box" placeholder="Søk"></div>
    <div class="menu-item create-activity"><a href="#">Opprett Aktivitet</a></div>
    <Dropdown icon="Varslinger" :items="notifications" />
    <Dropdown icon="Bruker" :items="user"/>
  </nav>
</template>

<script>
import vClickOutside from "v-click-outside";
import Dropdown from "./Dropdown.vue";

export default {
  name: "navbar",
  components: {
    Dropdown,
  },
  directives: {
      clickOutside: vClickOutside.directive
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
  methods: {
      onClickOutside (event) {
        console.log('Clicked outside. Event: ', event)
      }
    }
};
</script>

<style>
.nav-bar {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

nav .menu-item {
  color: #000;
  padding: 10px 20px;
  position: relative;
  text-align: center;
  display: flex;
  margin: 0 10px;
}

nav .menu-item:active,
nav .menu-item:hover {
  background-color: rgb(219, 219, 219);
}

nav .menu-item a {
  color: inherit;
  text-decoration: none;
}

nav .search-field {
    padding: 0px 20px;
    display: flex;
    align-content: center;
}

nav .search-box {
    background-color: #D1D1D1;
    opacity: 0.5;
    border: none;
    border-radius: 6px;
    height: 30px;
    outline: none;
    padding-left: 10px;
}

nav .search-box::placeholder {
    color: rgb(50, 50, 50);
}

nav .create-activity {
    background-color: #FFBD3E;
    border-radius: 6px;
    padding: 6px 20px;
}

nav .create-activity a {
    color: white;
}
</style>
