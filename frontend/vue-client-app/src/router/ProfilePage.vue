<template>
  <div class="profile_container">
    <NavBar />
    <div class="profile_info">
      <img :src="AccountInfo.imageURL" @error="AccountInfo.imageURL" alt="Profil bilde"
         width="170" height="170"
         id="profile_photo">
      <div id="info-list">
        <div class="info-list-item">
          <p>{{ AccountInfo.email }}</p>
          <b-icon icon="mailbox"></b-icon>
        </div>
        <div class="info-list-item">
          <p>{{ AccountInfo.phone }}</p>
          <b-icon icon="telephone"></b-icon>
        </div>
      </div>
    </div>
    <div>
      <div class="header-info">
        <h1 id="name">{{ AccountInfo.firstname }} {{ AccountInfo.surname }}</h1>
        <p id="description">{{ AccountInfo.description }}</p>
      </div>
        <div class="profile-info-small">
          <img :src=AccountInfo.imageURL @error="AccountInfo.imageURL" alt="Profil bilde"
            width="170" height="170"
            id="profile_photo">
          <div id="info-list">
            <div class="info-list-item">
              <p>{{ AccountInfo.email }}</p>
              <b-icon icon="mailbox"></b-icon>
            </div>
            <div class="info-list-item">
              <p>{{ AccountInfo.phone }}</p>
              <b-icon icon="telephone"></b-icon>
            </div>
          </div>
      </div>
      <div class="display_container">
          <profileToolbar :current-comp="currentComp" @switchComp="sendComp"></profileToolbar>
          <component v-bind="activityProperties" :is="currentComp" ></component>
      </div>
    </div>
  </div>
</template>

<script>
import '../assets/styles/profile.css';
import fetch from "node-fetch";
import ProfileToolbar from '../components/profileComponents/ProfileToolbar';
import ProfileCalendar from "../components/profileComponents/ProfileCalendar";
import ProfileActivity from "../components/profileComponents/ProfileActivity";
import ProfileEdit from "../components/profileComponents/ProfileEdit";
import { userService } from '../services/UserService';
import NavBar from "../components/Nav/NavBar.vue"

let path_to_db='http://localhost:8080/';
/**
 * @Author Kevin Andre Helgeland
 * Component for displaying the profile page
 * Methods:
 * @method getImgUrl(String)
 * This is a method for creating an image url that is accessible for the user
 * @method sendComp(String)
 * This is a method to send information to profile toolbar in order to disable the correct
 * button and display the correct component
 * @method getUserinfo()
 * This method is created to fetch all information on user based on id in url.
 * After fetching the data, it reads the data and displays it
 * @method getAllActivities()
 * This method i created to fetch all activities that the given user is registered to
 * Computation:
 * @method activityProperties()
 * this method is created to format the activity data to fit either profileCalendar
 * or profileActivity
 * @data currentComp default: profileCalendar
 * This is the variable that decides which component is currently showing.
 * @data userInfo
 * This is a JSON object that holds all information about the user
 * @data activities
 * This is a JSON array that holds all activities that the account is registered to
 */
export default {
  name: "ProfilePage",
  methods:{
    sendComp(newComp){
      console.log(newComp)
      this.currentComp=newComp

    },
    getDefaultImg(){
      this.AccountInfo.imageURL=require("../assets/Default_profile.png")
    },

    getUserInfo(){
      console.log(this.$route.params.userId)
      //Getting the user data
      fetch(path_to_db+`accounts/${this.$route.params.userId}/info`,{
        method:'GET',
        headers:{
          'Content-Type':'application/json',
          'Authorization': userService.getTokenString()
        }
        //Unpacking the data
      }).then(res=>{
        return res.json()
      })
          //Using the data
          .then(data=>{
            console.log('Getting account')
            console.log(data);
            this.originalAccount=data
            if(data.firstname!==null&&typeof data.firstname !== 'undefined'){
              this.AccountInfo.firstname=data.firstname;
            }
            if(data.surname!==null&&typeof data.surname !== 'undefined'){
              this.AccountInfo.surname=data.surname;
            }
            if(data.imageURL!==null&&typeof data.imageURL !== 'undefined'){
              this.AccountInfo.imageURL=data.imageURL;
            }
            if(data.phone!==null&&typeof data.phone !== 'undefined'){
              this.AccountInfo.phone=data.phone;
            }
            if(data.address!==null&&typeof data.address !== 'undefined') {
              this.AccountInfo.address = data.address;
            }
            if(data.account.email!==null&&typeof data.account.email !== 'undefined') {
              this.AccountInfo.email = data.account.email;
            }
            if(data.profileDescription!==null&&typeof data.profileDescription !== 'undefined'){
              this.AccountInfo.description=data.profileDescription;
            }

          })
          .catch(e=>console.log(e))
    },
    getAllActivities(){
      fetch(path_to_db+`accounts/${this.$route.params.userId}/activities`,{
        method:'GET',
            headers:{
          'Content-Type':'application/json',
          'Authorization': userService.getTokenString()
        }
      }).then(res=>res.json())
      .then(data=>{
        if(data!==null&&typeof data!=='undefined'&&typeof data.error==='undefined'){
          console.log(data)
          this.activities=data
        }
      })
      .catch(e=>console.error(e))
    }
  },
  components:{
    'ProfileToolbar':ProfileToolbar,
    'profileCalendar':ProfileCalendar,
    'profileActivity':ProfileActivity,
    'profileEdit':ProfileEdit,
    NavBar
  },
  computed:{
    activityProperties(){
      //Calendar component
      if(this.currentComp==='profileCalendar'){
        console.log(this.activities)
        return {activities:this.activities}
      }else//profileActivity component
        if(this.currentComp==='profileActivity'){
        let events=[]
        this.activities.forEach(item => {
          if (item.title === null || typeof item.title === 'undefined') {
            item.title = 'Ingen navn'
          }
          events.push({
            id: item.id,
            title: item.title,
            startTime: item.startTime,
            latitude: item.latitude,
            longitude: item.longitude,
            location: item.location
          })
        })
        //ProfileActivity.scrollToEnd();
        console.log('To activity list '+events)
        return {activities:events}
      }else{//Errors and Edit component
        console.log(this.AccountInfo)
        return {AccountInfo:this.AccountInfo}
      }
    }
  },
  data(){
    return{
    currentComp: 'profileCalendar',
    originalAccount:Object,
    AccountInfo:{
      firstname:"Ingen fornavn",
      surname:"Ingen etternavn",
      imageURL:"../assets/Default_profile.png",
      email:"Ingen email funnet",
      description:"Ingen beskrivelse funnet"
    },
    activities:[{
        id:-1,
        title:'Debugging activity',
        description:'Vue js 101 with some vanilla js sprinkled in for good measure',
        start_time:new Date('2021-04-22T08:00:00'),
        end_time:new Date('2021-04-22T11:00:00')},

        {id:-2,
        title:'Debugging calendar',
        description:'vue-cal seems to be kind of wonky',
        start_time:new Date(),
        end_time:new Date(new Date().getTime()+(60*60000))
      }]
    }
  },

  created() {
    this.getUserInfo()
    this.getAllActivities()
  }
}
</script>

<style scoped>

  .profile_container {
    padding-top: 200px;
    background-color: #f6f6f6;
    width: 99vw;
    display: flex;
    min-height: 100vh;
  }

  .profile_info{
    width: 400px;
    margin-right: 2vw;
    border-right: 1px solid rgb(155, 153, 153);
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .profile-info-small {
    display: none;
  }

  .header-info {
    width: 75vw;
  }

  .display_container {
    margin-right: 30px;
  }

  #info-list {
    display: flex;
    flex-direction: column;
    font-weight: bold;
    margin-top: 70px;
    margin: 70px 10px 10px 10px;
  }

  #info-list div{
    margin: 5px 0;
  }

  .info-list-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  .info-list-item p{
    margin: 0 10px 0 0;
  }

  @media (max-width: 1225px) {
    .profile_info {
      display: none;
    }

    .profile_container {
      flex-direction: column;
    }

    .display_container{
      margin-left: 20px;
      margin-right: 20px;
    }

    .profile-info-small {
      display: flex;
      align-items: center;
      justify-content: space-around;
      margin-bottom: 20px;
    }

    .header-info{
      margin-left: 30px;
    }
  }

  @media (max-width: 450px) {

    .profile_container{
      padding-top: 130px;
    }

    .header-info h1{
      font-size: 25px;
    }

    .header-info p{
      font-size: 15px;
      opacity: 80%;
    }

    .profile-info-small img{
      width: 100px;
      height: 100px;
    }
  }


</style>
