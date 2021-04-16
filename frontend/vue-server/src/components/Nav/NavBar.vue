<template>
  <nav class="nav-bar">
    <div class="menu-item"><a href="#">Søk</a></div>
    <div class="menu-item"><a href="#">Opprett Aktivitet</a></div>
    <Dropdown title="Varslinger" :items="notifications" />
    <Dropdown title="Bruker" :items="user" />
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
}

nav .menu-item {
  color: #000;
  padding: 10px 20px;
  position: relative;
  text-align: center;
  display: flex;
}

nav .menu-item:active,
nav .menu-item:hover {
  background-color: rgb(219, 219, 219);
}

nav .menu-item a {
  color: inherit;
  text-decoration: none;
}
</style>
