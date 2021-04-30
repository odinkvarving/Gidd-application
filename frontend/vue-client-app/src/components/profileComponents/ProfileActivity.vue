<template>
  <div class="activity">
    <h1>Aktiviteter</h1>
    <hr>
    <div v-for="activity in activities" :key="activity.id" class="profileActivityDisplay" @click="activityClicked(activity.id)">

      <div class="header-info">
        <p class="title"> {{ activity.title }} </p>
        <p class="date"> {{ activity.startTime.toLocaleString() }} </p>
        <p class="location"> {{ activity.location }} </p>
      </div>

      <div class="location">
        <GmapMap
            :center="{lat:parseInt(activity.latitude), lng:parseInt(activity.longitude)}"
            :zoom="11"
            map-type-id="roadmap"
            style="width: 200px; height: 130px"
            disableDefaultUI= "true"
            :options="{
              fullscreenControl: false,
              gestureHandling: 'none'
            }"
            >
        <GmapMarker
            :position="{lat:parseInt(activity.latitude), lng:parseInt(activity.longitude)}"
            @click="center={lat:parseInt(activity.latitude), lng:parseInt(activity.longitude)}"
        />
        </GmapMap>
      </div>
      <div class="profileActivityStartTime">
        <b-button variant="warning" style="color: white;" @click="activityClicked(activity.id)">Til aktivitet</b-button>
      </div>

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
    },
    activityClicked(activityId){
      this.$router.push(`/dashboard/activity/${activityId}`);
    }
  },
  mounted() {
    this.scrollToEnd();
    console.log(this.activities[0]);
  }


}
</script>

<style scoped>

.activity{
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.profileActivityDisplay{
  width: 100%;
  height: 200px;
  background-color: white;
  box-shadow: 0px 4px 4px 0px #0000001A;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
  margin: 15px 0;
  transition: 0.3s;
}

.profileActivityDisplay:hover{
  background-color: rgba(255, 255, 255, 0.164);
  cursor: pointer;
  transition: 0.3s;
}

.header-info {
  width: 30%;
}

.title{
  font-size: 20px;
  margin: 0 0 20px 0;
}

.date{
  font-size: 15px;
  opacity: 75%;
  margin: 10px 0 0 0;
}

.location{
  font-size: 15px;
  margin: 0;
}

@media (max-width: 650px) {
  .profileActivityDisplay{
    height: 500px;
    width: 350px;
    flex-direction: column;
    text-align: center;
    padding: 30px 0;
  }
}

</style>
