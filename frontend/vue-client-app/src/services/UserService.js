import VueJwtDecode from "vue-jwt-decode"

export const userService = {
    login,
    getAccountByEmail,
    setAccount,
    logout,
    getAll,
    getAccountDetails,
    isLoggedIn,
    sendImage,
    authorizationHeader,
    getTokenString,
    getCurrentLocation,
    getAccountInfo
}


function login(email, password) {
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
    };

    return fetch("http://localhost:8080/accounts/login", requestOptions)
        .then(response => response.text())
        .then(text => {
            const data = text && JSON.parse(text);
            return data;
        })
        .then(jwtResponse => {
            if (jwtResponse.jwtToken) {
                localStorage.setItem('user', JSON.stringify(jwtResponse))
            }
            return jwtResponse;
        });
}

function sendImage(image,id){
    console.log(image.get('file'))
    let user = JSON.parse(localStorage.getItem('user'));
        return fetch(`http://localhost:8080/accounts/${id}/profilepicture`,{
            method:'POST',
            headers:{
                'Accept':image.get('file').type,
                'Authorization': `Bearer ${user.jwtToken}`
            },
            body:image
        }).then(handleResponse)

}

function getAccountByEmail(){
    try{
        let account = getAccountDetails();
        const requestOptions = {
            method: 'GET',
            headers: authorizationHeader()
        }
        return fetch(`http://localhost:8080/accounts/${account.sub}`, requestOptions).then(handleResponse);
    } catch (error) {
        console.log("User is not logged in, redirecting to login page...");
        return null;
    }
}

function logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('user');
}

function getAll() {
    const requestOptions = {
        method: 'GET',
        headers: authorizationHeader()
    };

    return fetch("http://localhost:8080/accounts", requestOptions).then(handleResponse);
}


function handleResponse(response) {
    return response.text().then(text => {
        console.log(text)
        const data = text && JSON.parse(text);
        if (!response.ok) {
            if (response.status === 403) {
                // Logout if error code 403 is returned from api
                logout();
                location.reload(true);
            }
            const error = (data && data.message) || response.statusText;
            return Promise.reject(error);
        }

        return data;
    });
}

// returns the authorization headeren with JWT token that is stored in local storage
function authorizationHeader() {

    let user = JSON.parse(localStorage.getItem('user'));

    if (user && user.jwtToken) {
        return { 'Authorization': `Bearer ${user.jwtToken}` };
    } else {
        return {};
    }
}

function getTokenString() {
    let user = JSON.parse(localStorage.getItem('user'));

    if (user && user.jwtToken) {
        return `Bearer ${user.jwtToken}`;
    } else {
        return null;
    }
}


function getAccountDetails() {

    // get token from localstorage
    let token = JSON.parse(localStorage.getItem("user"));
    try {
        // decode token and attach to account object
        let decoded = VueJwtDecode.decode(token.jwtToken);
        let accountDetails = decoded;
        return accountDetails;
    } catch (error) {
        console.log(error, "error from decoding token");
        return null;
    }
}
function setAccount(newAccount,id){
    let user = JSON.parse(localStorage.getItem("user"));
    if(!user || !user.jwtToken){
        return false;
    }
    return fetch(`http://localhost:8080/accounts/${id}/accountInfo`,{
        method:'PUT',
        headers:{
            'Content-Type':'application/json',
            'Authorization': `Bearer ${user.jwtToken}`
        }
        ,
        body: JSON.stringify(newAccount)
    }).then(handleResponse)
}
function isLoggedIn(){
    // get token from localstorage
    let user = JSON.parse(localStorage.getItem("user"));
    if (!user || !user.jwtToken) {
        return false;
    }

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${user.jwtToken}`
        },
        body: JSON.stringify({ jwtToken: user.jwtToken })
    }

    return fetch("http://localhost:8080/accounts/validateToken", requestOptions)
        .then(handleResponse);
}

function getCurrentLocation(){
    let currentLocation;

    navigator.geolocation.getCurrentPosition(position => {
        console.log(position.coords);
        currentLocation = {
            lat: position.coords.latitude,
            lng: position.coords.longitude,
        }
        return currentLocation;
    })
}

async function getAccountInfo(accountId){
    let url = `http://localhost:8080/accounts/${accountId}/info`;

    const requestOptions = {
        method: 'GET',
        headers: authorizationHeader()
    }

    return await fetch(url, requestOptions)
        .then(response => response.json())
        .then(data => {
            return data;
        })
        .catch(error => console.log(error));
}