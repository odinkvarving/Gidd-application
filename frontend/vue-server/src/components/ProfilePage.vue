<template>
  <div class="profile_container">
    <img src="../assets/Default_cover.jpg" alt="Cover photo"
         id="cover_photo"/>
    <img src="../assets/Default_profile.png" alt="Profile photo"
         width="170" height="170"
         class="headshot" id="profile_photo">
    <div class="headshot">
      <h1 >Odin Kvarving</h1>
      <p>Studies at NTNU an playing super smush bros at all time</p>
    </div>
    <ul id="info_list">
      <li>
        {{userInfo.telephone}}
      </li>
      <li>
        {{userInfo.address}}
      </li>
      <li>
        {{userInfo.email}}
      </li>
    </ul>
    <div id="profile_buttons">
      <button>Activities</button>
      <button>Calendar</button>
      <button>Edit</button>
    </div>

  </div>
</template>

<script>
import '../assets/styles/profile.css';
import fetch from 'fetch'
//import axios from 'axios'

export default {
  name: "ProfilePage",
  el:'#info_list',
  data(){
    return{
    userInfo:{
      telephone:"No telephone number found",
      address:"No address found",
      email:"No email found"}
    }
  },
  mounted() {
    fetch('/{id}',{
        method:'get'
  }).then(res=>{

      if(res.telephone!==null||typeof this.userInfo.telephone !== 'undefined'){
        this.userInfo.telephone=res.telephone;
      }

      if(res.address!==null||typeof this.userInfo.address !== 'undefined') {
        this.userInfo.address = res.address;
      }

      if(res.email!==null||typeof this.userInfo.email !== 'undefined') {
        this.userInfo.email = res.email;
      }

    })
  }
}
</script>

<style scoped>
</style>