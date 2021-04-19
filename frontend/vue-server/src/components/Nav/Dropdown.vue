<template>
  <div class="menu-item" @click="isVisible = !isVisible">
    <a xlink:href="#">
      {{ icon }}
    </a>
    <div class="sub-menu" v-if="isVisible">
      <div v-for="(item, i) in items" :key="i" class="menu-item">
        <router-link :to="item.link">{{ item.title }}</router-link>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "dropdown",
  props: ["icon", "items"],
  data() {
    return {
      isVisible: false,
    };
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
</style>
