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
<style>
    #list{
        display: flex;
        flex-wrap: wrap;
        flex-direction: column;
        border: 1px solid black;
        height: 100%;
    }
</style>
