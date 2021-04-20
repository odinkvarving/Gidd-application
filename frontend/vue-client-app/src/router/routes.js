import Home from "./Home.vue";
import Register from "./Register.vue";
import Login from "./Login.vue"
import ForgotPasswordPage from "./ForgotPasswordPage.vue"
import ProfilePage from "./ProfilePage.vue"
import Dashboard from "./Dashboard.vue"
import Activity from "./Activity.vue"


const routes = [
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/register",
    name: "Register",
    component: Register,
  },
  {
    path: "/login",
    name: "Login",
    component: Login,
  },
  {
    path: "/forgotPassword",
    name: "forgotPassword",
    component: ForgotPasswordPage
  },
  {
    path:'/accounts/:userId',
    name:'Profile',
    component:ProfilePage
  },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/dashboard/activity/:id",
    name: "Activity",
    component: Activity,
  }
];

export default routes;