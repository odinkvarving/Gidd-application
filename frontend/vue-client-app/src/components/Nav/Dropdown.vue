<template>
  <div
    class="menu-item"
    @click="isVisible = !isVisible"
    v-on-clickaway="hideDropdown"
  >
    <img :src="require('@/assets/' + icon + '')" class="profile-picture" :style="'width:' + width + '; height:' + height + ''">
    <transition name="fade">
      <div class="sub-menu" v-if="isVisible">
        <div v-for="(item, i) in items" :key="i" class="menu-item">
          <p @click="item.method()" :to="item.link">{{ item.title }}</p>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { mixin as clickaway } from "vue-clickaway";

export default {
  name: "dropdown",
  props: ["icon", "items", "width", "height"],
  mixins: [clickaway],
  data() {
    return {
      isVisible: false,
    };
  },
  methods: {
    toggleDropdown() {
      this.isVisible = !this.isVisible;
    },
    hideDropdown() {
      this.isVisible = false;
    },
  },
};
</script>

<style>
nav .menu-item .sub-menu {
  background-color: white;
  position: absolute;
  top: calc(100%);
  transform: translateX(-60%);
  width: max-content;
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
</style>
