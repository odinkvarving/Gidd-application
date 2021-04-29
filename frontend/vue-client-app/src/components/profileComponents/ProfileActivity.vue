<template>
  <div class="activity">
    <h1>Aktiviteter</h1>
    <div class="profileActivityDisplay">

      <div class="profileActivityID" ><b>ID</b></div>
      <div class="profileActivityTitle"><b>TITTEL</b></div>
      <div class="profileActivityStartTime"><b>START TID</b></div>
    </div>
    <hr>
    <div v-for="activity in activities" :key="activity.id" class="profileActivityDisplay">

      <div class="profileActivityID" >{{activity.id}}</div>
      <div class="profileActivityTitle">{{activity.title}}</div>
      <div class="profileActivityStartTime">{{activity.startTime.toLocaleString()}}</div>

    </div>

  </div>
</template>

<script>
/**
 * @Author Kevin Andre Helgeland
 * Component for displaying a list of all activities for a given account
 * Props:
 * @property activities  JSON Array object
 *
 * Expected input:
 *  * id: Number
 *  * title: String {Optional}
 *  * start_time: Date
 *  methods:
 *  @method scrollToEnd()
 *  This method allows us to make the activityList scrollable
 */
export default {
  name: "ProfileActivity",
  props:{
    activities: Array
  },
  methods:{
    scrollToEnd(){
      let activityList=this.$el.querySelector('.profileActivityDisplay');
      activityList.scrollTop=activityList.scrollHeight;
    }
  },
  mounted() {
    this.scrollToEnd()
  }


}
</script>

<style scoped>

</style>
