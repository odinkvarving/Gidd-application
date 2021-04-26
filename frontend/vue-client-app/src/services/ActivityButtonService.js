/**
 * ActivityButtonService is a script which contains methods related to join buttons in activities
 * 
 * @author Magnus Bredeli
 * @author Scott Rydberg Sonen
 */

import { userService } from './UserService'
 
export const activityButtonService = {
    joinButtonClicked,
    getButtonStatus,
    checkIfFull,
    isAlreadyParticipating,
    getCurrentParticipantsNumber,
    addParticipantToActivity,
    removeParticipantFromActivity,
    countAccountsInQueue,
    getQueuePosition
}

function joinButtonClicked(){
    if(!userService.isLoggedIn()){
        console.log("Tried to join activity without being logged in.\nRedirecting to login page");
        this.$router.push("/login");
    }else{
        this.addParticipantToActivity();
    }
}

function getButtonStatus(alreadyParticipating, currentParticipants, activity) {
    if (alreadyParticipating) { 
        return "Påmeldt";
    } else { //If we could not find the ID in accountActivity database, we check if the activity is full or not
        return this.checkIfFull(currentParticipants, activity);
    }
}

function checkIfFull(currentParticipants, activity) {
    if (currentParticipants < activity.maxParticipants) {
        return "Bli med";
    } else {
        return "Fullt";
    }
}

async function isAlreadyParticipating(activity) {
    let accountId;
    await userService.getAccountByEmail().then(data => accountId = data.id);

    let url = `http://localhost:8080/activities/${activity.id}/accounts/${accountId}/`;

    const requestOptions ={
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': userService.getTokenString()
        }
    }

    let participating = false;
    
    await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => participating = data)
        .catch(error => console.log(error));

    console.log(`You are already participating: ${participating}`);

    if(participating){
        this.getQueuePosition(accountId);
    }
    return participating;
}

async function getCurrentParticipantsNumber(){
    // Get number of participators on this activity
    let url =  `http://localhost:8080/activities/${this.activity.id}/accounts/count`

    const requestOptions ={
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    }

    await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => this.currentParticipants = data)
        .catch(error => console.log(error));

    console.log(`${this.activity.title} got current participants: ${this.currentParticipants}.`);

    if(this.currentParticipants == this.activity.maxParticipants){
        this.countAccountsInQueue();
    }

}

async function addParticipantToActivity(){

    let accountId;
    await userService.getAccountByEmail().then(data => accountId = data.id);

    let url = `http://localhost:8080/activities/${this.activity.id}/accounts/${accountId}/`;

    const requestOptions ={
        method: 'POST',
        headers: userService.authorizationHeader()
    }

    fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            if(data.activityId === this.activity.id && data.accountId === accountId){
                console.log("Joining activity was successful! Changing button style");
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
        })
        .catch(error => console.log(error));
}

async function removeParticipantFromActivity(activity){
    let accountId;
    await userService.getAccountByEmail().then(data => accountId = data.id);

    let url = `http://localhost:8080/accounts/${accountId}/activities/${activity.id}`;

    const requestOptions ={
        method: 'DELETE',
        headers: userService.authorizationHeader()
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .catch(error => console.log(error))
}

function countAccountsInQueue(){
    
    let url = `http://localhost:8080/activities/${this.activity.id}/accounts/queue/count`;

    const requestOptions = {
        method:'GET',
        headers: userService.authorizationHeader()
    }

    fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => this.participantsInQueue = data);
}

async function getQueuePosition(accountId){
    let url = `http://localhost:8080/accounts/${accountId}/activities/${this.activity.id}`

    const requestOptions = {
        method:'GET',
        headers: userService.authorizationHeader()
    }

    await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => this.queuePosition = data)
        .catch(error => console.log(error));

    if(this.queuePosition > 0){
        this.isInQueue = true;
    }
}