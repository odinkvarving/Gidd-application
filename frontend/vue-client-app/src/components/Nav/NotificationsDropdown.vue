<template>
  <div
    class="menu-item"
    @click="showNotifications()"
    v-on-clickaway="hideDropdown"
  >
    <img :src="require('@/assets/' + icon + '')" class="profile-picture" :style="'width:' + width + '; height:' + height + ''">
    <transition name="fade">
      <div class="sub-menu" v-if="isVisible">
        <div v-for="(item, i) in items" :key="i" class="menu-item">
          <div @click="handleClick(item)" class="notification-item">
                <p id="message">{{ item.message }}</p>
                <p id="date">{{ item.date }}</p>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { mixin as clickaway } from "vue-clickaway";
import { notificationService } from "../../services/NotificationService.js"
import { userService } from '../../services/UserService.js';

export default {
  name: "NotificationsDropdown",
  props: ["icon", "width", "height"],
  mixins: [clickaway],
  data() {
    return {
      isVisible: false,
      items: []
    };
  },
  methods: {
    toggleDropdown() {
      this.isVisible = !this.isVisible;
    },
    hideDropdown() {
      this.isVisible = false;
    },
    handleClick(item){
        let path = `/dashboard/activity/${item.activityId}`;
        if(this.$router.currentRoute.path !== path){
            this.$router.push(path);
        }
    },
    async showNotifications(){
        this.isVisible = !this.isVisible;
        let accountId = await userService.getAccountByEmail().then(data => {
          return data.id
        });
        this.items = await notificationService.getAccountsNotifications(accountId);
        console.log(this.items);
    }
  },
};
</script>

<style>
nav .menu-item .sub-menu {
  background-color: white;
  position: absolute;
  top: calc(100%);
  transform: translateX(-60%);
  width: 250px;
  max-height: 30vh;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid rgba(0, 0, 0, 0.245)
}

.fade-enter-active, .fade-leave-active {
  transition: all .1s ease-in-out;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}

.profile-picture {
  height: 40px;
  width: 40px;
  object-fit: cover;
  border-radius: 100%;
}

.notification-item {
    padding: 8px;
    font-family: "Mulish";
    text-overflow: ellipsis;
    line-height: 1.5em;
    height: 7.5em;
    overflow: hidden;
}

.notification-item:hover{
    background-color: rgba(0, 0, 0, 0.145);
}

.notification-item p {
    margin: 0;
}

.notification-item #date {
    opacity: 70%;
    font-size: 15px;
}

.notification-item #message {
    font-size: 17px;
}
</style>
