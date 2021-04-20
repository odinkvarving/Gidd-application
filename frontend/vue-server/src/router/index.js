import ProfilePage from "../components/profileComponents/ProfilePage";
import {createRouter, createWebHistory} from "vue-router";

const routes=[
    {path:'/account/:userId',name:'Profile',component:ProfilePage}
]

const router=createRouter({
    history: createWebHistory(),
    routes
})

export default router;