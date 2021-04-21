<template>
    <div id="infobox" v-if="activity != null">
        <div class="box" id="top">
            <h1>{{ activity.name }}</h1>
            <div id="ownerInfo">
                <img :src="require('@/assets/' + activity.ownerImage)">
                <h3 class="txt">{{ activity.ownerName }}</h3>
            </div>
            <p class="txt">{{ activity.description }}</p>
        </div>
        <div class="box" id="bottom">
            <h3>Informasjon:</h3>
            <ul class="list" id="list1">
                <li class="txt">Kategori:</li>
                <li class="txt">Sted:</li>
                <li class="txt">Tid:</li>
                <li class="txt">Varighet:</li>
                <li class="txt">Værmelding:</li>
                <li class="txt">Deltakere:</li>
            </ul>
            <ul class="list" id="list2">
                <li class="txt">{{ activity.type }}</li>
                <li class="txt">{{ activity.location }}</li>
                <li class="txt">{{ activity.time }}</li>
                <li class="txt">{{ activity.duration }}</li>
                <li class="txt" v-if="activity.weather != null">{{ activity.weather.temp }}</li>
                <li class="txt" v-else>Ingen værmelding</li>
                <li class="txt">{{ activity.currentParticipants }} / {{ activity.totalParticipants }}</li>
            </ul>
            <button id="btn" :class="{ full: isFull }" @click="handleButtonClick()"><span>{{ checkIfFull() }}</span></button>
        </div>
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
                isFull: false,
            }
        },

        methods: {
            checkIfFull() {
                console.log(this.activity);
                if (this.activity.currentParticipants < this.activity.totalParticipants) { 
                    return "Bli med";
                } else {
                    this.isFull = true;
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
<style>
    #infobox{
        display: grid;
        grid-template-areas: 
        "top"
        "bottom";
        height: 50vh;
        text-align: center;
    }
    /*.box{
        margin: 2vh 2vw 2vh 2vw;
    }*/
    .txt{
        font-size: 20px;
    }
    #top{
        grid-area: top;
        display: grid;
        grid-template-areas: 
        "title"
        "ownerInfo"
        "desc";
    }
    #top h1{
        grid-area: title;
    }
    #ownerInfo{
        grid-area: ownerInfo;
        display: grid;
        grid-template-areas: "image name";
        margin: auto;
        width: 60%;
    }
    #ownerInfo img{
        grid-area: image;
        width: 70px;
        height: 70px;
        border-radius: 35px;
        box-shadow: 0px 4px 4px 0px #0000001A;
    }
    #ownerInfo h3{
        grid-area: name;
    }
    #top p{
        grid-area: desc;
        height: 100px;
    }
    .list{
        list-style-type: none;
    }
    #bottom{
        grid-area: bottom;
        display: grid;
        grid-template-areas: 
        "infoTitle infoTitle"
        "list1 list2"
        "btn btn";
    }
    #bottom h3{
        grid-area: infoTitle;
    }
    #list1{
        grid-area: list1;
        text-align: left;
        margin-left: 0;
    }
    #list2{
        grid-area: list2;
        text-align: right;
        margin-right: 40px;
    }
    #btn{
        grid-area: btn;
        height: 5vh;
        width: 8vw;
        border-radius: 6px;
        font-size: 20px;
        cursor: pointer;
        background-color: #FFBD3E;
        color: white;
        border: 0;
        margin: auto;
        outline: none;
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
