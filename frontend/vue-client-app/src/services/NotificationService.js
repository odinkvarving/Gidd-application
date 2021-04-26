import { userService } from "./UserService.js"

export const notificationService = {
    getAccountsNotifications,
    sendNotification,
    updateNotification
}


function getAccountsNotifications(accountId){

    let url = `http://localhost:8080/accounts/${accountId}/notifications`;
    
    const requestOptions = {
        method: 'GET',
        headers: userService.authorizationHeader()
    }

    fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => console.log(data))
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
    

function updateNotification(data){
    let url = `http://localhost:8080/accounts/notifications`;

    const requestOptions = {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': userService.getTokenString()
        },
        body: JSON.stringify({id: data.id, isSeen: true})
    }

    return fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error))
}
