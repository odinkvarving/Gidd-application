<template>
    <div>
        <FrontPageNav />
        <div class="front-page">
            <RegistrationBox />
        </div>
    </div>
</template>

<script>
import RegistrationBox from "../components/UserComponents/RegistrationBox.vue"
import FrontPageNav from "../components/Nav/FrontPageNav.vue"

export default {
    name: "Register",
    components: {
        RegistrationBox,
        FrontPageNav,
    }
}
</script>

<style>

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

</style>