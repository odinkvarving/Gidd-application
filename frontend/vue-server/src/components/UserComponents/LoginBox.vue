<template>
    <div class="login-box">
        <router-link to="/"><button id="close-btn"><span>x</span></button></router-link>
        <h2>GIDD og logg deg inn da</h2>

        <div id="facebook-btn">
            <img src="../../assets/facebook.png" alt="facebook logo" style="width: 32px; height: 32px">
            <p class="text">Logg inn med Facebook</p>
        </div>
        <div id="google-btn">
            <img src="../../assets/google.png" alt="google logo" style="width: 32px; height: 32px">
            <p class="text">Logg inn med Google</p>
        </div>
        <p class="or-text">eller bruk epost</p>

        <div class="input-container">
            <p v-if="!isEmailValid">Skriv inn en gyldig e-postadresse</p>
            <input type="text" class="input" id="email" placeholder="E-post" v-model="emailValue">
        </div>
        <div class="input-container">
            <p v-if="!isPasswordValid">Passord må være mellom 5 og 16 tegn</p>
            <input type="password" class="input" id="password" placeholder="Passord" v-model="passwordValue">
        </div>

        <button id="Login-btn" @click="loginUser">Logg inn</button>
    </div>
</template>

<script>

export default {
    name: "LoginBox",
    data(){
        return {
            isEmailValid: true,
            isPasswordValid: true,
            emailValue: '',
            passwordValue: ''
        }
    },
    methods: {
        loginUser(){
            console.log("Login button clicked");

            this.isEmailValid = true;
            this.isPasswordValid = true;
            if(!this.validateEmail()){
                this.isEmailValid = false;
            }
            if(!this.validatePassword()){
                this.isPasswordValid = false;
            }

            if(this.isEmailValid && this.isPasswordValid){
                console.log("All inputs are valid. Signing in...");
                this.login();
            }
        },
        validateEmail() {
            const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(String(this.emailValue).toLowerCase());
        },
        validatePassword() {
            return this.passwordValue.length >= 6 && this.passwordValue.length <= 16;
        },
        async login() {
            let loginData = {
                email: this.emailValue,
                password: this.passwordValue
            }

            await fetch("http://localhost:8080/accounts/login", {
                method: "POST",
                headers: {"Content-Type" : "application/json"},
                credentials: "include",
                body: JSON.stringify(loginData)
            })

            await this.$router.push("/dashboard");
        },
        handleLoginWithFacebook() {
            //Implement facebook compability
        },
        handleLoginWithGoogle() {
            //Implement google compability
        },
        closeWindow () {
            
        }
    }
}
</script>

<style>

.login-box {
    width: 510px;
    height: 600px;
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
    margin-right: 85px;
}

#google-btn:hover {
    background-color: #f1f1f1;
    cursor: pointer;
}

.or-text {
    font-size: 13px;
    opacity: 75%;
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
    display: flex;
    flex-direction: row;
    background: none;
    cursor: pointer;
    outline: none;
}

</style>