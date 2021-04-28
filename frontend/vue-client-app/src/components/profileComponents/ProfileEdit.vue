<template>
  <div class="edit">
    <h1 class="edit-title" >Edit Profile</h1><br>
    <form class="edit-form" @submit="checkform">
      <div v-if="errors.length">
        <b>Unable to edit account information for the following reasons:</b>
        <ul class="error-list">
          <li v-for="error in errors" :key="error">{{error}}</li>
        </ul>
      </div>
      <fieldset class="edit-general">
        <legend>General account information</legend>
        <div class="edit-name">
          <label for="first-name-form">First name:</label>
          <input type="text" id="first-name-form"  v-model="firstName" :placeholder="AccountInfo.firstname">
          <label for="last-name-form">Last name:</label>
          <input type="text" id="last-name-form" v-model="lastName" :placeholder="AccountInfo.surname">
        </div>
        <div class="edit-image">
          <label for="image-form">New profile image:</label>
          <input type="text" id="image-form" v-model="imageURL" placeholder="Link to image here">
        </div>
        <div class="edit-notification-settings">
          <label style="font-size: 20px; margin: 20px 10px 10px 10px">Edit notification settings</label>
          <div class="on-cancelled-activity notification-settings-item">
            <label for="">Når aktivitet blir avlyst: </label>
            <b-form-select v-model="cancelledActivityOption" :options="notificationOptions">
                <b-form-select-option :value="null" disabled>-- Velg et alternativ --</b-form-select-option>
            </b-form-select>
          </div>
          <div class="on-edited-activity notification-settings-item">
            <label for="">Når aktivitet blir endret: </label>
            <b-form-select v-model="editedActivityOption" :options="notificationOptions">
                <b-form-select-option :value="null" disabled>-- Velg et alternativ --</b-form-select-option>
            </b-form-select>
          </div>
          <div class="on-out-of-queue notification-settings-item">
            <label for="">Når du får plass i aktivitet: </label>
            <b-form-select v-model="outOfQueueOption" :options="notificationOptions">
                <b-form-select-option :value="null" disabled>-- Velg et alternativ --</b-form-select-option>
            </b-form-select>
          </div>
        </div>
        <div class="edit-description">
          <label for="description-form">Description</label>
          <textarea type="text" id="description-form" rows=5 cols=80 placeholder="Please tell us something about you|" v-model="description"></textarea>
        </div>

      </fieldset>
      <input type="button" @click="sendChangePasswordForm" id="change-password" value="Send change password form">
      <input type="password" v-model="old_password" id="old-password-form" placeholder="Old password to save">
      <input type="submit" value="Save changes" id="edit-submit">

    </form>
  </div>
</template>

<script>
import { notificationService } from '../../services/NotificationService';
import {userService} from "../../services/UserService";

export default {
  name: "ProfileEdit",
  data(){
    return{
      errors:[],
      firstName:"",
      lastName:"",
      description:"",
      imageURL:"",
      old_password:"",
      notificationOptions: ["Notifikasjon og mail", "Kun notifikasjon", "Kun mail", "Ingen av delene"],
      cancelledActivityOption: null,
      editedActivityOption: null,
      outOfQueueOption: null,
      notificationSettings: {},
    }
  },
  props:{
    AccountInfo:Object
  },
  async mounted(){
    notificationService.getAccountsNotificationSettings(this.$route.params.userId)
      .then(data => {
        this.notificationSettings = data;
        console.log(this.notificationSettings);
      });
  },
  methods:{
    exists(obj){
      return (obj!==null&&typeof obj!=='undefined'&&obj!=="")
    },
    validURL(str){
      let pattern = new RegExp('^(https?:\\/\\/)?'+ // protocol
          '((([a-z\\d]([a-z\\d-]*[a-z\\d])*)\\.)+[a-z]{2,}|'+ // domain name
          '((\\d{1,3}\\.){3}\\d{1,3}))'+ // OR ip (v4) address
          '(\\:\\d+)?(\\/[-a-z\\d%_.~+]*)*'+ // port and path
          '(\\?[;&a-z\\d%_.~+=-]*)?'+ // query string
          '(\\#[-a-z\\d_]*)?$','i'); // fragment locator
      return !!pattern.test(str);
    },
    async sendChangePasswordForm(){
      console.log("Sending mail for password change to "+this.AccountInfo.email)
      fetch("http://localhost:8080/reset/"+this.AccountInfo.email,{
        method:"POST",
      });
    },
    async checkform(e){
      this.errors=[]

      let newAccount=this.$parent.originalAccount
      if(this.exists(this.firstName)){
        console.log('Changing first name to '+this.firstName)
        newAccount.firstname = this.firstName
      }
      if(this.exists(this.lastName)){
        console.log('Changing last name to '+this.lastName)
        newAccount.surname=this.lastName
      }

      if(this.exists(this.description)){
        console.log('changing description to '+this.description)
        newAccount.profileDescription=this.description
      }

      if(this.cancelledActivityOption !== null){
        this.getOnCancelledSettings();
      }
      
      if(this.editedActivityOption !== null){
        this.getOnEditedSettings();
      }

      if(this.outOfQueueOption !== null){
        this.getOnOutOfQueueSettings();
      }

      if(this.exists(this.imageURL)){
        if(this.validURL(this.imageURL)||this.imageURL===""){
          console.log('Changing image url to '+this.imageURL)
          newAccount.imageURL=this.imageURL
        }else{
          this.errors.push('This is not a valid URL')
        }
      }
      if(userService.getAccountDetails().sub===this.AccountInfo.email) {
        userService.login(this.AccountInfo.email, this.old_password).then(res => {
          console.log('getting jwtToken')
          if (!res.jwtToken) {
            console.log('Authentication failed')
            this.errors.push('Old password does not match the account')
          }else console.log('Login successful')
        })
      }else{
        this.errors.push('Your account does not have the rights to change the account for ' + this.AccountInfo.email)
      }
      if(this.errors.length){
        console.log('preventing post call '+this.errors.length+ ' errors')
        e.preventDefault()
      }else{

        console.log('sending following json')
        console.log(newAccount)
        if(this.exists(this.firstName)||this.exists(this.lastName)||
            this.exists(this.description)||this.exists(this.imageURL)){
              newAccount.notificationSettings = this.notificationSettings;
          let res=userService.setAccount(newAccount,this.$route.params.userId);
          console.log(res)
        }
      }
      if(this.getOnCancelledSettings !== null || this.getOnEditedSettings !== null 
          || this.outOfQueueOption !== null){
            console.log(this.notificationSettings.id);
            let res = notificationService.updateAccountsNotificationSettings(this.$route.params.userId, this.notificationSettings);
            console.log(res);
      }
    },
    getOnCancelledSettings(){
      if(this.cancelledActivityOption === this.notificationOptions[0]){
        this.notificationSettings.wantsActivityCancelledNotifications = true;
        this.notificationSettings.wantsActivityCancelledMails = true;
      }
      if(this.cancelledActivityOption === this.notificationOptions[1]){
        this.notificationSettings.wantsActivityCancelledNotifications = true;
        this.notificationSettings.wantsActivityCancelledMails = false;
      }
      if(this.cancelledActivityOption === this.notificationOptions[2]){
        this.notificationSettings.wantsActivityCancelledNotifications = false;
        this.notificationSettings.wantsActivityCancelledMails = true;
      }
      if(this.cancelledActivityOption === this.notificationOptions[3]){
        this.notificationSettings.wantsActivityCancelledNotifications = false;
        this.notificationSettings.wantsActivityCancelledMails = false;
      }
    },
    getOnEditedSettings(){
      if(this.editedActivityOption === this.notificationOptions[0]){
        this.notificationSettings.wantsActivityChangedNotifications = true;
        this.notificationSettings.wantsActivityChangedMails = true;
      }
      if(this.editedActivityOption === this.notificationOptions[1]){
        this.notificationSettings.wantsActivityChangedNotifications = true;
        this.notificationSettings.wantsActivityChangedMails = false;
      }
      if(this.editedActivityOption === this.notificationOptions[2]){
        this.notificationSettings.wantsActivityChangedNotifications = false;
        this.notificationSettings.wantsActivityChangedMails = true;
      }
      if(this.editedActivityOption === this.notificationOptions[3]){
        this.notificationSettings.wantsActivityChangedNotifications = false;
        this.notificationSettings.wantsActivityChangedMails = false;
      }
    },
    getOnOutOfQueueSettings(){
      if(this.outOfQueueOption === this.notificationOptions[0]){
        this.notificationSettings.wantsOutOfQueueNotifications = true;
        this.notificationSettings.wantsOutOfQueueMails = true;
      }
      if(this.outOfQueueOption === this.notificationOptions[1]){
        this.notificationSettings.wantsOutOfQueueNotifications = true;
        this.notificationSettings.wantsOutOfQueueMails = false;
      }
      if(this.outOfQueueOption === this.notificationOptions[2]){
        this.notificationSettings.wantsOutOfQueueNotifications = false;
        this.notificationSettings.wantsOutOfQueueMails = true;
      }
      if(this.outOfQueueOption === this.notificationOptions[3]){
        this.notificationSettings.wantsOutOfQueueNotifications = false;
        this.notificationSettings.wantsOutOfQueueMails = false;
      }
    }
  }
}
</script>

<style scoped>
  
  .edit-notification-settings {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }

  .notification-settings-item {
    width: 300px;
    text-align: center;
    margin: 10px 0;
  }
</style>
