<template>
  <div class="profile_buttons">
    <b-button id="calendarBtn" class="tabBtn" @click="switchComponent('profileCalendar')" :disabled="currentComp==='profileCalendar'">
        Kalender
        <b-icon icon="calendar"></b-icon>
    </b-button>
    <b-button id="activityBtn" class="tabBtn" @click="switchComponent('profileActivity')" :disabled="currentComp==='profileActivity'">
      Aktiviteter
      <b-icon icon="bicycle"></b-icon>
    </b-button>
    <b-button id="editBtn" class="tabBtn" @click="switchComponent('profileEdit')" :disabled="currentComp==='profileEdit'">
      Endre profil
      <b-icon icon="calendar"></b-icon>    
    </b-button>
  </div>
</template>

<script>
/**
 * @author Kevin Andre Helgeland
 *This is a component to have a toolbar for switching components
 *
 * methods:
 * @method switchComponent(String)
 * This is the method that emits the component that the user has selected to front end so that
 * the parent component knows what to switch to
 */

export default {
  name: "ProfileToolbar",
  props:{
    currentComp:{
      type:String,
      required:true
    }
},
  emits:['switchComp'],
  methods:{
    switchComponent(comp){
      console.log('switching component to '+comp);
      this.$emit('switchComp',comp);
    }
  },


}
</script>

<style scoped>
</style>
