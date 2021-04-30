<template>
  <div style="margin-bottom: 40px; ">
    <vue-cal
        :time-from="6*60"
        :time-to="23*60"
        :time-step="60"
        :events="getEvents"
        :disable-views="['years','year','month']"
        :on-event-click="onEventClick"
        locale="no"
        cell-contextmenu/>
    <b-modal id="activity_modal" hide-footer="true" title="Activity pop-up">
      <Activity
          :activity="selectedEvent"
          :isLoggedIn="loggedIn"
          v-model="showEvent"/>
    </b-modal>

  </div>
</template>

<script>
import VueCal from 'vue-cal'
import 'vue-cal/dist/vuecal.css'
import 'vue-cal/dist/i18n/no.js'
import Activity from "../ActivityFeedComponents/Activity";
import {userService} from "../../services/UserService";

/**
 * @author Kevin Andre Helgeland
 * This is a component for viewing a calendar using 'vue-cal'
 * @property activities JSON Array object
 *
 * Expppected input:
 *  * start: Date
 *  * end: Date {Optional; if missing, 'allDay' will beset to true}
 *  * title: String {Optional}
 *  * allDay: false {true if end is null}
 *  * class:String{Optional; if missing, can be edited in .css from .vuecal__event.normal-event.
 *  If provided, you can style the event in a .css doc using .vuecal__event.<String>}
 */
export default {
  name: "ProfileCalendar",
  components:{Activity, 'vue-cal':VueCal},
  props:{
    activities:{
      type:Array,
      required:true
    }
  },
  data(){
    return{
      selectedEvent:{},
      showEvent:false,
      loggedIn:false
    }},
  methods:{
    onEventClick(event,e){
      this.selectedEvent=this.activities.find(element=> event.id===element.id)
      this.showEvent=true
      this.$bvModal.show('activity_modal')
      console.log('event has been clicked')
      e.stopPropagation()
    },
    async isLoggedIn(){
      let res=await userService.isLoggedIn();
      console.log('isLoggedIn response:'+res)
      this.loggedIn= !!res;

    }

  },
  computed:{
    getEvents() {
      console.log('getEvents()')
      console.log(this.activities)
      let events = []
      if (this.activities) {
        this.activities.forEach(item => {
          let isAllDay = false;
          if (item.endTime === null || typeof item.endTime === 'undefined') {
            isAllDay = true
            item.endTime = 'undefined'
          }
          if (item.title === null || typeof item.title === 'undefined') {
            item.title = 'Ingen navn'
          }
          events.push({
            id:item.id,
            start: new Date(item.startTime),
            end: new Date(item.endTime),
            title: item.title,
            allDay: isAllDay,
            class: "normal-event"
          })
        })
        console.log('To Calendar:')
        console.log(events)
        return events
      }
      console.log('No events')
      return []
    }
  }

}
</script>

<style scoped>

</style>
