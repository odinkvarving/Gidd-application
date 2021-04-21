<template>
  <div class="body">
    <FrontPageNav />
    <div class="front-page">
      <ForgotPasswordBox />
    </div>
  </div>
</template>

<script>
import FrontPageNav from "../components/Nav/FrontPageNav.vue";
import ForgotPasswordBox from "../components/ForgotPasswordComponents/ForgotPasswordBox.vue";

export default {
  name: "Login",
  components: {
    FrontPageNav,
    ForgotPasswordBox,
  },
};
</script>

<style>
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