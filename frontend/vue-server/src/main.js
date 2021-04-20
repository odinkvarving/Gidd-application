import {createApp} from 'vue'
import App from './App.vue'
import router from './router/index'
import home from './components/Home';
import profile from './components/profileComponents/ProfilePage'


const app=createApp(App);
app.use(router);
app.component('Home',home);
app.component('Profile',profile);
app.mount('#app');

