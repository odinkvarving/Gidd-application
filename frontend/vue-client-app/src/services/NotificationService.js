import { userService } from "./UserService.js"

export const notificationService = {
    getAccountsNotifications,
    getAccountsNotificationSettings,
    updateAccountsNotificationSettings,
    sendNotification,
    updateNotification,
    sendNotificationToAllParticipants,
    removeNotification
}

/**
 * Fetches all notifications to a user.
 * @param {Number} accountId 
 * @returns notifications
 */
function getAccountsNotifications(accountId){

    let url = `http://localhost:8080/accounts/${accountId}/notifications`;
    
    const requestOptions = {
        method: 'GET',
        headers: userService.authorizationHeader()
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error));
}

/**
 * Fetches notification settings to a user.
 * @param {Number} accountId 
 * @returns notification settings
 */
function getAccountsNotificationSettings(accountId){
    let url = `http://localhost:8080/accounts/${accountId}/accountInfo/notificationSettings`;

    const requestOptions = {
        method: 'GET',
        headers: userService.authorizationHeader()
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error));
}

/**
 * 
 * @param {Number} accountId 
 * @param {NotificationSetting} notificationSettings 
 * @returns notification settings
 */
async function updateAccountsNotificationSettings(accountId, notificationSettings){
    let url = `http://localhost:8080/accounts/${accountId}/accountInfo/notificationSettings`;
    console.log(notificationSettings);
    const requestOptions = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': userService.getTokenString()
        },
        body: JSON.stringify(notificationSettings)
    }

    return await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error));
}

/**
 * Sends notification to user.
 * @param {boolean} notification 
 * @returns boolean
 */
function sendNotification(notification){
    let url = `http://localhost:8080/accounts/notifications`

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': userService.getTokenString()
        },
        body: JSON.stringify(notification)
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error))
}
    
/**
 * Updates notifications.
 * @param {boolean} notification 
 * @returns boolean
 */
function updateNotification(notification){
    let url = `http://localhost:8080/accounts/notifications`;

    const requestOptions = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': userService.getTokenString()
        },
        body: JSON.stringify({id: notification.id, isSeen: true})
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error))
}

/**
 * Sends notification on all participants on given activity.
 * @param {Number} activityId 
 * @returns Notification
 */
async function sendNotificationToAllParticipants(activityId){
    let url = `http://localhost:8080/activities/${activityId}/notify-edit`;

    const requestOptions = {
        method: 'POST'
    }

    return await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error));
}

/**
 * Removes notification with given ID.
 * @param {Number} notificationId 
 * @returns Notification
 */
async function removeNotification(notificationId){
    console.log(notificationId);
    let url = `http://localhost:8080/accounts/notifications/${notificationId}`;

    const requestOptions = {
        method: 'DELETE'
    }

    return await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error));
}