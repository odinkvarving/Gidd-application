import VueJwtDecode from "vue-jwt-decode"

export const userService = {
    login,
    getAccountByEmail,
    logout,
    getAll,
    getAccountDetails,
    isLoggedIn,
    authorizationHeader,
    getTokenString
}


function login(email, password){
    const requestOptions = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json'},
        body: JSON.stringify({email, password}) 
    };

    return fetch("http://localhost:8080/accounts/login", requestOptions)
        .then(response => response.text())
        .then(text => {
            const data = text && JSON.parse(text);
            return data;
        })
        .then(jwtResponse => {
            if(jwtResponse.jwtToken){
                localStorage.setItem('user', JSON.stringify(jwtResponse))
            }
            return jwtResponse;
        });
}

function getAccountByEmail(){

    try{
        let account = getAccountDetails();
        const requestOptions ={
            method: 'GET',
            headers: authorizationHeader()
        }
        return fetch(`http://localhost:8080/accounts/${account.sub}`, requestOptions).then(handleResponse);
    }catch(error){
        console.log("User is not logged in, redirecting to login page...");
        return null;
    }   

}

function logout(){
    // remove user from local storage to log user out
    localStorage.removeItem('user');
}

function getAll(){
    const requestOptions = {
        method: 'GET',
        headers: authorizationHeader()
    };

    return fetch("http://localhost:8080/accounts", requestOptions).then(handleResponse);
}


function handleResponse(response){
    return response.text().then(text => {
        const data = text && JSON.parse(text);
        if(!response.ok){
            if(response.status === 403){
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
function authorizationHeader(){
    
    let user = JSON.parse(localStorage.getItem('user'));

    if(user && user.jwtToken){
        return { 'Authorization': `Bearer ${user.jwtToken}`};
    } else {
        return {};
    }
}

function getTokenString(){
    let user = JSON.parse(localStorage.getItem('user'));

    if(user && user.jwtToken){
        return `Bearer ${user.jwtToken}`;
    } else {
        return null;
    }
}


function getAccountDetails(){
    
    // get token from localstorage
    let token = JSON.parse(localStorage.getItem("user"));
    try{
        // decode token and attach to account object
        let decoded = VueJwtDecode.decode(token.jwtToken);
        let accountDetails = decoded;
        return accountDetails;
    }catch(error){
        console.log(error, "error from decoding token");
        return null;
    }
}

function isLoggedIn(){
    
    // get token from localstorage
    let user = JSON.parse(localStorage.getItem("user"));
    if(!user || !user.jwtToken){
        return false;
    }

    console.log(JSON.stringify(user.jwtToken));

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${user.jwtToken}`
        },
        body: JSON.stringify({jwtToken: user.jwtToken}) 
    }

    return fetch("http://localhost:8080/accounts/validateToken", requestOptions)
        .then(handleResponse);
}