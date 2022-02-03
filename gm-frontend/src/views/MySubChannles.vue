<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>My Subscriptions</ion-title>
      <GoBack :routeName="routeName"/>
    </ion-header>

    <ion-content>
      <ion-refresher slot="fixed" @ionRefresh="doRefresher($event)">
        <ion-refresher-content :pulling-icon="syncOutline" refreshing-spinner="crescent"></ion-refresher-content>
      </ion-refresher>

      <!-- My subscribed channles -->
      <ion-list class="channel-list">
        <ion-item v-for="(item, index) in channels" :key="item.id">
          <ion-thumbnail slot="start" @click="onChannelClick(item, index)">
            <img :src="item.logo" />
          </ion-thumbnail>
          <ion-label @click="onChannelClick(item, index)">{{ item.name }}</ion-label>
          <ion-icon
            slot="end"
            :icon="ellipsisVertical"
            @click="onDeleteChannelsSubscription(item, index)"
          ></ion-icon>
        </ion-item>
      </ion-list>

      <!-- Load my channles -->
      <ion-infinite-scroll
        @ionInfinite="doNextPage($event)"
        threshold="100px"
        id="infinite-scroll"
        :disabled="isInfiniteDisabled"
      >
        <ion-infinite-scroll-content></ion-infinite-scroll-content>
      </ion-infinite-scroll>
    </ion-content>
  </ion-page>
</template>

<script>
import { getChannelsSubscription, deleteChannelsSubscription } from '../api/channel';
import GoBack from '@/components/GoBack.vue';
import { ellipsisVertical, syncOutline } from 'ionicons/icons';
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonRefresher,
  IonRefresherContent,
  IonInfiniteScroll,
  IonInfiniteScrollContent,
  IonText,
  IonLabel,
  IonItem,
  IonList,
  IonIcon,
  IonThumbnail
} from '@ionic/vue';

export default {
  name: 'AllChannels',
  components: {
    GoBack,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonRefresher,
    IonRefresherContent,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonPage,
    IonText,
    IonLabel,
    IonItem,
    IonList,
    IonIcon,
    IonThumbnail
  },
  setup() {
    return {
      ellipsisVertical,
      syncOutline
    };
  },
  data() {
    return {
      channels: [],
      isInfiniteDisabled: false,
      currentParams: {
        size: 10,
        page: 0
      },
	  routeName:''
    };
  },
  
  mounted() {
    this.getChannels();
  },
  beforeRouteEnter(to,from,next){
  	next(vm=>{
		let fromNames;
  		vm.routeName = 'channels';
  	})
  },
  methods: {
    doRefresher(e) {
      console.log('Begin async operation');
      this.getChannels();
      setTimeout(() => {
        console.log('Async operation has ended');
        e.target.complete();
      }, 500);
    },
    // Load more channles
    doNextPage(e) {
      console.log('Begin Load data');
      this.getNextChannels();
      setTimeout(() => {
        console.log('Loaded data');
        e.target.complete();
      }, 800);
    },
    getNextChannels() {
      this.currentParams.page++;
      getChannelsSubscription(this.currentParams).then((res) => {
        if (res.data.code == 1) {
          this.channels = this.channels.concat(res.data.content);
        } else {
          this.isInfiniteDisabled = true;
        }
      });
    },
    // Get channels info
    getChannels() {
      this.currentParams.page = 0;
      getChannelsSubscription(this.currentParams).then((res) => {
        if (res.data.code == 1) {
          this.channels = res.data.content;
        } else {
          this.channels = [];
        }
      });
    },
    // Jump to channel details page
    onChannelClick(item, index) {
      this.$router.push({
		  name:'channleDetail',
		  params:{
			routeMode:'push'  
		  },
		  query:{
			id:item.channelsId
		  }
      });
    },
    // Delete subscribed channel
    onDeleteChannelsSubscription(item, index) {
      this.$common.actionSheet({
        header: 'Do you want to unsubscribe this channel ?',
        buttons: [
          {
            text: 'Unsubscribe',
            role: 'unsubscribe',
            handler: () => {
              this.doDeleteChannelsSubscription(item, index);
            }
          },
          {
            text: 'Cancel',
            role: 'cancel',
            handler: () => {
              console.log('Cancel clicked');
            }
          }
        ]
      });
    },
    doDeleteChannelsSubscription(item, index) {
      deleteChannelsSubscription(item.channelsId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            // this.$common.toast('remove subscription success');
            this.getChannels();
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    }
  }
};
</script>

<style scoped>
.container {
  padding-top: 15%;
}
ion-header {
  margin-bottom: 20px;
}
ion-title {
  padding-bottom: 5px;
}

/* My subscribed list */
.channel-list ion-thumbnail {
  --border-radius: 5px;
  margin: 10px 10px 0px -8px;
}
.channel-list ion-label {
  font-size: 1rem;
}
.channel-list ion-icon {
  font-size: 1rem;
}
.infinite-nodata {
  padding: 10px;
  text-align: center;
}
</style>
