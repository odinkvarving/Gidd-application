<template>
    <div id="container">
        <div id="list">
            <h3>Utstyr:</h3>
            <ul v-for="e in activity.equipment" :key="e.equipmentType">
                <li>{{ e.equipmentType }}</li>
            </ul>
        </div>
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
        margin-left: 100px;
    }
</style>