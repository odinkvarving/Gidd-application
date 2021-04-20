<template>
<button v-on:click="goToProfile">To profile</button>
</template>

<script>
export default {
  name: "Home",
  methods:{
    goToProfile(){
      this.$router.push('/account/1')
    }
  }
}
</script>

<style scoped>

</style>