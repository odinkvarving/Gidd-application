<template>
    <div id="card">
        <h1>{{ activity.name }}</h1>
        <div id="ownerInfo">
            <img :src="require('@/assets/' + activity.ownerImage)">
            <h3>{{ activity.ownerName }}</h3>
        </div>
        <p>{{ activity.time }}</p>
        <p>Kategori: {{ activity.type }}</p>
        <p>Sted: {{ activity.location }}</p>
        <p>Deltakere: {{ activity.currentParticipants }} / {{ activity.totalParticipants }}</p>
        <div>
            <img v-for="image in images" :key="image.url" :src="image.url">
        </div>
        <button id="btn" :class="{ full: isFull }" @click.stop="handleButtonClick()"><span>{{ checkIfFull() }}</span></button>
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
                    return "Bli med";
                } else {
                    this.isFull = true;
                    return "Fullt";
                }
            },

            handleButtonClick() {
                //Open login/register window or add the user to "participants"
                console.log("Button clicked");
            },
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
    #ownerInfo{
        display: grid;
        grid-template-areas: "image name";
        margin: auto;
        width: 50%;
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