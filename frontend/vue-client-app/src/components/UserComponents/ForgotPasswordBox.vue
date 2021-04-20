<template>
    <div class="login-box">
        <router-link to="/"><p class="close-btn-p"><b-icon class="text-muted" id="close-btn" icon="x"></b-icon></p></router-link>

    </div>
</template>

<script>

import { userService } from "../../services/UserService.js"


export default {
    name: "LoginBox",
    data(){
        return {
            isEmailValid: true,
            isPasswordValid: true,
            emailValue: '',
            passwordValue: '',
            showLoginError: false
        }
    },
    methods: {
        loginUser(){
            console.log("Login button clicked");

            this.isEmailValid = true;
            if(!this.validateEmail()){
                this.isEmailValid = false;
            }
            if(this.isEmailValid){
                console.log("All inputs are valid. Trying to sign in...");
                this.login();
            }
        },
        validateEmail() {
            const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(String(this.emailValue).toLowerCase());
        },
        async login() {
            
            let response = await userService.login(this.emailValue, this.passwordValue);

            if(response.jwtToken){
                console.log("Login successfull! Token: ");
                console.log(response.jwtToken);
            }else{
                console.log("Invalid login credentials.");
                this.showLoginError = true;
            }

            // TODO: create error when wrong username and password, and redirect to either profile
            // or activity dashboard
        },
        handleLoginWithFacebook() {
            //Implement facebook compability
        },
        handleLoginWithGoogle() {
            //Implement google compability
        },
        closeInvalidLoginError(){
            this.showLoginError = false;
        }
    }
}
</script>

<style scoped>

.header {
    margin-top: 50px;
}

.login-box {
    position: relative;
    width: 510px;
    display: flex;
    flex-flow: column nowrap;
    align-items: center;
    justify-content: center;
    margin: 0;
    padding: 0;
    background: white;
}

#facebook-btn {
    margin: 15px 0;
    width: 394px;
    height: 51px;
    background-color: #3b5998;
    border-radius: 6px;
    border: 1px solid #dfdfe8;
    color: white;
    display: flex;
    flex-flow: row nowrap;
    justify-content: space-between;
    align-items: center;
}

#facebook-btn p {
    margin-top: 15px;
    margin-right: 80px;
}
#facebook-btn img {
    margin: 0 10px;
}

#facebook-btn:hover {
    background-color: #2f4779;
    cursor: pointer;
}

#google-btn {
    margin: 15px 0 0 0;
    width: 394px;
    height: 51px;
    border-radius: 6px;
    border: 1px solid #dfdfe8;

    
    display: flex;
    flex-flow: row nowrap;
    justify-content: space-between;
    align-items: center;
}

#google-btn img{
    margin: 0 10px;
}

#google-btn p{
    margin-top: 15px;
    margin-right: 85px;
}

#google-btn:hover {
    background-color: #f1f1f1;
    cursor: pointer;
}

.or-text {
    font-size: 13px;
    opacity: 75%;
    margin-top: 17px;
}

.input-container{
    font-family: "Mulish";
    margin: 10px 0;
    display: flex;
    flex-flow: column;
}

.input-container p{
    margin: 0 0 4px 0;
    font-size: 13px;
    align-self: flex-start;
    color: red;
}

.input{
    width: 372px;
    height: 50px;
    padding: 0 10px;
    background-color: #F6F6F6;
    border: none;
    outline: none;
}

#Login-btn{
    margin-top: 20px;
    font-family: "Mulish";
    font-size: 20px;
    width: 250px;
    height: 42px;
    color: white;
    background: #FFBD3E;
    border: none;
    border-radius: 6px;
    outline: none;
}

#Login-btn:hover{
    background-color: #efb03a;
    cursor: pointer;
}

#close-btn{
    position: absolute;
    top: 5px;
    right: 5px;
}

.close-btn-p{
    font-size: 50px;
}

.register-text {
    opacity: 75%;
    margin-top: 15px;
    font-size: 14px;
    margin-bottom: 20px;
}

.register-text:hover{
    cursor: pointer;
    text-decoration: underline;
}

.invalid-login{
    width: 75%;
    height: 50px;
    background-color: #ffdbdf;
    border: solid 1px #b7666a;
    margin: 20px 0 40px 0;
    display: flex;
    align-items: center;
    justify-content: space-between;
}

.invalid-login .invalid-login-text{
    color: #91595c;
    font-size: 13px;
    margin: 0 0 0 20px;

}

.x-icon {
    font-size: 20px;
    color: #492728;
    margin: 0 10px 0 0;
}

.x-icon:hover {
    cursor: pointer;
}

.spacing {
    margin-bottom: 40px;
}
</style>