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

function joinButtonClicked(activity, isLoggedIn){
    if(!isLoggedIn){
        console.log("Tried to join activity without being logged in.\nRedirecting to login page");
        this.$router.push("/login");
    }else{
        return this.addParticipantToActivity(activity);
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

async function getCurrentParticipantsNumber(activity){
    // Get number of participators on this activity
    let currentParticipants = 0;

    let url =  `http://localhost:8080/activities/${activity.id}/accounts/count`

    const requestOptions ={
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    }

    await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => currentParticipants = data)
        .catch(error => console.log(error));

    console.log(`${activity.title} got current participants: ${currentParticipants}.`);
    return currentParticipants;
}

async function addParticipantToActivity(activity){
    let accountId;
    await userService.getAccountByEmail().then(data => accountId = data.id);

    let url = `http://localhost:8080/activities/${activity.id}/accounts/${accountId}/`;

    const requestOptions ={
        method: 'POST',
        headers: userService.authorizationHeader()
    }

    return await fetch(url, requestOptions)
        .then(response => response.json())
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

    return await fetch(url, requestOptions)
        .then(response => response.json())
        .catch(error => console.log(error))
}

function countAccountsInQueue(activity){
    let url = `http://localhost:8080/activities/${activity.id}/accounts/queue/count`;

    const requestOptions = {
        method:'GET',
        headers: userService.authorizationHeader()
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .catch(error => console.log(error));
}

async function getQueuePosition(activity){
    let accountId;
    await userService.getAccountByEmail().then(data => accountId = data.id);

    let url = `http://localhost:8080/accounts/${accountId}/activities/${activity.id}`

    const requestOptions = {
        method:'GET',
        headers: userService.authorizationHeader()
    }

    return await fetch(url, requestOptions)
        .then(response => response.json())
        .catch(error => console.log(error));
}