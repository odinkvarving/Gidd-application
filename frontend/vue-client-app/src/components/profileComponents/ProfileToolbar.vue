<template>
  <div class="profile_buttons">
    <button id="calendarBtn" class="tabBtn"
          @click="switchComponent('profileCalendar')"
          :disabled="currentComp==='profileCalendar'">Calendar</button>
    <button id="activityBtn" class="tabBtn"
            @click="switchComponent('profileActivity')"
            :disabled="currentComp==='profileActivity'">Activities</button>
    <button id="editBtn" class="tabBtn"
            @click="switchComponent('profileEdit')"
            :disabled="currentComp==='profileEdit'">Edit profile</button>
  </div>
</template>

<script>
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
