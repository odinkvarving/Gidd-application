<template>
    <div id="activity" v-if="activity != null">
        <div v-show="isActivityHost" class="cancel-button-container">
            <b-button class="cancel-button" variant="danger">Avlys aktivitet</b-button>
        </div>
        <div class="upper-row">
            <Info class="comp" id="info" :activity="activity" :weather="weather" :isActivityHost="isActivityHost"/>
            <div class="map-equipment-container">
                <Map class="comp" id="map" :latitude="activity.latitude" :longitude="activity.longitude"/>
                <Equipment class="comp" id="equipment" :activity="activity" :isActivityHost="isActivityHost"/>
            </div>
        </div>
        <button id="btnVisible" @click="changeChatVisibility">Skjul chat</button>
        <Chat class="chat" id="chat" :activity="activity" v-show="isChatVisible"/>
    </div>
</template>
<script>
    import Info from './Info.vue'
    import Map from './Map.vue'
    import Equipment from './Equipment.vue'
    import Chat from './Chat.vue'
    import {userService} from '../../services/UserService.js'
    //import {weatherService} from '../../services/WeatherService.js'

    /**
     * ActivityCard is a component which represents a clicked activity.
     * The component is found in router/Activity.
     * The component itself contains four other components: Info, Map, Equipment and Chat.
     * Each of these components represents their own part of an activity.
     */
    export default {
        name: "ActivityCard",
        components: {
            Info,
            Map,
            Equipment,
            Chat
        },

        props: {
            /**
             * activity is a prop passed from router/Activity to ActivityCard.
             * The activity represents the clicked activity in activity feed.
             */
            activity: {
                type: Object,
                required: true
            },
            weather: {
                type: Object,
                required: true
            },
        },

        data() {
            return {
                /**
                 * isChatVisible is a boolean which changes each time the button (btnVisible) is clicked.
                 * When isChatVisible changes to false, the chat box will be not be displayed.
                 */
                isChatVisible: true,
                /**
                 * isLoggedIn is a boolean to check if the client is logged in or not
                 */
                isLoggedIn: false,
                isActivityHost: false
            }
        },
        async mounted(){
            this.isLoggedIn = await userService.isLoggedIn();
            this.isActivityHost = await this.checkIfActivityHost();
            console.log(this.isActivityHost);
        },
        methods: {
            /**
             * changeChatVisibility is a function which changes the state of chat visibility.
             * When btnVisible is clicked, it will change value, depending on if the chat is visible or not.
             */
            changeChatVisibility() {
                this.isChatVisible = !this.isChatVisible;
                const btn = document.getElementById('btnVisible');
                if (this.isChatVisible) {
                    btn.childNodes[0].nodeValue = "Skjul chat";
                } else {
                    btn.childNodes[0].nodeValue = "Åpne chat";
                }
            },
            async checkIfActivityHost(){
                if(this.isLoggedIn){
                    let accountId = await userService.getAccountByEmail().then(data => {
                    return data.id;
                    });
                    
                    return this.activity.creator.id === accountId;
                }else{
                    return false;
                }
            }
        }
    }
</script>
<style>

    .cancel-button-container {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
    }

    .cancel-button{
        width: 200px;
        height: 40px;
        margin: 20px 60px 20px 20px;
    }

    #activity{
        display: flex;
        /*display: flex;
        flex-wrap: wrap;
        flex-direction: row;
        background-color: #F6F6F6;*/
    }

    .upper-row{
        display: flex;
        flex-direction: row wrap;
        height: 85vh;

    }
    .comp{
        background-color: white;
        box-shadow: 0px 4px 4px 0px #00000040;
    }
    #info{
        width: 40vw;
        text-align: center;
    }

    .map-equipment-container{
        display: flex;
        flex-flow: column;
        justify-content: space-between;
        margin-left: 40px;
    }

    #map{
        width: 40vw;
        height: 40vh;

    }
    #equipment{
        text-align: left;
        width: 40vw;
        height: 40vh;
    }
    #btnVisible{
        border: none;
        background-color: #FFBD3E;
        color: white;
    }
    #chat{
        width: 87vw;
        margin-top: 1%;
    }
</style>