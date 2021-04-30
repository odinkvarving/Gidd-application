/**
 * ActivityButtonService is a script which contains methods related to join buttons in activities
 * 
 * @author Magnus Bredeli
 * @author Scott Rydberg Sonen
 */

/**
 * userService is imported for login check
 */
import { userService } from './UserService'
 
/**
 * These are all the methods which will be available for other components importing this service
 */
export const activityButtonService = {
    joinButtonClicked,
    getButtonStatus,
    checkIfFull,
    isAlreadyParticipating,
    getCurrentParticipantsNumber,
    addParticipantToActivity,
    removeParticipantFromActivity,
    countAccountsInQueue,
    getQueuePosition,
    showRemoveAlert
}

/**
 * joinButtonClicked is a function which checks if the account is logged in
 *  before adding the account as a participant to the activity
 * @returns true if account is logged in. If not, the account will be redirected to the login page
 */
function joinButtonClicked(){
    if(!userService.isLoggedIn()){
        console.log("Tried to join activity without being logged in.\nRedirecting to login page");
        this.$router.push("/login");
    }else{
        return true;
    }
}

/**
 * getButtonStatus is a function which returns button status
 *  depending on if the account is already participating or not.
 * @param {boolean} alreadyParticipating: representing if the account is participating in given activity or not
 * @param {Number} currentParticipants: current participants of the activity
 * @param {Object} activity: the activity itself
 * @returns button status as a string
 */
function getButtonStatus(alreadyParticipating, currentParticipants, activity) {
    if (alreadyParticipating) { 
        return "Påmeldt";
    } else { //If we could not find the ID in accountActivity database, we check if the activity is full or not
        return this.checkIfFull(currentParticipants, activity);
    }
}

/**
 * checkIfFull is a function which checks if the activity is full or not
 *  by comparing current participants to a max limit
 * @param {Number} currentParticipants: current participants of the activity
 * @param {Object} activity: the activity itself
 * @returns button status represented as a string
 */
function checkIfFull(currentParticipants, activity) {
    if (currentParticipants < activity.maxParticipants) {
        return "Bli med";
    } else {
        return "Fullt";
    }
}

/**
 * isAlreadyParticipating is a function which checks if the account is already participating in a given activity.
 * This is done by fetching a boolean from backend, where we check if the account token has a registered connection to it.
 * @param {Object} activity: the activity itself
 * @returns state of participation represented as a boolean
 */
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
        this.getQueuePosition(activity);
    }
    return participating;
}

/**
 * getCurrentParticipantsNumber is a function which gets number of current participants using fetching from backend function.
 * @param {Object} activity: the activity itself
 * @returns number of participants
 */
async function getCurrentParticipantsNumber(activity){
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

/**
 * addParticipantToActivity is a function which adds a participant by post request to backend.
 * @param {Object} activity: the activity itself
 * @returns boolean representing if the activity was added or not
 */
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

/**
 * removeParticipantFromActivity is a function which removes current participant from activity.
 * Delete request is sent to backend, containing account's ID and the activity's ID.
 * @param {Object} activity: the activity itself
 * @returns boolean representing if the participant was removed from activity or not
 */
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

/**
 * countAccountsInQueue is a function which receives number of accounts which is in the queue of the activity.
 * @param {Object} activity: the activity itself
 * @returns number of accounts in queue
 */
async function countAccountsInQueue(activity){
    let url = `http://localhost:8080/activities/${activity.id}/accounts/queue/count`;

    const requestOptions = {
        method:'GET',
        headers: userService.authorizationHeader()
    }

    return await fetch(url, requestOptions)
        .then(response => response.json())
        .catch(error => console.log(error));
}

/**
 * getQueuePosition is a function which receives queue position of current account in given activity.
 * @param {Object} activity: the activity itself
 * @returns queue position of account in activity
 */
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

/**
 * showRemoveAlert displays a confirmation box,
 *  where the account can click 'OK' if it wants to remove itself from the activity
 * @returns boolean representing account's choice
 */
function showRemoveAlert() {
    if (confirm('Klikk "OK" hvis du vil melde deg av aktiviteten')) {
        return true;
    } else {
        return false;
    }
}