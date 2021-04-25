<template>
  <div id="search-container">
      <GmapAutocomplete @place_changed="setLocation" style="width: 100%;"/>
  </div>
</template>

<script>
export default {
    name: "LocationSearchBar",
    data(){
        return{
            currentLocation: null
        }
    },
    methods: {
        setLocation(location){
            this.currentLocation = location;
            console.log(this.currentLocation);
        }
    },
}
</script>

<style scoped>



</style>