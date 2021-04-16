<template>
    <div id="container" v-if="activity != null">
        <div class="box" id="top">
            <h1>{{ activity.name }}</h1>
            <img :src="activity.ownerImage">
            <h3 class="txt">{{ activity.ownerName }}</h3>
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
                <li class="txt">{{ activity.weather }}</li>
                <li class="txt">{{ activity.currentParticipants }}/{{ activity.totalParticipants }}</li>
            </ul>
            <button id="btn" @click="handleButtonClick()">{{ checkIfFull() }}</button>
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
                status: "",
            }
        },

        methods: {
            checkIfFull() {
                console.log(this.activity);
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
        display: grid;
        grid-template-areas: 
        "top"
        "bottom";
        height: 50vh;
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
        "title title"
        "img name"
        "desc desc";
    }
    #top h1{
        grid-area: title;
    }
    #top img{
        grid-area: img;
    }
    #top h3{
        grid-area: name
    }
    #top p{
        grid-area: desc;
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
    }
</style>