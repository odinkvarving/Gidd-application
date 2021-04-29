<template>
  <div class="body">
    <FrontPageNav />
    <div class="front-page">
      <ResetPasswordBox />
    </div>
  </div>
</template>

<script>
import FrontPageNav from "../components/Nav/FrontPageNav.vue";
import ResetPasswordBox from "../components/ResetPassword/ResetPasswordBox.vue";

export default {
  name: "Login",
  components: {
    FrontPageNav,
    ResetPasswordBox,
  },
};
</script>

<style>
.input-box {
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

.front-page {
  height: 100vh;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;

  background: linear-gradient(to bottom, rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.3)),
    url("~@/assets/frontpage_background.jpg") no-repeat center center fixed;
  -webkit-background-size: cover;
  -moz-background-size: cover;
  -o-background-size: cover;
  -webkit-overflow-scrolling: touch;
  background-size: cover;
}
</style>