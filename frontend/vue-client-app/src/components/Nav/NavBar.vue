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
          <input v-model="search" type="text" @input="onChange" @keydown.down="onArrowDown" @keydown.up="onArrowUp" @keydown.enter="onEnter" placeholder="Søk"/>
          <div class="wrapper">
            <ul v-show="isOpen" class="autocomplete-results">
            <li v-if="isLoading" class="loading"> Loading results...</li>
            <li v-else v-for="(result, i) in results" :key="i" class="autocomplete-result" :class="{ 'is-active': i === arrowCounter }">
              <p @click="resultClicked(result.id)">
                 <span class="title">{{result.title}}</span> 
                 <span class="email" >{{result.creator.email}}</span>
              </p>
            </li>
        </ul>
          </div>
        </div>

        <router-link v-if="isLoggedIn" to="/dashboard/createActivity">
          <div class="menu-item create-activity">Lag aktivitet</div>
        </router-link>

        <router-link v-else to="/login">
            <div class="menu-item create-activity">
                Logg inn
            </div>
          </router-link>
        
        <NotificationsDropdown
          class="notification-icon"
          icon="bell.png"
        />
        <Dropdown icon="user.png" :items="user" />
      </ul>
    </div>
  </b-navbar>
</template>

<script>
import Dropdown from "./Dropdown.vue";
import NotificationsDropdown from "./NotificationsDropdown.vue"
import { userService } from "../../services/UserService.js"

export default {
  name: "navbar",
  props: {
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
    
    items: function (value, oldValue) {
      console.log(oldValue);
      if(this.isAsync) {
        this.results = value;
        this.isOpen = true;
        this.isLoading = false;
      }
    }
  },

  components: {
    Dropdown,
    NotificationsDropdown
  },

  mounted() {
    document.addEventListener('click', this.handleClickOutside);
    this.getActivities();
  },
  destroyed() {
    document.removeEventListener('click', this.handleClickOutside);
  },
  
  data() {
    return {
      isCreateActivityVisible: false,
      user: [
        {
          title: "Min Profil",
          link: String,
          method: () => {
            console.log(this.user[0].link)
            if(this.user[0].link){
              console.log('Logged in; going to '+this.user[0].link)
              this.$router.push(this.user[0].link)
            }else{
              console.log('Not logged in, redirecting to login')
              this.$router.push('/login')
            }
          }
        },
        {
          title: "Logg Ut",
          link: "/",
          method: () => {
            userService.logout();
            this.$router.push("/");
          }
        },
      ],
      isLoggedIn: userService.isLoggedIn(),
      notifications: [{}],
      activities: {},
      search: '',
      isLoading: false,
      results: [],
      isOpen: false,
      arrowCounter: -1,
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
  },created() {
    if(userService.getTokenString()){
      this.getUserId()
    }
  },
  methods: {

    async getUserId(){
    let res = await userService.getAccountByEmail(userService.getAccountDetails().sub)
    this.user[0].link = '/accounts/' + res.id;
    },

    resultClicked(resultId){
      console.log(this.$route.path)
      //if((this.$router.currentRoute.path !== `/dashboard/activity/${resultId}`)){
        this.$router.push({ name: 'Activity', params: { id: resultId }}).then((route) => {
          if(route.path === this.$route.path) {
            throw new Error('Navigation Duplicated')
          }
        }).catch((error) => { console.error(error)});
      //}
      this.isOpen = false;
      this.search = '';
    },
    toggleCreateActivity() {
      if (this.isCreateActivityVisible === false) {
        this.isCreateActivityVisible = true;
      } else {
        this.isCreateActivityVisible = false;
      }
    },
    getActivities() {
        const requestOptions ={
            method: 'GET',
        }

        // Get all registered activites from database
        fetch("http://localhost:8080/activities/", requestOptions)
            .then(response => response.json())
            .then(data => {
                this.activities = data;
                console.log(data[0]);
            })
            .catch(error => console.log(error))
    },
    filterResults() {
      console.log(this.search);
      this.results = [];
      for(let i = 0; i < this.activities.length; i ++){
        if(this.activities[i].title.toLowerCase().includes(this.search.toLowerCase())){
          this.results.push(this.activities[i]);
        }else if(this.activities[i].creator.email.toLowerCase().includes(this.search.toLowerCase())){
          this.results.push(this.activities[i])
        }
      }
    },
    onChange() {
      this.$emit('input', this.search);
      if(this.isAsync) {
        this.isLoading = true;
      }else {
        this.filterResults();
        this.isOpen = true;
      }
    },
    setResult(result) {
      this.search = result;
      this.isOpen = false;
    },
    handleClickOutside(event) {
      if(!this.$el.contains(event.target)) {
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
    onEnter() { //fiks onEnter gjør samme som click
      this.search = this.results[this.arrowCounter];
      this.arrowCounter = -1;
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
  margin: 0px 10px;
  width: 300px;
  align-content: center;
  align-self: center;
  position: relative;
}

.search-box::placeholder {
  color: rgb(50, 50, 50);
}

input {
  padding: 0 20px;
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
    position:sticky;
  }

  .autocomplete-result {
    list-style: none;
    text-align: left;
    padding: 4px 2px;
    cursor: pointer;
    padding-bottom: 0px;
  }

  .email {
    font-size: small;
    display: block;
  }

  .title {
    font-weight: bold;
  }

  .autocomplete-result.is-active,
  .autocomplete-result:hover {
    background-color: #FFBD3E;
    color: white;
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
