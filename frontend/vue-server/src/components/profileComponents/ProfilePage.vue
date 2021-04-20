<template>
  <div class="profile_container">
    <img src="../../assets/Default_cover.jpg" alt="Cover photo"
         id="cover_photo"/>
    <img :src=userInfo.imageURL alt="Profile photo"
         width="170" height="170"
         class="headshot" id="profile_photo">
    <div class="headshot">
      <h1 >{{userInfo.firstname}} {{userInfo.surname}}</h1>
      <p>{{userInfo.description}}</p>
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
import '../../assets/styles/profile.css';
import fetch from "node-fetch";

let path_to_db='http://localhost:8081/';
/**
 * @Author Kevin Andre Helgeland
 * Component for displaying the profile page
 * @method getImgUrl(String)
 * This is a method for creating an image url that is accessible for the user
 */
export default {
  name: "ProfilePage",
  methods:{
    getImgUrl(imgName){
      let image=require.context('../../assets/',false,/\.png$/);
      console.log(image('./'+imgName+'.png'))
      return image('./'+imgName+'.png');
    }
  },
  data(){
    return{
    userInfo:{
      firstname:"No first name",
      surname:"No last name",
      imageURL:this.getImgUrl('Default_profile'),
      telephone:"No telephone number found",
      address:"No address found",
      email:"No email found",
      description:"No description fount"
      }
    }
  },

  mounted() {
    console.log(this.$route.params.userId)
    fetch(path_to_db+'users/'+this.$route.params.userId,{
        method:'get',
      headers:{
          'Content-Type':'application/json'
      }

  }).then(res=>{
    return res.json()
  })
    .then(data=>{
      console.log('Getting account')
      console.log(data);

      if(data.firstname!==null||typeof data.firstname !== 'undefined'){
        this.userInfo.firstname=data.firstname;
      }
      if(data.telephone!==null||typeof data.telephone !== 'undefined'){
        this.userInfo.surname=data.surname;
      }
      if(data.imageURL!==null||typeof data.imageURL !== 'undefined'){
        this.userInfo.imageURL=this.getImgUrl(data.imageURL);
      }
      if(data.telephone!==null||typeof data.telephone !== 'undefined'){
        this.userInfo.telephone=data.telephone;
      }
      if(data.address!==null||typeof data.address !== 'undefined') {
        this.userInfo.address = data.address;
      }
      if(data.email!==null||typeof data.email !== 'undefined') {
        this.userInfo.email = data.email;
      }
      if(data.description!==null||typeof data.description !== 'undefined'){
        this.userInfo.description=data.description;
      }

    })
    .catch(e=>console.log(e))
  }
}
</script>

<style scoped>
</style>