<template>
    <div id="container">
        <h3>Utstyr:</h3>
        <p>{{ activity.equipment }}</p>
    </div>
</template>
<script>
    export default {
        name: "Equipments",

        props: {
            activity: {
                type: Object,
                required: true
            }
        }
    }
</script>
<style scoped>
    #container{
        float: left;
    }
</style>