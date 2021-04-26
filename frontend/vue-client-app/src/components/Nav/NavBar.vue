<template>
  <b-navbar class="bg-white" fixed="top" v-show="onHomePage">
    <div class="collapse navbar-collapse">
      <router-link to="/">
        <div class="logo">
          <div class="circle-2">
            <div class="circle-3" />
          </div>
        </div>
      </router-link>
      <ul class="navbar-nav ml-auto">
        <div class="search-field">
          <input class="search-box" placeholder="Søk" />
        </div>

        <router-link v-if="isLoggedIn" to="/dashboard/createActivity">
          <div class="menu-item create-activity">Lag aktivitet</div>
        </router-link>

        <router-link v-else to="/login">
          <div class="menu-item create-activity">Logg inn</div>
        </router-link>

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
import { userService } from "../../services/UserService.js";

export default {
  name: "navbar",
  components: {
    Dropdown,
  },
  data() {
    return {
      isCreateActivityVisible: false,
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
      isLoggedIn: userService.isLoggedIn(),
    };
  },
  computed: {
    onHomePage() {
      if (
        this.$route.path === "/" ||
        this.$route.path === "/register" ||
        this.$route.path === "/login" ||
        this.$route.path === "/forgotPassword" ||
        this.$route.path.includes("/resetpassword")
      ) {
        return false;
      } else {
        return true;
      }
    },
  },
  methods: {
    toggleCreateActivity() {
      if (this.isCreateActivityVisible === false) {
        this.isCreateActivityVisible = true;
      } else {
        this.isCreateActivityVisible = false;
      }
    },
  },
};
</script>

<style>
.logo {
  width: 59px;
  height: 59px;
  background-color: black;
  border-radius: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.circle-2 {
  width: 42px;
  height: 42px;
  background-color: white;
  border-radius: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
}

.circle-3 {
  width: 21px;
  height: 21px;
  background-color: #ff0000;
  border-radius: 100%;
}

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
  color: white;
  border-radius: 6px;
  padding: 0 20px;
  height: 30px;
  min-width: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
  align-self: center;
}

nav .create-activity a {
  color: white;
}

.bg-white {
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
}
</style>
