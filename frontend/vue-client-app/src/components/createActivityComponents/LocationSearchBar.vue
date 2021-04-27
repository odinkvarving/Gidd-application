<template>
  <div id="search-container">
      <GmapAutocomplete @place_changed="setLocation" style="width: 100%; height: 30px;" placeHolder="Legg til sted..."/>
  </div>
</template>

<script>

export default {
    name: "LocationSearchBar",
    data(){
        return{
            currentLocation: null,
            center: {lat: 1, lng: 1},
            geometryFound: false,
            newLocation: true
        }
    },
    methods: {
        setLocation(location){
            this.$emit("setPlace", location);
        }   
    },
}
</script>

<style scoped>

</style>