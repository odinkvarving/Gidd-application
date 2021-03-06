import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import Routes from "./router/routes.js"
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import VueMoment from 'vue-moment'
import * as VueGoogleMaps from 'vue2-google-maps'

// Import Bootstrap an BootstrapVue CSS files (order is important)
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

// Make BootstrapVue available throughout your project
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.use(VueRouter)
Vue.use(VueMoment);
Vue.use(VueGoogleMaps,{
  load: {
    key: 'AIzaSyB8KEwtdxowU0S-VNdmM5fE7lmed8Mlezw',
    libraries:'places',
  }
});

const router = new VueRouter({
  routes: Routes,
  mode: 'history'
});

new Vue({
  render: h => h(App),
  router: router
}).$mount('#app')


