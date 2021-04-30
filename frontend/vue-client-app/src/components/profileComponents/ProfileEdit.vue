<template>
  <div class="edit">
    <h1 class="edit-title" >Endre profil</h1><br>
    <form class="edit-form" @submit="checkform">
      <div v-if="errors.length">
        <b>Kan ikke endre profil av følgende grunner:</b>
        <ul class="error-list">
          <li v-for="error in errors" :key="error">{{error}}</li>
        </ul>
      </div>
      <fieldset class="edit-general">
        <label class="second-header" style="font-size: 20px; font-weight: bolder; margin-top: 50px">Generell brukerinformasjon</label>
        <div class="edit-name">
          <div class="personal-info">
            <div class="personal-item">
              <label for="first-name-form">Fornavn:</label>
              <input type="text" id="first-name-form"  v-model="firstName" :placeholder="AccountInfo.firstname">
            </div>
            <div class="personal-item">
              <label for="last-name-form">Etternavn:</label>
              <input type="text" id="last-name-form" v-model="lastName" :placeholder="AccountInfo.surname">
            </div>
            <div class="personal-item">
              <label for="image-form">Profilbilde:</label>
              <input type="file" accept="image/png,image/gif,image/jpeg" id="image-form" value="Velg fil" @change="handleFiles($event)">            
            </div>
            </div>
          <div class="edit-description">
            <label for="description-form">Beskrivelse</label> 
            <textarea type="text" id="description-form" rows=5 cols=80 placeholder="Vennligst fortell oss noe om deg selv" v-model="description"></textarea>
          </div>
        </div>
        <label class="second-header" style="font-size: 20px; font-weight: bolder; margin: 20px 10px 10px 10px">Endre notifikasjonsinstilinger</label>
        <div class="edit-notification-settings">
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

      </fieldset>
      
      <div class="submit-container">
        <b-button type="submit" variant="success" value="Lagre endringer" id="edit-submit">Lagre</b-button>
      </div>
      
      <h1 class="edit-title" >Endre passord</h1><br>
      <div class="password-change-container">
        <b-button type="button" @click="sendChangePasswordForm" id="change-password">Endre passord</b-button>
      </div>
    </form>
  </div>
</template>

<script>
/**
 * @author Kevin Andre Helgeland
 * This is the component that displays the input fields for editing your own profile
 * props:
 * @property accountInfo JSON
 * This is a property that holds the account from backend
 * methods:
 * @method exists(obj)
 * This method checks if a value is not null. undefined or empty string.
 * @method validFile(obj)
 * This is a method that checks if the input file is of a valid format
 * @method async sendChangePasswordForm()
 * This is a method that sends a mail to the profile mail for changing the password
 * @method checkForm(event)
 * This is a method that checks if the values in the form is ok
 * @method handleFiles(event)
 * This is a method that readies an image for upload
 * @method getOnCancelledSettings()
 * This method changes the setting for how notifications should be handled if an activity that the user is
 * registered in is cancelled
 * @method getOnEditedSettings()
 * This method changes the setting for how notifications should be handled if an activity that the user is
 * registered in is edited
 * @method getOnOutOfQueueSettings()
 * This method changes the setting for how notifications should be handled if an activity that the user is
 * in queue in gives the user a spot
 */

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
      old_password:"",
      newImageFile:{},
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
    validFile(image){
      return image.type==='image/jpeg'||image.type==='image/gif'||image.type==='image/png'
    },
    async sendChangePasswordForm(){
      console.log("Sending mail for password change to "+this.AccountInfo.email)
      fetch(`http://localhost:8080/reset/${this.AccountInfo.email}`,{
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
      if(userService.getAccountDetails().sub===this.AccountInfo.email) {
        console.log("ok")
      }else{
        this.errors.push('Denne brukeren kan ikke gjøre endringer på ' + this.AccountInfo.email+' sin bruker.')
      }
      if(this.errors.length){
        console.log('preventing post call '+this.errors.length+ ' errors')
        e.preventDefault()
      }else{
        console.log('sending following json')
        console.log(newAccount)
        if(this.exists(this.firstName)||this.exists(this.lastName)||
            this.exists(this.description)){
              newAccount.notificationSettings = this.notificationSettings;
          let res= await userService.setAccount(newAccount,this.$route.params.userId);
          console.log('setAccount res:'+res)
        }
        console.log('Current image status')
        console.log(this.newImageFile)

        if(this.exists(this.newImageFile)){
          let formData=new FormData()
          formData.append('file',this.newImageFile)
          let res= await userService.sendImage(formData,this.$route.params.userId)
          console.log('sendImage res'+res)
        }

      }
      if(this.getOnCancelledSettings !== null || this.getOnEditedSettings !== null
          || this.outOfQueueOption !== null){
            console.log(this.notificationSettings.id);
            let res = notificationService.updateAccountsNotificationSettings(this.$route.params.userId, this.notificationSettings);
            console.log(res);
      }
    },
    handleFiles(event){
      console.log('The image is being set to:')
      console.log(event.target.files[0])
      this.newImageFile=event.target.files[0]
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
    flex-direction: row;
    align-items: center;
    justify-content: space-between;
  }

  .notification-settings-item {
    width: 300px;
    text-align: center;
    margin: 10px 0;
  }

  .edit-name{
    display: flex;
    align-items: center;
    flex-direction: column;
    justify-content: space-between;
  }

  .personal-info {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }

  #first-name-form{
    width: 200px;
  }

  #last-name-form{
    width: 200px;
  }

  #image-form {
    width: 300px;
  }

  .personal-item {
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .personal-item label{
    width: 140px;
  }

  .edit-description {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .edit-description textarea{
    width: 70%;
  }

  .submit-container {
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 30px;
    margin-bottom: 50px;
  }

  #edit-submit{
    width: 200px;
    height: 50px;
  }

  .password-change-container {
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin-bottom: 50px;
    margin-top: 25px;
  }

  .password-change-container input{
    width: 40%;
    height: 50px;
    margin: 10px;
  }

  #change-password {
    width: 40%;
    height: 50px;
  }

  @media (max-width: 1270px) {
    .edit-notification-settings {
      flex-direction: column;
    }

    .personal-info{
      flex-direction: column;
    }

    .personal-item {
      margin: 5px 0;
    }

    .edit-title{
      text-align: center;
    }

    .second-header{
      text-align: center;
    }

  }

  @media (max-width: 750px) {
    .edit-description textarea{
      width: 100%;
    }
  }
  
  </style>
