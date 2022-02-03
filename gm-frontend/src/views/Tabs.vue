<template>
  <ion-page>
    <ion-tabs @ionTabsDidChange="afterTabChange">
      <!-- <ion-router-outlet></ion-router-outlet> -->
      <ion-tab-bar slot="button">
        <ion-tab-button tab="home" href="/tabs/home">
          <ion-icon class="navbtn" :icon="homeOutline" @click="reloadTabPage('home')" />
          <ion-label>Home</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="channels" href="/tabs/channels">
          <ion-icon class="navbtn" :icon="gridOutline" />
          <ion-label>Channels</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="add">
          <ion-icon class="add" :icon="addCircle" @click="publish" />
        </ion-tab-button>

        <ion-tab-button tab="notification" href="/tabs/notification">
          <ion-icon class="navbtn" :icon="notificationsOutline" />
          <ion-label>Notification</ion-label>
        </ion-tab-button>

        <ion-tab-button tab="profile" href="/tabs/profile">
          <ion-icon class="navbtn" :icon="personOutline" />
          <ion-label>Profile</ion-label>
        </ion-tab-button>
      </ion-tab-bar>
    </ion-tabs>
  </ion-page>
</template>

<script>
import {
  IonTabBar,
  IonTabButton,
  IonTabs,
  IonLabel,
  IonIcon,
  IonPage,
  IonContent,
  IonRouterOutlet
} from '@ionic/vue';
import {
  homeOutline,
  gridOutline,
  notificationsOutline,
  personOutline,
  addCircle
} from 'ionicons/icons';

export default {
  name: 'Tabs',
  components: {
    IonLabel,
    IonTabs,
    IonTabBar,
    IonTabButton,
    IonIcon,
    IonPage,
    IonContent,
    IonRouterOutlet
  },
  setup() {
    return {
      homeOutline,
      gridOutline,
      notificationsOutline,
      personOutline,
      addCircle
    };
  },
  data() {
    return {
      currentTab: ''
    };
  },
  methods: {
    reloadTabPage(tab) {
      console.log('reloadTabPage', this.currentTab);
      if (this.currentTab == tab) {
        this.$mybus.emit('tabRriggerReload', null);
      }
    },
    afterTabChange(e) {
      this.currentTab = e.tab;
      console.log('afterTabChange', this.currentTab);
    },
    publish() {
      this.$router.push({
        name: 'publish',
        params: {
          routeMode: 'push'
        }
      });
    }
  }
};
</script>

<style scoped>
.add {
  font-size: 45px;
  color: #ff9820;
}
.navbtn {
  font-size: 1.5rem;
}

ion-tab-button {
  --color-selected: #ff9820;
}
</style>
