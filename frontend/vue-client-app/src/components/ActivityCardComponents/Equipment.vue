<template>
    <div id="container">
        <h3>Utstyr:</h3>
        <div id="list">
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
<style>
    #list{
        display: flex;
        flex-wrap: wrap;
        flex-direction: column;
        height: 80%;
    }
</style>
