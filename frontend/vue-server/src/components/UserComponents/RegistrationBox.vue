<template>
    <div class="registration-box">
        <h2>Registrer deg hos GIDD idag<br />Det er bare å "GIDDE"</h2>

        <div id="facebook-btn">
            <img src="../../assets/facebook.png" alt="facebook logo" style="width: 32px; height: 32px">
            <p class="text">Registrer deg med Facebook</p>
        </div>
        <div id="google-btn">
            <img src="../../assets/google.png" alt="google logo" style="width: 32px; height: 32px">
            <p class="text">Registrer deg med Google</p>
        </div>
        <p class="or-text">eller bruk epost</p>


        <div class="input-container">
            <p v-if="!nameValid">Ikke gyldig navn!</p>
            <input type="text" class="input" id="name" placeholder="Fullt navn" v-model="nameValue">
        </div>
        <div class="input-container">
            <p v-if="!phoneValid">Ikke gyldig telefon nummmer!(Husk landskode!)</p>
            <input type="text" class="input" id="phone" placeholder="Telefon" v-model="phoneValue">
        </div>
        <div class="input-container">
            <p v-if="!emailValid">Vennligst skriv inn en email!</p>
            <input type="text" class="input" id="email" placeholder="E-post" v-model="emailValue">
        </div>
        <div class="input-container">
            <p v-if="!passwordValid">Passord må være mellom 5 og 16 tegn!</p>
            <input type="password" class="input" id="password" placeholder="Password" v-model="passwordValue">
        </div>

        <button id="register-btn" @click="registerUser">Register</button>
        
    </div>
</template>

<script>

export default {
    name: "RegistrationBox",
    data(){
        return {
            nameValid: true,
            phoneValid: true,
            emailValid: true,
            passwordValid: true,
            nameValue: '',
            phoneValue: '',
            emailValue: '',
            passwordValue: ''
        }
    },
    methods: {
        registerUser(){
            console.log("Register button clicked");

            this.nameValid = true;
            this.phoneValid = true;
            this.emailValid = true;
            this.passwordValid = true;
            if(!this.validateName()){
                this.nameValid = false;
            }
            if(!this.validatePhone()){
                this.phoneValid = false;
            }
            if(!this.validateEmail()){
                this.emailValid = false;
            }
            if(!this.validatePassword()){
                this.passwordValid = false;
            }

            if(this.nameValid && this.phoneValid && this.emailValid && this.passwordValid){
                console.log("All inputs are valid! Creating user...");
                this.sendNewUserToServer();
            }
        },
        validateName(){
            let regName = /^[a-zA-Z]+ [a-zA-Z]+$/;
            return regName.test(this.nameValue);
        },
        validatePhone(){
            let regPhone = /^\+?([0-9]{2})\)?[-. ]?([0-9]{4})[-. ]?([0-9]{4})$/;
            return regPhone.test(this.phoneValue);
        },
        validateEmail() {

            //Check if email already exists method. Only run if the test underneath passes

            const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
            return re.test(String(this.emailValue).toLowerCase());
        },
        validatePassword(){
            return this.passwordValue.length >= 6 && this.passwordValue.length <= 16;
        },
        sendNewUserToServer(){
            
        },
        handleRegisterWithFacebook(){
            //Implement facebook compability
        },
        handleRegisterWithGoogle(){
            //Implement google compability
        }
    }
}
</script>

<style>

.registration-box {
    width: 563px;
    height: 790px;

    display: flex;
    flex-flow: column nowrap;
    align-items: center;
    justify-content: center;
    box-shadow: 2px 2px 4px #b8b3b3;
    margin: 0;
    padding: 0;
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
}

#register-btn{
    margin-top: 20px;
    font-family: "Mulish";
    font-size: 20px;
    width: 250px;
    height: 42px;
    color: white;
    background: #FFBD3E;
    border: none;
    border-radius: 6px;
}

#register-btn:hover{
    background-color: #efb03a;
    cursor: pointer;
}

</style>