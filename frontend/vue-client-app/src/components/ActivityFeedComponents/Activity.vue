<template>
    <div id="card">
        <h1 style="margin-top: 15px; opacity: 75%;">{{ activity.name }}</h1>
        <div id="ownerInfo">
            <img alt="Activity host profile picture" :src="require('@/assets/' + activity.ownerImage) ">
            <div class="owner-time">
                <h3>{{ activity.ownerName }}</h3>
                <p>{{ activity.time }}</p>
            </div>
        </div>
        <div class="mini-details">
            <div class="detail-container">
                <p class="detail-header">Sted</p> 
                <p class="detail-value" style="font-size:18px;">{{ activity.location }}</p>
            </div>
            <div class="vertical-line"/>
            <div class="detail-container">
                <p class="detail-header">Varighet</p> 
                <p class="detail-value" style="font-size:18px;">{{ activity.duration }}</p>
            </div>
            <div class="vertical-line"/>
            <div class="detail-container">
                <p class="detail-header">Kategori</p> 
                <p class="detail-value" style="font-size:18px;">{{ activity.type }}</p>
            </div>
        </div>
        <div id="map-preview">
            <img src="../../assets/map-preview-example.png" alt="Activity location map preview"/>
        </div>
        <p>Deltakere: {{ activity.currentParticipants }} / {{ activity.totalParticipants }}</p>
        <!--<div>
            <img alt="Participant profile picture" v-for="image in images" :key="image.url" :src="image.url">
        </div>-->
        <button id="btn" :class="{ full: isFull }" @click.stop="handleButtonClick()"><span>{{ getButtonStatus() }}</span></button>
    </div>
</template>
<script>
import { userService } from '../../services/UserService';
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
            handleButtonClick() {
                //Open login/register window or add the user to "participants"
                console.log("Button clicked");

                if(!userService.isLoggedIn()){
                    console.log("Tried to join activity without being logged in.\nRedirecting to login page");
                    this.$router.push("/login");
                }

                
            },

            getButtonStatus() {
                let accountId = 0; //Here we will find the account Id for the account which is logged on
                if (this.ifParticipating(accountId)) { 
                    return "Påmeldt";
                } else { //If we could not find the ID in accountActivity database, we check if the activity is full or not
                    return this.checkIfFull();
                }
            },

            checkIfFull() {
                if (this.activity.currentParticipants < this.activity.totalParticipants) {
                    return "Bli med";
                } else {
                    this.isFull = true;
                    return "Fullt";
                }
            },

            ifParticipating(accountId) {
                if (accountId === 0) {
                    return false;
                }
                return true;
            }
        }
    }
</script>
<style scoped>
    #card{
        height: 625px;
        width: 464px;
        background: #FFFFFF;
        box-shadow: 0px 4px 4px 0px #0000001A;
        cursor: pointer;
        text-align: center;
        font-size: 20px;
        margin: 0 10px 20px 10px;
        display: flex;
        flex-flow: column;
        align-items: center;
    }
    
    #card:hover {
        background: #ffffff00;
        transition: 0.3s;
    }

    #card p{
        font-size: 20px;
    }
    #ownerInfo{
        display: flex;
        align-items: center;
        margin: auto;
        width: 100%;
        margin: 20px 0 0 65px;
    }
    #ownerInfo img{
        grid-area: image;
        width: 50px;
        height: 50px;
        border-radius: 35px;
        box-shadow: 0px 4px 4px 0px #0000001A;
    }

    .owner-time {
        display: flex;
        flex-flow: column;
        align-items: flex-start;
        width: 80%;
        margin-left: 30px;
    }

    #ownerInfo h3{
        grid-area: name;
        font-size: 25px;
        margin: 0;
        opacity: 75%;
    }

    #ownerInfo p{
        margin: 0 0 0 3px;
        font-size: 15px;
        opacity: 50%;
    }

    .time{
        margin: 0;
    }

    .mini-details {
        display: flex;
        flex-flow: row;
        width: 90%;
        height: 75px;
        justify-content: space-around;
        margin-top: 40px;
    }

    .vertical-line {
        width: 1px;
        height: 71px;
        opacity: 45%;
        background-color: black;
    }

    .detail-container {
        display: flex;
        flex-flow: column;
        justify-content: space-between;
    }

    .detail-header {
        opacity: 65%;
        margin: 0;
    }

    .detail-value {
        margin: 0;
    }

    #map-preview {
        margin: 30px 0;
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
        outline: none;
    }
    #btn:hover {
        background-color: #eca82b;
        transition: 0.2s;
    }
    #btn.full{
        background-color: #FF5B3E;
    }
    #btn.full:hover{
        background-color: #dd4b31;
        transition: 0.2s;
    }
    #btn.full:hover span{
        display: none;
    }
    #btn.full:hover:before{
        content: "Venteliste";
    }
</style>
