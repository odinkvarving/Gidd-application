<template>
    <div id="card">
        <h1>{{ activity.name }}</h1>
        <img :src="activity.ownerImage">
        <h3>{{ activity.ownerName }}</h3>
        <p>{{ activity.time }}</p>
        <p>Kategori: {{ activity.type }}</p>
        <p>Sted: {{ activity.location }}</p>
        <p>Deltakere: {{ activity.currentParticipants }} / {{ activity.totalParticipants }}</p>
        <div>
            <img v-for="image in images" :key="image.url" :src="image.url">
        </div>
        <button id="btn" :class="{ full: isFull }" @click="handleButtonClick()"><span>{{ checkIfFull() }}</span></button>
    </div>
</template>
<script>
    export default {
        name: "Activity",

        props: {
            activity: {
                type: Object,
                required: true
            }
        },

        data() {
            return {
                isFull: false,
            }
        },

        methods: {
            checkIfFull() {
                if (this.activity.currentParticipants < this.activity.totalParticipants) { 
                    //document.getElementById("btn").classList.add("notFull"); 
                    return "Bli med";
                } else {
                    //document.getElementById("btn").classList.add("full"); 
                    this.isFull = true;
                    return "Fullt";
                }
            },

            handleButtonClick() {
                //Open login/register window or add the user to "participants"
            },

            /*addClass(className) {
                document.getElementById("btn").classList.add(className);
            },*/
        }
    }
</script>
<style scoped>
    #card{
        height: 70vh;
        width: 30vw;
        background: #FFFFFF;
        box-shadow: 0px 4px 4px 0px #0000001A;
        cursor: pointer;
    }
    #card p{
        font-size: 20px;
    }
    #btn{
        height: 5vh;
        width: 8vw;
        border-radius: 6px;
        font-size: 20px;
        cursor: pointer;
        background-color: #FFBD3E;
        color: white;
        border: 0;
    }
    #btn.full{
        background-color: #FF5B3E;
    }
    #btn.full:hover span{
        display: none;
    }
    #btn.full:hover:before{
        content: "Venteliste";
    }
</style>