<template>
  <ion-app>
    <router-view v-slot="{ Component, route }">
      <transition :name="route.meta.transitionName">
        <keep-alive :include="cachePublish">
          <component :is="Component" :key="$route.path" />
        </keep-alive>
      </transition>
    </router-view>
  </ion-app>
</template>

<script>
import { IonApp } from '@ionic/vue';
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'App',
  components: {
    IonApp
  },
  data() {
    return {
      cachePublish: ['Publish,Home']
    };
  }
});
</script>
<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();
let routerArrItem = [];
router.beforeEach((to, from) => {
  routerArrItem.push(from.name);
  routerArrItem = Array.from(new Set(routerArrItem));
  if (from.name == 'detail' && routerArrItem.indexOf('mySubChannles') > -1) {
    localStorage.setItem('routerName', 'mySubChannles');
  } else {
    localStorage.setItem('routerName', 'channels');
  }
  if (to.name == 'channels') {
    routerArrItem = [];
  }
  console.log(routerArrItem);
  const toDepth = to.params.routeMode;
  if (toDepth === 'push') {
    to.meta.transitionName = 'slide-left';
  } else if (toDepth === 'pop') {
    to.meta.transitionName = 'slide-right';
  }
  return true;
});
</script>