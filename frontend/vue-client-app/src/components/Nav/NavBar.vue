<template>
  <b-navbar class="bg-white" toggleable="md" fixed="top">
    <router-link to="/dashboard">
      <img
        src="../../assets/Logo.jpg"
        alt="GIDD logo"
        width="50px"
        height="50px"
        style="margin-right: 10px; margin-left: 10px"
      />
    </router-link>
    <router-link class="mr-auto" to="/dashboard">
      <b-navbar-brand>GIDD</b-navbar-brand>
    </router-link>
    <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
    <b-collapse id="nav-collapse" is-nav>
      <b-navbar-nav class="ml-auto nav-container-center" justified>
        <b-nav-form right>
          <div class="search-field">
            <input
              v-model="search"
              type="text"
              @input="onChange"
              @keydown.down="onArrowDown"
              @keydown.up="onArrowUp"
              @keydown.enter="onEnter"
              placeholder="Søk"
            />
            <div class="wrapper">
              <ul v-show="isOpen" class="autocomplete-results">
                <li v-if="isLoading" class="loading">Loading results...</li>
                <li
                  v-else
                  v-for="(result, i) in results"
                  :key="i"
                  class="autocomplete-result"
                  :class="{ 'is-active': i === arrowCounter }"
                >
                  <p @click="resultClicked(result.id)">
                    <span class="title">{{ result.title }}</span>
                    <span class="email">{{ result.creator.email }}</span>
                  </p>
                </li>
              </ul>
            </div>
          </div>
        </b-nav-form>
        <b-nav-item>
          <router-link v-if="isLoggedIn" to="/dashboard/createActivity">
            <div class="menu-item create-activity">Lag aktivitet</div>
          </router-link>
          <router-link v-else to="/login">
            <div class="menu-item create-activity">
              Logg inn
            </div>
          </router-link>
        </b-nav-item>

        <b-nav-form>
          <NotificationsDropdown class="notification-icon" icon="bell.png" />
        </b-nav-form>

        <b-nav-item-dropdown right>
          <template slot="button-content">
            <img v-if="accountInfo.imageURL !== ''" class="profile-pic" alt="Profile picture" :src="this.accountInfo.imageURL" height="40px" width="40px">
            <img v-else src="https://cdn.pixabay.com/photo/2016/08/08/09/17/avatar-1577909_960_720.png" alt="default pic" class="profile-pic" height="40px" width="40px">

          </template>
          <b-dropdown-item :to="`${user[0].link}`">
            Min profil
          </b-dropdown-item>
          <b-dropdown-item to="/feedback">
            Gi tilbakemelding
          </b-dropdown-item>
          <b-dropdown-item @click="logoutClicked()" to="/">
            Logg ut
          </b-dropdown-item>
        </b-nav-item-dropdown>
      </b-navbar-nav>
    </b-collapse>
  </b-navbar>
</template>

<script>
import NotificationsDropdown from "./NotificationsDropdown.vue";
import { userService } from "../../services/UserService.js";

export default {
  name: "navbar",
  /* props: {
    items: {
      type: Array,
      required: false,
      default: () => [],
    },
    isAsync: {
      type: Boolean,
      required: false,
      default: false,
    },
  },

  watch: {
    items: function(value, oldValue) {
      console.log(oldValue);
      if (this.isAsync) {
        this.results = value;
        this.isOpen = true;
        this.isLoading = false;
      }
    },
  },
 */
  components: {
    NotificationsDropdown,
  },

  async mounted() {
    document.addEventListener("click", this.handleClickOutside);
    this.getActivities();
    this.isLoggedIn = await userService.isLoggedIn();
    if(this.isLoggedIn){
      let account = await userService.getAccountByEmail();
      this.accountInfo = await userService.getAccountInfo(account.id);
    }
  },

  destroyed() {
    document.removeEventListener("click", this.handleClickOutside);
  },

  data() {
    return {
      isCreateActivityVisible: false,
      /**
       * User: Contains information about the different items in the navbar dropdown menu, with corresponding links and functions.
       */
      user: [
        {
          title: "Min Profil",
          link: String,
          /**
           * Checks if user is logged in, and adds the correct routing link to user page.
           */
          method: () => {
            console.log(this.user[0].link);
            if (this.user[0].link) {
              console.log("Logged in; going to " + this.user[0].link);
              this.$router.push(this.user[0].link);
            } else {
              console.log("Not logged in, redirecting to login");
              this.$router.push("/login");
            }
          },
        },
        {
          title: "Gi tilbakemelding",
          link: "/feedback",
          method: () => {
            this.$router.push("/feedback");
          },
        },
        {
          title: "Logg Ut",
          link: "/",
          method: () => {
            userService.logout();
            this.$router.push("/");
          },
        },
      ],
      /**
       * Boolean operator that flags if a user is logged in.
       */
      isLoggedIn: false,
      /**
       * Array of notifications.
       */
      notifications: [{}],
      /**
       * List of activities.
       */
      activities: {},
      /**
       * Input text in search field
       */
      search: "",
      /**
       * Boolean operator that flags if something is loading.
       */
      isLoading: false,
      /**
       * List of search results.
       */
      results: [],
      /**
       * oolean operator that flags if search field is open.
       */
      isOpen: false,
      /**
       * Used to check which item in search field is selected with arrows.
       */
      arrowCounter: -1,
      /**
       * Account info
       */
      accountInfo: {}
    };
  },

  /**
   * Gets the logged in users user id on creation.
   */
  created() {
    if (userService.getTokenString()) {
      this.getUserId();
    }
  },

  methods: {
    /**
     * Gets the logged in users user id, and sets the navbar dropdown user page link to the given user page.
     */
    async getUserId() {
      let res = await userService.getAccountByEmail(
        userService.getAccountDetails().sub
      );
      this.user[0].link = "/accounts/" + res.id;
    },

    /**
     * Routes to correct activity page when a search result is clicked.
     */
    async resultClicked(resultId) {
      console.log(resultId);
      if (this.$router.path !== `/dashboard/activity/${resultId}`) {
        console.log("test");
        await this.$router.push(`/dashboard/activity/${resultId}`);
      }
      this.isOpen = false;
      this.search = "";
    },

    /**
     * Fetches activities from backend.
     */
    getActivities() {
      const requestOptions = {
        method: "GET",
      };

      // Get all registered activites from database
      fetch("http://localhost:8080/activities/", requestOptions)
        .then((response) => response.json())
        .then((data) => {
          this.activities = data;
          console.log(data[0]);
        })
        .catch((error) => console.log(error));
    },

    /**
     * Filter results from selected keywords.
     */
    filterResults() {
      console.log(this.search);
      this.results = [];
      for (let i = 0; i < this.activities.length; i++) {
        if (
          this.activities[i].title
            .toLowerCase()
            .includes(this.search.toLowerCase())
        ) {
          this.results.push(this.activities[i]);
        } else if (
          this.activities[i].creator.email
            .toLowerCase()
            .includes(this.search.toLowerCase())
        ) {
          this.results.push(this.activities[i]);
        }
      }
    },


    onChange() {
      this.$emit("input", this.search);
      if (this.isAsync) {
        this.isLoading = true;
      } else {
        this.filterResults();
        this.isOpen = true;
      }
    },
    
    setResult(result) {
      this.search = result;
      this.isOpen = false;
    },
    handleClickOutside(event) {
      if (!this.$el.contains(event.target)) {
        this.arrowCounter = -1;
        this.isOpen = false;
      }
    },
    onArrowDown() {
      if (this.arrowCounter < this.results.length) {
        this.arrowCounter = this.arrowCounter + 1;
      }
    },
    onArrowUp() {
      if (this.arrowCounter > 0) {
        this.arrowCounter = this.arrowCounter - 1;
      }
    },
    onEnter() {
      //fiks onEnter gjør samme som click
      this.search = this.results[this.arrowCounter];
      this.arrowCounter = -1;
    },
    logoutClicked() {
      userService.logout();
    },
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
  margin: 0px 10px;
  width: 180px;
  align-content: center;
  align-self: center;
  position: relative;
  padding: 8px 0;
}

input {
  padding: 0 5px;
  height: 30px;
  min-width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: auto;
  align-self: center;
}

.wrapper {
  float: left;
  position: absolute;
  z-index: 100;
  background: white;
}

.autocomplete-results {
  padding: 0;
  margin: 0;
  border: 1px solid black;
  height: 120px;
  width: 260px;
  min-height: 1em;
  max-height: 6em;
  overflow: auto;
  z-index: 1;
  position: sticky;
}

.autocomplete-result {
  list-style: none;
  text-align: left;
  padding: 4px 2px;
  cursor: pointer;
  padding-bottom: 0px;
}

.autocomplete-result.is-active,
.autocomplete-result:hover {
  background-color: #ffbd3e;
  color: white;
}

.create-activity {
  background-color: #ffbd3e;
  color: white;
  border-radius: 6px;
  height: 30px;
  min-width: 180px;
  display: flex;
  justify-content: center;
  align-items: center;
  align-self: center;
}

.bg-white {
  box-shadow: 0px 0px 10px 0px rgba(0, 0, 0, 0.1);
  min-height: 72px;
}

a.nav-link {
  outline: none;
  display: flex;
  align-items: center;
}

.dropdown-toggle:after {
 display: none !important;
}

.profile-pic {
  margin-left: 10px;
  margin-right: 10px;
  border-radius: 100%;
}
</style>
