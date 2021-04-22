<template>
    <div id="activity" v-if="activity != null">
        <Info class="comp" id="info" :activity="activity"/> <!-- info box> -->
        <Map class="comp" id="map" :activity="activity"/> <!-- component for map location -->
        <Equipment class="comp" id="equipment" :activity="activity"/> <!-- list of equipment -->
        <button class="comp" id="btnVisible" @click="changeChatVisibility">Åpne chat</button> <!-- button for opening and closing the chat box -->
        <Chat class="comp chat" id="chat" :activity="activity" v-show="isChatVisible"/> <!-- chat box (visible if isChatVisible is true) -->
    </div>
</template>
<script>
    import Info from './Info.vue'
    import Map from './Map.vue'
    import Equipment from './Equipment.vue'
    import Chat from './Chat.vue'

    /**
     * ActivityCard is a component which represents a clicked activity.
     * The component is found in router/Activity.
     * The component itself contains four other components: Info, Map, Equipment and Chat.
     * Each of these components represents their own part of an activity.
     * 
     * @author Scott Rydberg Sonen
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
        },

        data() {
            return {
                /**
                 * isChatVisible is a boolean which changes each time the button (btnVisible) is clicked.
                 * When isChatVisible changes to true, the chat box will be displayed.
                 */
                isChatVisible: false,
		isLoggedIn: userService.isLoggedIn()
            }
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
            }
        }
    }
</script>
<style>
    #activity{
        display: grid;
        grid-template-areas: 
        "info map"
        "info equipment"
        "btn equipment"
        "chat chat";
        /*display: flex;
        flex-wrap: wrap;
        flex-direction: row;
        background-color: #F6F6F6;*/
    }
    .comp{
        background-color: white;
        width: 40vw;
        box-shadow: 0px 4px 4px 0px #00000040;
    }
    #info{
        grid-area: info;
        height: 90%;
        text-align: center;
    }
    #map{
        grid-area: map;
        height: 380px;
    }
    #equipment{
        grid-area: equipment;
        text-align: left;
        padding-left: 2%;
        height: 80%;
        margin-top: 2%;
    }
    #btnVisible{
        grid-area: btn;
        border: none;
        background-color: #FFBD3E;
        color: white;
    }
    #chat{
        grid-area: chat;
        width: 87vw;
        margin-top: 1%;
    }
</style>