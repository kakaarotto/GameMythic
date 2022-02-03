<template>
  <ion-page class="container">
    <ion-header>
      <!-- All Channels-->
      <div class="channels">
        <div class="channel-item" @click="goAddOutline">
          <ion-icon :icon="addOutline"></ion-icon>
          <div>All Channels</div>
        </div>

        <!-- My Subscribed channels -->
        <div class="channel-item" @click="goSubscriptions">
          <div id="my">
            <div id="sub-num">{{subscriptionSize}}</div>
          </div>
          <div>My Subscriptions</div>
        </div>
      </div>
    </ion-header>

    <ion-content>
      <ion-refresher slot="fixed" @ionRefresh="doRefresher($event)">
        <ion-refresher-content :pulling-icon="syncOutline" refreshing-spinner="crescent"></ion-refresher-content>
      </ion-refresher>

      <!-- View channels history -->
      <h6>Recently Viewed Channels</h6>
      <div class="history">
        <ion-list>
          <ion-item
            v-for="(item, index) in histories"
            :key="item.id"
            @click="onChannelDetail(item, index)"
          >
            <ion-thumbnail slot="start">
              <img :src="item.logo" />
            </ion-thumbnail>
            <ion-label>{{ item.name }}</ion-label>
          </ion-item>
        </ion-list>
      </div>

      <!-- Random channels -->
      <h6>Popular Channels</h6>
      <div
        class="channel-list"
        v-for="(item, index) in popularChannels"
        :key="'popular' + item.channelsId"
      >
        <div class="channel-header">
          <div class="channel-name" @click="onChannelDetail(item, index)">{{item.name}}</div>

          <!-- Subscribe button -->
          <div
            :class="item.subscription?'inactive activated':'inactive'"
            @click="onChangeChannelsSubscription(item, index)"
          >{{item.subscriptionCount + (item.subscription ? ' Subscribed' : ' Subscribe')}}</div>
        </div>

        <!-- Channel content body -->
        <div class="channel-content">
          <div
            class="game-list-item"
            v-for="(content, index) in item.contents"
            :key="content.contentId"
            @click="gotoContentDetail(content.contentId)"
          >
            <img
              v-if="content.imageList && content.imageList.length > 0"
              :src="content.imageList[0]"
              alt
            />
            <ion-text class="content-title">{{ content.title }}</ion-text>
            <div class="icon">
              <i class="ico icon-like"></i>
              <ion-text class="number">{{ content.goodCount }}</ion-text>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-h"></div>
    </ion-content>
  </ion-page>
</template>

<script>
import { addOutline, thumbsUpOutline, syncOutline } from 'ionicons/icons';
import {
  getChannelsSubscription,
  getChannelsHistory,
  getPopularChannels,
  addChannelsSubscription,
  deleteChannelsSubscription
} from '../api/channel';
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonRefresher,
  IonRefresherContent,
  IonText,
  IonIcon,
  IonButton,
  IonList,
  IonItem,
  IonLabel,
  IonThumbnail
} from '@ionic/vue';
export default {
  name: 'Channels',
  components: {
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonRefresher,
    IonRefresherContent,
    IonPage,
    IonText,
    IonIcon,
    IonButton,
    IonList,
    IonItem,
    IonLabel,
    IonThumbnail
  },
  setup() {
    return {
      addOutline,
      thumbsUpOutline,
      syncOutline
    };
  },
  data() {
    return {
      subscriptionSize: 0,
      histories: [],
      popularChannels: []
    };
  },
  mounted() {
    this.getChannelsHistory();
    this.getPopularChannels();
    this.getChannelsSubscription();
  },
  beforeDestroy() {
    this.$mybus.off('tabRriggerReload');
  },
  methods: {
    doRefresher(e) {
      console.log('Begin async operation');
      this.getChannelsHistory();
      this.getPopularChannels();
      this.getChannelsSubscription();
      setTimeout(() => {
        console.log('Async operation has ended');
        e.target.complete();
      }, 500);
    },
    // Go to content detail page
    gotoContentDetail(contentId) {
      this.$router.push({
        name: 'detail',
        params: {
          routeMode: 'push'
        },
        query: {
          contentId
        }
      });
    },
    // Go to all channels page
    goAddOutline() {
      this.$router.push({
        name: 'allChannles',
        params: {
          routeMode: 'push'
        }
      });
    },
    // Go to my subscriptions page
    goSubscriptions() {
      this.$router.push({
        name: 'mySubChannles',
        params: {
          routeMode: 'push'
        }
      });
    },
    // Get popular channels part
    getPopularChannels() {
      let params = {
        // Limit size 5
        size: 5,
        page: 0
      };
      getPopularChannels(params).then((res) => {
        if (res.data.code == 1) {
          this.popularChannels = res.data.content;
        } else {
          this.popularChannels = [];
        }
      });
    },
    // Get user subscription channle
    getChannelsSubscription() {
      let params = {
        size: 1,
        page: 0
      };
      getChannelsSubscription(params).then((res) => {
        if (res.data.code == 1) {
          this.subscriptionSize = res.data.totalElements;
        }
      });
    },
    // Show channle history list
    getChannelsHistory() {
      let params = {
        // List mumber
        size: 5,
        page: 0
      };
      getChannelsHistory(params).then((res) => {
        if (res.data.code == 1) {
          this.histories = res.data.content;
        } else {
          this.histories = [];
        }
      });
    },
    // Go to channle detail page
    onChannelDetail(item, index) {
      this.$router.push({
        name: 'channleDetail',
        params: {
          routeMode: 'push'
        },
        query: {
          id: item.channelsId
        }
      });
    },
    onChangeChannelsSubscription(item, index) {
      if (item.subscription) {
        this.deleteChannelsSubscription(item);
      } else {
        this.addChannelsSubscription(item);
      }
    },
    //  Add subscription channel
    addChannelsSubscription(item) {
      // this.$common.showLoading();
      addChannelsSubscription(item.channelsId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.$common.toast('Subscription Success');
            item.subscription = true;
            item.subscriptionCount++;
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    // Unsubscribe channel
    deleteChannelsSubscription(item) {
      // this.$common.showLoading();
      deleteChannelsSubscription(item.channelsId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            // this.$common.toast('remove subscription success');
            item.subscription = false;
            item.subscriptionCount--;
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
@import url('../common/svg/style.css');
.container {
  position: relative;
  padding-top: 40px;
}

/* Add channles & My sub */
.channels {
  display: flex;
  padding: 20px 13px;
  border-bottom: 1px solid #f2f2f2;
  background-color: #fff;
}

/* Add channles btn */
.channel-item {
  text-align: center;
  font-size: 0.9rem;
  margin-right: 14px;
}
.channel-item ion-icon {
  font-size: 2.4rem;
  color: #ccc;
  border-radius: 50%;
  border: 2px solid #ccc;
}
/* My sub btn */
#my {
  margin: 0 0 3px 36px;
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: #ff9820;
}
#sub-num {
  padding-top: 12px;
  font-size: 0.9rem;
  color: #fff;
}

/* Titile */
h6 {
  padding: 0 13px;
}

/* Channles History  */
.history {
  padding: 0 0 20px 0;
  border-bottom: 8px solid #f2f2f2;
}
.history ion-thumbnail {
  --border-radius: 4px;
  margin: 10px 10px 0px -8px;
}
.history ion-label {
  font-size: 0.95rem;
}
/* Random Channels */
.channel-list {
  padding: 0px 12px;
  margin-bottom: 16px;
}
.channel-list::after {
  content: '';
  width: 100%;
  display: block;
  margin: 0 auto;
  border-bottom: 1px solid #f2f2f2;
}
.channel-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.channel-name {
  color: #666;
  font-size: 0.95rem;
}
.channel-content {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}
.game-list-item {
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  transition: 0.3s;
  border-radius: 4px;
  background: #fff;
  margin-bottom: 10px;
  width: calc(50% - 3px);
  overflow: hidden;
  padding-bottom: 10px;
}
.game-list-item img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}
.game-list-item .content-title {
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  overflow: hidden;
}
.game-list-item .icon {
  display: flex;
  padding: 2px 8px 0 0;
  float: right;
  color: #999;
  font-size: 0.9rem;
  align-items: center;
}
.game-list-item .number {
  margin: 0 5px;
  font-size: 0.8rem;
  color: #666;
}
/* Subscribe btn */
.inactive {
  padding: 2px 14px;
  line-height: 1.3rem;
}
.activated {
  padding: 2px 14px;
  line-height: 1.3rem;
}
</style>
