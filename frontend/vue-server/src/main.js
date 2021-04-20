import {createApp} from 'vue'
import App from './App.vue'
import router from './router/index'
import ProfilePage from "./components/profileComponents/ProfilePage";

const app=createApp(App);
app.use(router);
app.component(ProfilePage)
app.mount('#app');

