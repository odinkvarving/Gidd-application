<template>
    <div id="card" v-if="isDataReady">
        <h1 style="margin-top: 15px; opacity: 75%; height: 60px;">{{ activity.title }}</h1>
        <div id="ownerInfo">
            
            
            <img alt="Activity host profile picture" :src="accountInfo.imageURL">

            <!-- Add profile pic! -->
            <div class="owner-time">
                <h3>{{ activity.creator.email }}</h3>
                <p>{{ activity.startTime }}</p>
            </div>
        </div>
        <div class="mini-details">
            <div class="detail-container">
                <p class="detail-header">Sted</p> 
                <p class="detail-value" style="font-size:18px;"> {{ activity.location }} </p>
            </div>
            <div class="vertical-line"/>
            <div class="detail-container">
                <p class="detail-header">Nivå</p> 
                <p class="detail-value"> {{ activity.level.description }}</p>
            </div>
            <div class="vertical-line"/>
            <div class="detail-container">
                <p class="detail-header">Kategori</p> 
                <p class="detail-value" style="font-size:18px;">{{ activity.activityType.type }}</p>
            </div>
        </div>
        <div id="map-preview">
            <div v-if="activity.latitude == 0 && activity.longitude == 0" style="width: 245px; height: 161px; background-color: #f6f6f6">
                <p>Location not found!</p>
            </div>
            <GmapMap 
                :center="{lat:activity.latitude, lng:activity.longitude}"
                :zoom="11"
                :options='{fullscreenControl: false, gestureHandling: "none"}'
                map-type-id="roadmap"
                style="width: 245px; height: 161px"
                v-else
                >

                <GmapMarker 
                    :position="{lat:activity.latitude, lng:activity.longitude}"
                    @click="center={lat:activity.latitude, lng:activity.longitude}"
                />
            </GmapMap>
        </div>
        <p style="margin: 0">Deltakere: {{ currentParticipants }} / {{ activity.maxParticipants }}</p>
        <p style="font-size: 13px; opacity: 70%; margin: 0; position: absolute; bottom: 90px;" v-if="participantsInQueue > 0">+ {{ participantsInQueue }} på venteliste</p>
        <!--<div>
            <img alt="Participant profile picture" v-for="image in images" :key="image.url" :src="image.url">
        </div>-->
        <div v-show="!activity.cancelled">
            <button v-if="!isFull && !alreadyParticipating" id="btn" class="join" @click.stop="joinButtonClicked()">
                <div v-if="showJoinSpinner" class="spinner-border" role="status" style="margin-top: 4px">
                    <span class="sr-only">Loading...</span>
                </div>
                <span v-else >{{ getButtonStatus() }}</span>
            </button>
            <button v-else-if="isFull && !alreadyParticipating" id="btn" class="full" @click.stop="joinButtonClicked()"><span>{{ getButtonStatus() }}</span></button>
            <button v-else id="btn" :class="{ 'inQueue': isInQueue, 'participating': !isInQueue }" @click.stop="removeParticipantClicked()">
                <div v-if="showRemoveSpinner" class="spinner-border" role="status" style="margin-top: 4px">
                    <span class="sr-only">Loading...</span>
                </div>
                <span id="test-id" v-else>{{ isInQueue ? "På venteliste" : "Påmeldt" }}</span>
            </button>
        </div>
        <button
            disabled
            v-show="activity.cancelled"
            class="cancel-button"
            >Aktivitet avlyst!</button
        >
    </div>
</template>
<script>
/**
 * Activity is a component which represent a single activity in the activity feed.
 * 
 * @author Scott Rydberg Sonen
 * @author Magnus Bredeli
 */

/**
 * userService and activityButtonService is imported for efficient use of common and relevant functions.
 */
import { userService } from '../../services/UserService';
import { activityButtonService } from '../../services/ActivityButtonService';
    export default {
        name: "Activity",

        props: {
            /**
             * activity is a prop which is passed from ActivityFeed containing an activity
             */
            activity: {
                type: Object,
                required: true
            },
            /**
             * isLoggedIn is a prop which is passed from ActivityFeed which tells us if the account is logged in or not.
             */
            isLoggedIn: Boolean
        },

        data() {
            return {
                /**
                 * isFull tells us if the activity is full or not.
                 */
                isFull: false,
                /**
                 * alreadyParticipating represents the state of an account's participation in an activity.
                 */
                alreadyParticipating: false,
                /**
                 * currentParticipants contains number of participants in the activity.
                 */
                currentParticipants: 0,
                /**
                 * showJoinSpinner represents the visibility of join spinner.
                 */
                showJoinSpinner: false,
                /**
                 * showRemoveSpinner represents the visibility of remove spinner.
                 */
                showRemoveSpinner: false,
                /**
                 * participantsInQueue represents number of participants in an activity's queue.
                 */
                participantsInQueue: 0,
                /**
                 * queuePosition represents the queue position of the account.
                 */
                queuePosition: 0,
                /**
                 * isInQueue represents the state of an account's existence in the queue.
                 */
                isInQueue: false,
                /**
                 * isDataReady is a flag which tells us when fetched data is ready for utilization.
                 */
                isDataReady: false,
                accountInfo: {}
            }
        },

        /**
         * mounted is a function which runs when the component renders.
         * mounted runs functions and defines values which are important for the app's functionality.
         */
        async mounted(){
            await this.getCurrentParticipantsNumber();
            this.accountInfo = await userService.getAccountInfo(this.activity.creator.id);
            if(this.accountInfo.imageURL === null || !this.accountInfo.imageURL.includes("http://localhost:8080/profilepictures/")){
                this.accountInfo.imageURL = "http://localhost:8080/profilepictures/";
            }
            if(this.isLoggedIn){
                await this.isAlreadyParticipating();
                if ((this.currentParticipants === this.activity.maxParticipants) && (this.queuePosition > 0)) {
                    this.isInQueue = true;
                }
                console.log(this.accountInfo);
            }
            this.isDataReady = true;
        },
        
        methods: {
            /**
             * joinButtonClicked is a function which runs on join button click
             *  and adds account as a participant of the activity.
             */
            joinButtonClicked(){
                this.showJoinSpinner = true;
                if (activityButtonService.joinButtonClicked()) {
                    this.addParticipantToActivity(this.activity);
                }
            },
            
            /**
             * removeParticipantClicked is a function which runs removeParticipantFromActivity function in activityButtonService
             *  if 'OK' is clicked in a confirmation box.
             * The function also changes number of current participants and queue participants for display purpose.
             */
            async removeParticipantClicked(){
                if (activityButtonService.showRemoveAlert()) {
                    this.showRemoveSpinner = true;
                    const data = await activityButtonService.removeParticipantFromActivity(this.activity);
                    if (data) {
                        this.showRemoveSpinner = false;
                        if ((this.currentParticipants === this.activity.maxParticipants) && (this.queuePosition > 0)) {
                            this.participantsInQueue --;
                        } else {
                            this.currentParticipants --;
                        }
                        this.alreadyParticipating = false;
                        this.isInQueue = false;
                        this.$emit('refresh-list', this.activity.id, false);
                    }
                }
            },
            
            /**
             * getButtonStatus is a function which runs getButtonStatus function in activityButtonService
             *  and receives button status.
             */
            getButtonStatus() {
                let status = activityButtonService.getButtonStatus(this.alreadyParticipating, this.currentParticipants, this.activity);
                if (status === "Fullt") {
                    this.isFull = true;
                }
                return status;
            },
            
            /**
             * isAlreadyParticipating is a function which runs isAlreadyParticipating function in activityButtonService.
             * If account is present in activity_account table in the database, 
             *  the function also checks the queue position of account.
             * Queue position remains 0 if user is not present in the queue.
             */
            async isAlreadyParticipating() {
                this.alreadyParticipating = await activityButtonService.isAlreadyParticipating(this.activity);
                if (this.alreadyParticipating) {
                    this.getQueuePosition();
                }
            },

            /**
             * getCurrentParticipantsNumber runs getCurrentParticipantsNumber function in activityButtonService.
             * If current participants equals max limit, 
             *  the function also checks how many accounts the queue contains.
             * Lastly, the function emits currentparticipants back to ActivityFeed,
             *  along with activity ID as a key.
             */
            async getCurrentParticipantsNumber(){
                this.currentParticipants = await activityButtonService.getCurrentParticipantsNumber(this.activity);
                if(this.currentParticipants === this.activity.maxParticipants){
                    this.participantsInQueue = await activityButtonService.countAccountsInQueue(this.activity);
                }
                this.$emit('currentParticipantsFound', this.activity.id, this.currentParticipants);
            },
            
            /**
             * addParticipantToActivity runs addParticipantToActivity function in activityButtonService.
             * If the called function returns correct values,
             *  the connection was correctly added to the database.
             * Different variables are modified when the function confirms that the account is successfully participating.
             */
            async addParticipantToActivity(){
                let data = await activityButtonService.addParticipantToActivity(this.activity);
                let accountId = await userService.getAccountByEmail().then(data => accountId = data.id);
                    if(data.activityId === this.activity.id && data.accountId === accountId){
                        if(this.currentParticipants === this.activity.maxParticipants){
                            this.participantsInQueue ++;
                            this.isInQueue = true;
                        }else{
                            this.currentParticipants ++;
                        }
                        this.alreadyParticipating = true;
                        this.showJoinSpinner = false;
                        this.$emit('refresh-list', this.activity.id, true);
                    }
            },
            
            /**
             * countAccountsInQueue runs countAccountsInQueue function in activityButtonService.
             * Return value equals the number of participants in the queue.
             */
            async countAccountsInQueue(){
                this.participantsInQueue = await activityButtonService.countAccountsInQueue(this.activity);
            },

            /**
             * getQueuePosition runs getQueuePosition function in activityButtonService.
             * Return value equals queue position of account.
             * If the queue position is larger than 0, it means that the account is in the queue.
             */
            async getQueuePosition(){
                this.queuePosition = await activityButtonService.getQueuePosition(this.activity);

                if(this.queuePosition > 0){
                    this.isInQueue = true;
                }
            }
        }
    }
</script>
<style scoped>
    #card{
        position: relative;
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

    #card h1 {
        font-size: 30px;
    }
    
    #card:hover {
        background: #ffffff42;
        transition: 0.3s;
    }

    #card p{
        font-size: 20px;
        padding: 0;
    }
    
    #ownerInfo{
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin: auto;
        width: 100%;
        margin: 20px 0 0 65px;
    }
    
    #ownerInfo img{
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
        font-size: 20px;
        margin: 0;
        opacity: 75%;
        width: 80%;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
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
        height: 70px;
        justify-content: space-around;
        margin-top: 40px;
    }

    .vertical-line {
        width: 1px;
        height: 64px;
        opacity: 45%;
        background-color: black;
    }

    .detail-container {
        display: flex;
        width: 30%;
        flex-flow: column;
        justify-content: space-between;
    }

    .detail-header {
        opacity: 65%;
        margin: 0;
        padding: 0;
    }

    .detail-value {
        margin: 0;
        padding: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        height: 25px;
        line-height: 25px;
    }

    #map-preview {
        margin: 30px 0;
    }

    #btn{
        height: 50px;
        width: 160px;
        border-radius: 6px;
        font-size: 20px;
        cursor: pointer;
        background-color: #FFBD3E;
        color: white;
        border: 0;
        outline: none;
        margin-top: 40px;
    }
    
    #btn:hover {
        background-color: #eca82b;
        transition: 0.2s;
    }
    
    #btn.full{
        background-color: #FF5B3E;
    }
    
    #btn.full:hover{
        background-color: #91301f;
        transition: 0.2s;
    }
    
    #btn.full:hover span{
        display: none;
    }
    
    #btn.full:hover:before{
        content: "Venteliste";
    }

    #btn.participating{
        background-color: #4a934a;
        transition: 0.2s;
    }
    
    #btn.participating:hover{
        background-color: #408140;
        transition: 0.2s;
    }
    
    #btn.participating:hover span{
        display: none;
    }
    
    #btn.participating:hover:before{
        content: "Meld av";
    }
    
    #btn.inQueue{
        background-color: #FF5B3E;
    }
    
    #btn.inQueue:hover{
        background-color: #91301f;
        transition: 0.2s;
    }

    #btn.inQueue:hover span{
        display: none;
    }

    #btn.inQueue:hover:before{
        content: "Meld av";
    }

    .queue-list{
        opacity: 70%;
    }

    .cancel-button {
        height: 50px;
        width: 160px;
        border-radius: 6px;
        font-size: 20px;
        background-color: #e87c86;
        color: white;
        border: 0;
        outline: none;
        margin-top: 40px;
    }

    @media (max-width: 550px) {
        #card {
            width: 330px;
        }

  
    }

</style>
