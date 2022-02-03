<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>All Channles</ion-title>
      <GoBack :routeName="routeName" />
      <ion-searchbar
        placeholder="Search for your favourite channel"
        v-model="keyword"
        @ionChange="onSearchChange"
        @ionClear="onSearchClear"
      ></ion-searchbar>
    </ion-header>

    <ion-content>
      <!-- Side nav -->
      <div class="all-channels">
        <div class="category-side">
          <div
            class="category-item"
            :class="item.checked ? 'category-item-active' : ''"
            v-for="(item, index) in categorys"
            :key="item.channelsCategoryId"
            @click="onCategoryClick(item, index)"
          >
            <ion-text>{{ item.name }}</ion-text>
          </div>
        </div>

        <!-- Channel List -->
        <ion-list class="channel-list">
          <ion-item v-for="(item, index) in channels" :key="item.id">
            <ion-thumbnail slot="start" @click="onChannelClick(item, index)">
              <img :src="item.logo" />
            </ion-thumbnail>
            <ion-label @click="onChannelClick(item, index)">{{ item.name }}</ion-label>
            <ion-button
              size="small"
              :fill="item.subscription ? 'solid' : 'outline'"
              :color="item.subscription ? 'medium' : 'warning'"
              @click="onChangeChannelsSubscription(item, index)"
            >{{ item.subscription ? 'Subscribed' : 'Subscribe'}}</ion-button>
          </ion-item>
        </ion-list>
      </div>
      <div class="bottom-h"></div>
    </ion-content>
  </ion-page>
</template>

<script>
import {
  getChannels,
  getChannelsCategories,
  addChannelsSubscription,
  deleteChannelsSubscription
} from '../api/channel';
import GoBack from '@/components/GoBack.vue';

import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonText,
  IonGrid,
  IonRow,
  IonList,
  IonLabel,
  IonButton,
  IonItem,
  IonThumbnail,
  IonCol,
  IonSearchbar
} from '@ionic/vue';

export default {
  name: 'AllChannels',
  components: {
    GoBack,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonText,
    IonGrid,
    IonRow,
    IonList,
    IonLabel,
    IonButton,
    IonItem,
    IonThumbnail,
    IonCol,
    IonSearchbar
  },
  data() {
    return {
      channels: [],
      categorys: [],
      currentChannelsCategoryId: 0,
      channelsCache: [],
      keyword: '',
      searchRuning: false,
      routeName: ''
    };
  },
  mounted() {
    this.getChannelsCategories();
    this.getChannels();
  },
  // Get routing hook function
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.routeName = 'channels';
    });
  },
  methods: {
    // Get channels list
    getChannels() {
      let params = {
        size: 1000,
        page: 0
      };
      if (this.currentChannelsCategoryId > 0) {
        params.channelsCategoryId = this.currentChannelsCategoryId;
      }
      this.channels = [];
      this.channelsCache = [];
      // this.$common.showLoading();
      getChannels(params)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.channels = res.data.content;
            this.channelsCache = res.data.content;
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    getChannelsCategories() {
      let params = {
        size: 1000,
        page: 0
      };
      let categorys = [];
      getChannelsCategories(params).then((res) => {
        if (res.data.code == 1) {
          res.data.content.map((cat) => {
            cat.checked = false;
            categorys.push(cat);
          });
        } else {
          categorys = [];
        }
        categorys.unshift({
          channelsCategoryId: 0,
          name: 'All',
          checked: true
        });
        this.categorys = categorys;
      });
    },
    onCategoryClick(item, index) {
      this.categorys.map((cat) => {
        cat.checked = false;
      });
      this.categorys[index].checked = true;
      this.currentChannelsCategoryId = this.categorys[index].channelsCategoryId;
      this.getChannels();
    },
    // Go to channel details page
    onChannelClick(item, index) {
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
    addChannelsSubscription(item) {
      // this.$common.showLoading();
      addChannelsSubscription(item.channelsId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.$common.toast('Subscription Success');
            item.subscription = true;
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    deleteChannelsSubscription(item) {
      // this.$common.showLoading();
      deleteChannelsSubscription(item.channelsId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            // this.$common.toast('remove subscription success');
            item.subscription = false;
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    // Search for channles
    onSearchChange() {
      if (!this.searchRuning && this.keyword) {
        this.searchRuning = true;
        let searchResult = [];
        this.channelsCache.map((cha) => {
          if (cha.name.toLowerCase().indexOf(this.keyword.toLowerCase()) > -1) {
            searchResult.push(cha);
          }
        });
        this.channels = searchResult;

        this.searchRuning = false;
      }
    },
    onSearchClear() {
      this.channels = this.channelsCache;
    }
  }
};
</script>

<style scoped>
.container {
  padding-top: 15%;
}
ion-title {
  padding-bottom: 60px;
}
ion-grid {
  --ion-grid-padding: 5px 5px 5px 0;
}
/* Side nav */
.all-channels {
  position: relative;
}
.category-side {
  left: 0px;
  position: fixed;
  width: 95px;
  height: 100%;
  background-color: #f4f4f4;
  --ion-grid-column-padding: 0;
}
.category-item {
  height: 60px;
  line-height: 60px;
  font-size: 0.8rem;
  font-weight: 450;
  text-align: center;
}
.category-item-active {
  background-color: #fff;
}

/* Channle list */
.channel-list {
  margin-left: 100px;
}
.channel-list ion-item {
  padding-bottom: 2px;
}
.channel-list ion-thumbnail {
  --border-radius: 4px;
  width: 52px;
  height: 52px;
  margin: 0 6px -22px -18px;
}
.channel-list ion-label {
  padding-top: 18px;
  font-size: 0.8rem;
  line-height: 1rem;
  height: 52px;
  flex: none;
  width: calc(100% / 1.8);
  word-break: break-all;
}
.channel-list ion-button {
  font-size: 0.7rem;
  position: absolute;
  right: 0;
  padding-right: 12px;
  --background-activated: none;
}
</style>

<style type="text/css">
/* .item .sc-ion-label-ios-h {
  text-overflow: unset;
  white-space: unset;
} */
</style>
