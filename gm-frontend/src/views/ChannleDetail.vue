<template>
  <ion-page>
    <ion-header>
      <ChannleGoBack :routeName="routeName" />
      <div class="channle-header">
        <div class="channle-cover-txt">{{detail.name}}</div>
        <div class="channle-cover-btn">
          <div
            :class="detail.subscription?'inactive activated':'inactive'"
            @click="onChangeChannelsSubscription()"
          >{{(detail.subscription ? ' Subscribed' : ' Subscribe')}}</div>
        </div>
      </div>
    </ion-header>
    <ion-content :scroll-events="true" ref="ionContent">
      <ion-refresher slot="fixed" @ionRefresh="doRefresher($event)">
        <ion-refresher-content :pulling-icon="syncOutline" refreshing-spinner="crescent"></ion-refresher-content>
      </ion-refresher>

      <!-- Channle connent list -->
      <div class="game">
        <div class="game-list" ref="gamelist">
          <div
            class="game-list-item"
            v-for="(item, index) in list"
            :key="index"
            @click="gotoDetail(item.contentId)"
          >
            <img :src="item.images[0]" alt />
            <ion-text class="content-title">{{ item.title }}</ion-text>
            <div class="icon">
              <i class="ico icon-like"></i>
              <ion-text class="number">{{ item.goodCount }}</ion-text>
            </div>
          </div>
        </div>
      </div>

      <!-- Load more contnet -->
      <ion-infinite-scroll
        @ionInfinite="doNextPage($event)"
        threshold="1%"
        id="infinite-scroll"
        :disabled="isInfiniteDisabled"
      >
        <ion-infinite-scroll-content></ion-infinite-scroll-content>
      </ion-infinite-scroll>
    </ion-content>
  </ion-page>
</template>

<script>
import {
  getChannelsDetail,
  addChannelsSubscription,
  deleteChannelsSubscription,
  addChannelsHistory
} from '../api/channel';
import { getContents } from '../api/content';
import ChannleGoBack from '@/components/ChannleGoBack.vue';
import { thumbsUpOutline, syncOutline } from 'ionicons/icons';
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonText,
  IonContent,
  IonButton,
  IonIcon,
  IonList,
  IonSlides,
  IonSlide,
  IonRefresher,
  IonRefresherContent,
  IonInfiniteScroll,
  IonInfiniteScrollContent
} from '@ionic/vue';

export default {
  name: 'Detail',
  components: {
    IonPage,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonButton,
    IonText,
    IonIcon,
    IonList,
    IonSlides,
    IonSlide,
    IonRefresher,
    IonRefresherContent,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    ChannleGoBack
  },
  setup() {
    return {
      thumbsUpOutline,
      syncOutline
    };
  },
  data() {
    return {
      channelsId: null,
      list: [],
      detail: {
        subscriptionCount: 0,
        subscription: false,
        name: 'Detail'
      },
      isInfiniteDisabled: false,
      currentParams: {
        size: 8,
        page: 0,
        collect: false,
        categoryId: ''
      },
      routeName: ''
    };
  },
  // Get routing hook function
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      let fromNames;

      switch (from.name) {
        case 'allChannles':
          fromNames = 'allChannles';
          break;
        case 'channels':
          fromNames = 'channels';
          break;
        case 'mySubChannles':
          fromNames = 'mySubChannles';
          break;
        case 'detail':
          fromNames = !localStorage.routerName ? 'channels' : localStorage.routerName;
          break;
        default:
          fromNames = 'home';
      }
      vm.routeName = fromNames;
    });
  },
  mounted() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
  },
  beforeDestroy() {
    this.$mybus.off('tabRriggerReload');
  },
  created() {
    if (this.$route.query.id) {
      this.channelsId = this.$route.query.id;
      this.getChannelsDetail();
      this.getContents();
      this.addChannelsHistory();
    }
  },
  methods: {
    doRefresher(e) {
      console.log('Begin async operation');
      this.getContents();
      setTimeout(() => {
        console.log('Async operation has ended');
        e.target.complete();
      }, 500);
    },
    // Change channel subscription status
    onChangeChannelsSubscription() {
      if (this.detail.subscription) {
        this.deleteChannelsSubscription();
      } else {
        this.addChannelsSubscription();
      }
    },
    addChannelsSubscription() {
      addChannelsSubscription(this.channelsId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.$common.toast('Subscription Success');
            this.detail.subscription = true;
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    deleteChannelsSubscription() {
      deleteChannelsSubscription(this.channelsId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.detail.subscription = false;
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    // Load next page
    doNextPage(e) {
      console.log('Begin Load data');
      this.getNexPageContents(() => {
        console.log('Loaded data');
        e.target.complete();
      });
    },
    gotoDetail(contentId) {
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
    addChannelsHistory() {
      addChannelsHistory(this.channelsId).then((res) => {});
    },
    // Get the content in the channel
    getContents(callback) {
      this.list = [];
      let params = {
        size: 8,
        page: 0,
        channelsId: this.channelsId
      };
      this.$common.showLoading();
      getContents(params).then((res) => {
        if (typeof callback === 'function') {
          callback();
        }
        if (res.data.code == 1) {
          let data = res.data.content;
          for (let item of data) {
            item.images = item.images.split(',');
          }
          this.$common.hideLoading();
          // Random sorting
          data.sort(() => {
            return Math.random() - 0.5;
          });
          this.list = data;
        } else {
          this.list = [];
        }
      });
    },
    getNexPageContents(callback) {
      this.currentParams.page++;
      this.currentParams.channelsId = this.channelsId;
      this.currentParams.collect = false;
      getContents(this.currentParams).then((res) => {
        if (typeof callback === 'function') {
          callback();
        }
        if (res.data.code == 1) {
          let data = res.data.content;
          for (let item of data) {
            item.images = item.images.split(',');
            this.list.push(item);
          }
        } else {
          this.isInfiniteDisabled = true;
        }
      });
    },
    getChannelsDetail() {
      getChannelsDetail(this.channelsId).then((res) => {
        if (res.data.code == 1) {
          this.detail = res.data.content;
        }
      });
    }
  }
};
</script>

<style scoped>
@import url('../common/svg/style.css');
ion-header {
  padding-top: 50px;
  height: 180px;
  width: 100%;
  background-position: center;
  position: relative;
  background: #2e2a20;
}
.channle-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
.channle-cover-txt {
  margin-top: -24px;
  font-size: 1rem;
  font-weight: 700;
  text-align: center;
  color: #fff;
}
.channle-cover-btn {
  flex: 0 1 auto;
  text-align: center;
  margin-top: 26px;
}

/* Channle contnet */
.game-list {
  margin-top: 5px;
  padding: 0 5px;
  columns: 2;
  column-gap: 5px;
  margin-bottom: 190px;
}

.game-list-item {
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  transition: 0.3s;
  border-radius: 4px;
  background: #fff;
  margin-bottom: 5px;
  overflow: hidden;
  padding-bottom: 10px;
  break-inside: avoid;
}
.game-list-item img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}
.game-list-item .content-title {
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  line-height: 1rem;
  color: #0d0d0d;
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
.infinite-nodata {
  padding: 10px;
  text-align: center;
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
