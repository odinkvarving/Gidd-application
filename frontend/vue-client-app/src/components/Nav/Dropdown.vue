<template>
  <div
    class="menu-item"
    @click="isVisible = !isVisible"
    v-on-clickaway="hideDropdown"
  >
    <a xlink:href="#">
      {{ icon }}
    </a>
    <transition name="fade">
      <div class="sub-menu" v-if="isVisible">
        <div v-for="(item, i) in items" :key="i" class="menu-item">
          <router-link :to="item.link">{{ item.title }}</router-link>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { mixin as clickaway } from "vue-clickaway";

export default {
  name: "dropdown",
  props: ["icon", "items"],
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
  position: absolute;
  top: calc(100% + 5px);
  left: 50%;
  transform: translateX(-60%);
  width: max-content;
  border: 1px solid black;
}

.fade-enter-active, .fade-leave-active {
  transition: all .1s ease-in-out;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>
