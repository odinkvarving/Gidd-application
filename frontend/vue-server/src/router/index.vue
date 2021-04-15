<template>
    <div class="navigation-bar">
        <button class="btn login" @click="showLoginBox"><span>Logg inn</span></button>
    </div>
    <div class="front-page">
        <div class="header-container" v-if="!isRegistrationBoxVisible && !isLoginBoxVisible">
            <div class="title">GIDD</div>
            <div class="paragraph">Det er bare å gidde</div>
            <button class="btn register" @click="showRegistrationBox"><span>Registrer deg</span></button>
        </div>
        <login-box v-show="isLoginBoxVisible" @close="closeLoginBox"/>
        <registration-box v-show="isRegistrationBoxVisible" @close="closeRegistrationBox"/>
    </div>
    <div class="test"></div>
</template>

<script>
import LoginBox from '../components/UserComponents/LoginBox.vue';
import RegistrationBox from "../components/UserComponents/RegistrationBox.vue"

export default {
  name: 'index',
  components: {
      'registration-box': RegistrationBox,
      'login-box': LoginBox,
  },
  data() {
      return {
          isRegistrationBoxVisible: false,
          isLoginBoxVisible: false,
      }
  },
  methods: {
      showRegistrationBox() {
          this.isRegistrationBoxVisible = true;
          console.log("Registration Box Clicked");
      },
      closeRegistrationBox() {
          this.isRegistrationBoxVisible = false;
          console.log("Close Registration Box Clicked");
      },
      showLoginBox() {
          if (this.isRegistrationBoxVisible)
            this.isRegistrationBoxVisible = false;
            this.isLoginBoxVisible = true;
          console.log("Login Box Clicked");
      },
      closeLoginBox() {
          this.isLoginBoxVisible = false;
          console.log('Close Login Box Clicked');
      },
  },
}
</script>

<style scoped>

.test {
    height: 1000px;
}

.navigation-bar {
    display: flex;
    justify-content: flex-end;
    position: fixed;
    top: 0;
    width: 100%;
    height: 8vh;
}

.front-page {
    height: 100vh;
    width: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    
    background: linear-gradient(to bottom, rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.3)),
    url('~@/assets/frontpage_background.jpg') no-repeat center center fixed;
    -webkit-background-size: cover;
    -moz-background-size: cover;
    -o-background-size: cover;
    -webkit-overflow-scrolling: touch;
    background-size: cover;
}

.header-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    margin: auto;
}

.title {
    font-size: 32px;
    color: white;
}

.paragraph {
    margin-bottom: 20px;
    color: white;
}

.btn {
    background: none;
    color: #FFF;
    cursor: pointer;
    transition: all 0.5s;
    position: relative;
    padding: 0;
    border:none;
    outline: none;
}

.btn::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1;
    background-color: rgba(255,255,255,0.12);
    transition: all 0.3s;
}

.btn:hover::before {
    opacity: 0;
    transform: scale(0.5, 0.5);
}

.btn::after {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 1;
    opacity: 0;
    transition: all 0.3s;
    border: 1px solid rgba(255,255,255,0.5);
    transform: scale(1.2,1.2);
}

.btn:hover::after {
    opacity: 1;
    transform: scale(1, 1);
}

.login {
    margin: 20px 40px 0 0;
    height: 40px;
    width: 120px;
}

.register {
    height: 40px;
    width: 160px;
}


</style>