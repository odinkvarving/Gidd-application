<template>
    <div id="container">
        <h1>{{ activity.name }}</h1>
        <img :src="activity.ownerImage">
        <h3>{{ activity.ownerName }}</h3>
        <p>{{ activity.description }}</p>
        <h3>Informasjon:</h3>
        <ul>
            <li>Kategori:</li>
            <li>Sted:</li>
            <li>Tid:</li>
            <li>Varighet:</li>
            <li>Værmelding:</li>
            <li>Deltakere:</li>
        </ul>
        <ul>
            <li>{{ activity.type }}</li>
            <li>{{ activity.location }}</li>
            <li>{{ activity.time }}</li>
            <li>{{ activity.duration }}</li>
            <li>{{ activity.weather }}</li>
            <li>{{ activity.currentParticipants }}/{{ activity.totalParticipants }}</li>
        </ul>
        <button id="btn" @click="handleButtonClick()">{{ checkIfFull() }}</button>
    </div>
</template>
<script>
    export default {
        name: "Info",

        props: {
            activity: {
                type: Object,
                required: true
            }
        },

        data() {
            return {
                status: "",
            }
        },

        methods: {
            checkIfFull() {
                if (this.activity.currentParticipants < this.activity.totalParticipants) { 
                    //document.getElementById("btn").classList.add("notFull"); 
                    return "Bli med";
                } else {
                    //document.getElementById("btn").classList.add("full"); 
                    return "Fullt";
                }
            },

            load() {
                
            },

            handleButtonClick() {
                //Open login/register window or add the user to "participants"
            },
        }
    }
</script>
<style scoped>
    #container{
        width: 30vw;
        height: 50vh;
        background-color: grey;
    }
</style>