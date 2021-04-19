<template>
    <div class="navigation-bar">
        <router-link to="/login"><button class="button login"><span>Logg inn</span></button></router-link>
    </div>
</template>

<script>
export default {
    name: "FrontPageNav",
    
}
</script>

<style>
    .navigation-bar {
        display: flex;
        justify-content: flex-end;
        position: fixed;
        top: 0;
        width: 100%;
        height: 8vh;
    }

    .login {
        margin: 20px 40px 0 0;
        height: 40px;
        width: 120px;
    }
    .button {
        background: none;
        color: #FFF;
        cursor: pointer;
        transition: all 0.5s;
        position: relative;
        padding: 0;
        border:none;
        outline: none;
    }

    .button::before {
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

    .button:hover::before {
        opacity: 0;
        transform: scale(0.5, 0.5);
    }

    .button::after {
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

    .button:hover::after {
        opacity: 1;
        transform: scale(1, 1);
    }

</style>