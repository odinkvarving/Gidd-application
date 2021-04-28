import { userService } from "./UserService.js"

export const notificationService = {
    getAccountsNotifications,
    getAccountsNotificationSettings,
    updateAccountsNotificationSettings,
    sendNotification,
    updateNotification,
    sendNotificationToAllParticipants
}


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

/** NotificationSettings object structure
{
    wantsActivityChangedNotifications:false,
    wantsActivityChangedMails: true,
    wantsOutOfQueueNotifications: false,
    wantsOutOfQueueMails: true,
    wantsActivityCancelledNotifications: false,
    wantsActivityCancelledMails: true
}
 */
function updateAccountsNotificationSettings(accountId, notificationSettings){
    let url = `http://localhost:8080/accounts/${accountId}/accountInfo/notificationSettings`;

    const requestOptions = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': userService.getTokenString()
        },
        body: JSON.stringify(notificationSettings)
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error));
}


/** Format of notification object:
 * {
    account:{
        id: 1
    },
    message: "HEllo boyyy",
    date: "2000-01-01 16:00",
    isSeen: false,
    activityId: 108
    }
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