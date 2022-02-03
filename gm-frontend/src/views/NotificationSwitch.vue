<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>Notification Setting</ion-title>
      <GoBack :routeName="routeName"/>
    </ion-header>

    <ion-content>
      <ion-list>
        <!-- Notification Notification switch button -->
        <ion-item>
          <ion-label>Receive Notifications</ion-label>
          <ion-toggle color="warning" :checked="notificationEnabled" @ionChange="onSave"></ion-toggle>
        </ion-item>
      </ion-list>
    </ion-content>
  </ion-page>
</template>

<script>
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonText,
  IonLabel,
  IonItem,
  IonList,
  IonIcon,
  IonToggle
} from '@ionic/vue';
import GoBack from '@/components/GoBack.vue';
import { updateNotificationEnabled } from '../api/account';
export default {
  name: 'NotificationSwitch',
  components: {
    GoBack,
    IonPage,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonText,
    IonLabel,
    IonItem,
    IonList,
    IonIcon,
    IonToggle
  },
  data() {
    return {
      userInfo: null,
      notificationEnabled: true,
	  routeName:''
    };
  },
  created() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    this.notificationEnabled = this.userInfo.notificationEnabled;
    console.log(this.notificationEnabled);
  },
  beforeRouteEnter(to,from,next){
  	next(vm=>{
  		vm.routeName = from.name
  	})
  },
  methods: {
    // Save Notification setting
    onSave(e) {
      console.log(e.detail.checked);
      updateNotificationEnabled(e.detail.checked).then((res) => {
        if(res.data.code == 1) {
          this.notificationEnabled = e.detail.checked;
          this.userInfo.notificationEnabled = e.detail.checked
          localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
        }
        else {
          this.notificationEnabled = this.notificationEnabled
          this.$common.error(res.data.message)
        }
      })
      .catch(() => {
        this.notificationEnabled = this.notificationEnabled
      })
    }
  }
};
</script>

<style scoped>
.container {
  padding-top: 15%;
}
ion-header {
  height: 40px;
}
ion-title {
  height: 32px;
  padding-bottom: 5px;
}

.list-ios {
  padding-top: 30px;
  height: 100%;
  background-color: #f2f2f2;
}

.ion-focusable {
  --border-color: whitesmoke;
}
</style>

<style type="text/css">
.item-inner {
  padding-left: 0px;
  border: none;
}
</style>