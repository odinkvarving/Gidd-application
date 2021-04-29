<template>
  <div
    class="menu-item"
    @click="showNotifications()"
    v-on-clickaway="hideDropdown"
  >
    <div class="notification-icon-container">
      <div v-if="showUnreadSymbol" id="notification-alert">
        <p style="margin: 0; color: white;">{{ unreadNotifications }}</p>
      </div>
      <img
        :src="require('@/assets/' + icon + '')"
        class="profile-picture"
        :style="'width:' + width + '; height:' + height + ''"
      />
    </div>
    <transition name="fade">
      <div class="sub-menu" v-if="isVisible">
        <div v-for="(item, i) in items" :key="i" class="menu-item">
          <div @click="handleClick(item)" class="notification-item">
            <p id="message">{{ item.message }}</p>
            <p id="date">{{ item.date }}</p>
            <b-icon
              id="close-notification"
              icon="x"
              @click.stop="removeNotification(item.id)"
            />
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { mixin as clickaway } from "vue-clickaway";
import { notificationService } from "../../services/NotificationService.js";
import { userService } from "../../services/UserService.js";

export default {
  name: "NotificationsDropdown",
  props: ["icon", "width", "height"],
  mixins: [clickaway],
  data() {
    return {
      isVisible: false,
      items: [],
      unreadNotifications: 0,
      showUnreadSymbol: false,
      isLoggedIn: false,
    };
  },
  async mounted() {
    this.isLoggedIn = await userService.isLoggedIn();
    if (this.isLoggedIn) {
      let accountId = await userService.getAccountByEmail().then((data) => {
        return data.id;
      });
      this.items = await notificationService.getAccountsNotifications(
        accountId
      );
      for (let i = 0; i < this.items.length; i++) {
        if (this.items[i].seen === false) {
          this.unreadNotifications++;
        }
      }

      if (this.unreadNotifications > 0) {
        this.showUnreadSymbol = true;
      }
    }
  },
  methods: {
    toggleDropdown() {
      this.isVisible = !this.isVisible;
    },
    hideDropdown() {
      this.isVisible = false;
    },
    handleClick(item) {
      let path = `/dashboard/activity/${item.activityId}`;
      notificationService.removeNotification(item.id).then((result) => {
        if (result === true) {
          console.log("Removed notification sucessfully");
        } else {
          console.log("Error removing notfication...");
        }
      });
      if (this.$router.currentRoute.path !== path) {
        this.$router.push(path);
      }
    },
    async showNotifications() {
      if (this.isLoggedIn) {
        let unread = 0;
        this.isVisible = !this.isVisible;
        let accountId = await userService.getAccountByEmail().then((data) => {
          return data.id;
        });
        this.items = await notificationService.getAccountsNotifications(
          accountId
        );
        for (let i = 0; i < this.items.length; i++) {
          if (this.items[i].seen === false) {
            unread++;
          }
        }

        if (unread > 0) {
          this.unreadNotifications = unread;
          this.showUnreadSymbol = true;
        }
        console.log(this.items);
        console.log(this.unreadNotifications);
      } else {
        this.$router.push("/login");
      }
    },
    async removeNotification(notificationId) {
      console.log("clicked");
      let result = await notificationService.removeNotification(notificationId);
      if (result === true) {
        console.log("Successfully removed notification");
        for (let i = 0; i < this.items.length; i++) {
          if (this.items[i].id === notificationId) {
            this.items.splice(i, 1);
          }
        }
      } else {
        console.log("Error removing notification...");
      }
    },
  },
};
</script>

<style>
nav .menu-item .sub-menu {
  background-color: white;
  position: absolute;
  top: calc(100%);
  transform: translateX(-40%);
  width: 250px;
  overflow-y: auto;
  border: 1px solid rgba(0, 0, 0, 0.245);
}

.fade-enter-active,
.fade-leave-active {
  transition: all 0.1s ease-in-out;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.profile-picture {
  height: 40px;
  width: 40px;
  object-fit: cover;
  border-radius: 100%;
}

.notification-item {
  width: 100%;
  padding: 8px;
  font-family: "Mulish";
  text-overflow: ellipsis;
  line-height: 1.5em;
  height: 7.5em;
  overflow: hidden;
  position: relative;
}

.notification-item:hover {
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

.notification-icon-container {
  position: relative;
}

#notification-alert {
  position: absolute;
}

#notification-alert {
  width: 18px;
  height: 18px;
  background-color: #f70128;
  border-radius: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-family: "Mulish";
  z-index: 10;
}

#close-notification {
  position: absolute;
  top: 27%;
  right: 0;
  font-size: 28px;
  opacity: 75%;
  transition: 0.3s;
}

#close-notification:hover {
  transition: 0.3s;
  font-size: 29px;
}

@media (max-width: 767px) {
  nav .menu-item .sub-menu{
      transform: translateX(-11%);
  }
}
</style>
