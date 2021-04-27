<template>
  <div class="profile_container">
    <NavBar />
    <img src="../assets/Default_cover.jpg" alt="Cover photo"
         id="cover_photo"/>
    <img :src=AccountInfo.imageURL @error="getDefaultImg" alt="Profile photo"
         width="170" height="170"
         id="profile_photo">
    <div class="profile_info">
      <h1 id="name">{{ AccountInfo.firstname }} {{ AccountInfo.surname }}</h1>
      <p id="description">{{ AccountInfo.description }}</p>

      <ul id="info_list">
        <li>
          {{ AccountInfo.telephone }}
        </li>
        <li>
          {{ AccountInfo.address }}
        </li>
        <li>
          {{ AccountInfo.email }}
        </li>
      </ul>
      <profileToolbar :current-comp="currentComp" @switchComp="sendComp"></profileToolbar>
      <div class="display_container">
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
      fetch(path_to_db+'accounts/'+this.$route.params.userId+'/info',{
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
            if(data.telephone!==null&&typeof data.telephone !== 'undefined'){
              this.AccountInfo.telephone=data.telephone;
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
      fetch(path_to_db+'accounts/'+this.$route.params.userId+'/activities',{
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
        let events=[]
        this.activities.forEach(item => {
          let isAllDay = false;
          if (item.endTime === null || typeof item.endTime === 'undefined') {
            isAllDay = true
            item.endTime = 'undefined'
          }
          if (item.title === null || typeof item.title === 'undefined') {
            item.title = 'Unnamed activity'
          }
          events.push({
            start: new Date(item.startTime),
            end: new Date(item.endTime),
            title: item.title,
            allDay: isAllDay,
            class:"normal-event"
          })
        })
        console.log('To Calendar:')
        return {activities:events}
      }else//profileActivity component
        if(this.currentComp==='profileActivity'){
        let events=[]
        this.activities.forEach(item => {
          if (item.title === null || typeof item.title === 'undefined') {
            item.title = 'Unnamed activity'
          }
          events.push({
            id: item.id,
            title: item.title,
            startTime: item.startTime
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
      firstname:"No first name",
      surname:"No last name",
      imageURL:"../assets/Default_profile.png",
      telephone:"No telephone number found",
      address:"No address found",
      email:"No email found",
      description:"No description found"
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
</style>
