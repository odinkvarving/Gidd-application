<template>
  <vue-cal
      :time-from="6*60"
      :time-to="23*60"
      :time-step="60"
      :events="activities"
      :disable-views="['years','year','month']"
      cell-contextmenu/>
</template>

<script>
import VueCal from 'vue-cal'
import 'vue-cal/dist/vuecal.css'

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
  components:{'vue-cal':VueCal},
  props:{
    activities: Array
  }
}
</script>

<style scoped>

</style>
