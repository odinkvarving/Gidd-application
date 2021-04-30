<template>
  <div id="search-container">
      <GmapAutocomplete @place_changed="setLocation" style="width: 100%; height: 30px;" placeHolder="Legg til sted..."/>
  </div>
</template>
<script>
/**
 * LocationSearchBar is a component that works as a search bar which auto completes a location name.
 * The component is used for finding location by name.
 * 
 * @author Magnus Bredeli
 */
export default {
    name: "LocationSearchBar",

    methods: {
        /**
         * setLocation is a function which emits the chosen location back to ActivityCard.
         */
        setLocation(location){
            this.$emit("setPlace", location);
        }   
    },
}
</script>