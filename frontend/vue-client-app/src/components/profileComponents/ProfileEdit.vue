<template>
  <div class="edit">
    <h1>Edit Profile</h1><br>
    <form class="edit-form">
      <fieldset>
        <legend>General account information</legend>
        <div class="edit-name">
          <label for="first-name-form">First name:</label>
          <input type="text" id="first-name-form" :placeholder="userInfo.firstname">
          <label for="last-name-form">Last name:</label>
          <input type="text" id="last-name-form" :placeholder="userInfo.surname">
        </div>
        <div class="edit-description">
          <label for="description-form">Description</label>
          <textarea type="text" id="description-form" placeholder="Please tell us something about you|" v-model="getDescriptionPlaceholder">
          </textarea>
        </div>
        <div class="edit-image">
          <label for="image-form">New image:</label>
          <input type="text" id="image-form" placeholder="Link to image here">
        </div>
      </fieldset>
      <fieldset>
        <legend>Login credentials</legend>
        <div>
          <label for="email-form">Change e-mail:</label>
          <input type="email" id="email-form" :placeholder="userInfo.email">
        </div>
        <div class="edit-password">
          <label for="new-password-form">Change password:</label>
          <input type="password" id="new-password-form" placeholder="New password">
          <input type="password" id="confirm-password-form" placeholder="Confirm password">
        </div>
      </fieldset>
      <input type="submit" value="Save changes">

    </form>
  </div>
</template>

<script>
export default {
  name: "ProfileEdit",
  props:{
    userInfo:Object
  },
  computed:{
    getDescriptionPlaceholder(){
      if(this.userInfo.description==="No description found"){
        return ""
      }else{
        return this.userInfo.description
      }
    }
  }
}
</script>

<style scoped>

</style>
