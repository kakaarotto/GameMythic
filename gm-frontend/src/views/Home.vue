<template>
  <ion-page class="container">
    <!-- Includes user avatar, top nav, search -->
    <ion-header class="container-header">
      <!-- user avater -->
      <div class="avatar" v-if="userInfo">
        <img :src="userInfo.avatar" alt />
      </div>
      <!-- Top nav title -->
      <div class="title">
        <ion-text
          :class="title == item.title ? 'top-nav' : ''"
          v-for="(item, index) in headerTitles"
          :key="index"
          @click="select(item.title)"
        >{{ item.title }}</ion-text>
      </div>
      <!-- Search icon -->
      <ion-icon :icon="searchOutline" @click="toSearch"></ion-icon>
    </ion-header>

    <!-- Game part category -->
    <ion-header class="game-category" v-if="title == 'Game'">
      <ion-text
        v-for="(item, index) in categories"
        :key="index"
        :class="
          item.ifClick ? 'category top-nav' : 'category'
        "
        @click="selectCategory(index, item.categoryId)"
      >{{ item.name }}</ion-text>
    </ion-header>

    <ion-content :scroll-events="true" @ionScroll="logScrolling($event)" ref="ionContent">
      <ion-refresher slot="fixed" @ionRefresh="doRefresher($event)">
        <ion-refresher-content :pulling-icon="syncOutline" refreshing-spinner="crescent"></ion-refresher-content>
      </ion-refresher>

      <!-- Game part list -->
      <div class="game" v-if="title == 'Game'">
        <div class="game-list" ref="gamelist">
          <div class="game-list-item-left">
            <div
              class="game-list-item"
              v-for="(item, index) in listLeft"
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
          <div class="game-list-item-right">
            <div
              class="game-list-item"
              v-for="(item, index) in listRight"
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
      </div>

      <!-- Follow part list -->
      <div class="follow-list" v-if="title == 'Follow'">
        <div
          @click="gotoDetail(item.contentId)"
          class="follow-item"
          v-for="(item, index) in listFollow"
          :key="index"
        >
          <ion-header class="follow-list-header">
            <img :src="item.user.avatar" alt />
            <ion-text class="name">{{ item.user.username }}</ion-text>
            <ion-text class="time">{{ item.addTime.slice(0, 10) }}</ion-text>
          </ion-header>
          <ion-slides class="follow-item-slides">
            <ion-slide v-for="(citem, cindex) in item.images" :key="cindex">
              <img :src="citem" alt />
            </ion-slide>
          </ion-slides>
          <div class="title">
            <ion-text>{{ item.title }}</ion-text>
          </div>
        </div>
      </div>

      <!-- Load more content -->
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
import { searchOutline, thumbsUpOutline, syncOutline } from 'ionicons/icons';
import { getContents, getNexPageContents } from '../api/content';
import { getCategories } from '../api/category';
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonText,
  IonContent,
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
  name: 'Home',
  components: {
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonText,
    IonIcon,
    IonList,
    IonSlides,
    IonSlide,
    IonRefresher,
    IonRefresherContent,
    IonInfiniteScroll,
    IonInfiniteScrollContent
  },
  setup() {
    return {
      searchOutline,
      thumbsUpOutline,
      syncOutline
    };
  },
  data() {
    return {
      categories: [],
      userInfo: null,
      list: [],
      listLeft: [],
      listRight: [],
      listFollow: [],
      title: 'Game',
      categoryId: '',
      headerTitles: [
        {
          title: 'Game'
        },
        {
          title: 'Follow'
        }
      ],
      isInfiniteDisabled: false,
      currentParams: {
        pageSize: 8,
        page: 0,
        collect: false,
        categoryId: '',
        excludeIds: []
      },
      scrollToTop_val: ''
    };
  },
  mounted() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    this.getCategories();
  },
  beforeDestroy() {
    this.$mybus.off('tabRriggerReload');
  },
  created() {
    // Receive tab click refresh events
    this.$mybus.on('tabRriggerReload', (data) => {
      // Top scrolling
      this.getContentScrolling();
    });
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
    doNextPage(e) {
      console.log('Begin Load data');
      this.getNexPageContents(() => {
        console.log('Loaded data');
        e.target.complete();
      });
    },
    toSearch() {
      this.$router.push({
        name: 'searchPage'
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
    getCategories() {
      let params = {
        size: 1000,
        page: 0
      };
      getCategories(params).then((res) => {
        if (res.data.code == 1) {
          let data = res.data.content;
          for (let item of data) {
            item.ifClick = false;
          }
          if (data.length > 0) {
            data[0].ifClick = true;
            this.categoryId = data[0].categoryId;
          }
          this.categories = data;
        } else {
          this.categories = [];
          this.categoryId = '';
        }
        this.getContents();
      });
    },
    fillDataList() {
      this.listLeft = [];
      this.listRight = [];
      for (let i = 0; i < this.list.length; i++) {
        if (i % 2 == 0) {
          this.listLeft.push(this.list[i]);
        } else {
          this.listRight.push(this.list[i]);
        }
      }
    },
    getContents(callback) {
      this.isInfiniteDisabled = false;
      if (this.title == 'Follow') {
        this.listFollow = [];
        this.currentParams.page = 0;
        this.currentParams.categoryId = '';
        this.currentParams.collect = true;
        getContents(this.currentParams).then((res) => {
          if (typeof callback === 'function') {
            callback();
          }
          if (res.data.code == 1) {
            let data = res.data.content;
            for (let item of data) {
              item.images = item.images.split(',');
            }
            this.listFollow = data;
          } else {
            this.listFollow = [];
          }
        });
      } else {
        this.list = [];
        this.listLeft = [];
        this.listRight = [];
        this.currentParams.excludeIds = [];
        this.currentParams.categoryId = this.categoryId;
        this.currentParams.collect = false;

        getNexPageContents(this.currentParams).then((res) => {
          if (typeof callback === 'function') {
            callback();
          }
          if (res.data.code == 1) {
            let data = res.data.content;
            for (let item of data) {
              item.images = item.images.split(',');
            }
            this.list = data;
            this.fillDataList();
          } else {
            this.list = [];
          }
        });
      }
    },
    getNexPageContents(callback) {
      if (this.title == 'Follow') {
        this.currentParams.page++;
        this.currentParams.categoryId = '';
        this.currentParams.collect = true;
        // this.currentParams.userId = this.userInfo.userId;
        getContents(this.currentParams).then((res) => {
          if (typeof callback === 'function') {
            callback();
          }
          if (res.data.code == 1) {
            let data = res.data.content;
            for (let item of data) {
              item.images = item.images.split(',');
              this.listFollow.push(item);
            }
          } else {
            this.isInfiniteDisabled = true;
          }
        });
      } else {
        this.currentParams.categoryId = this.categoryId;
        this.currentParams.collect = false;

        // exclude Ids
        this.currentParams.excludeIds = [];
        for (let item of this.list) {
          this.currentParams.excludeIds.push(item.contentId);
        }

        getNexPageContents(this.currentParams).then((res) => {
          if (typeof callback === 'function') {
            callback();
          }
          if (res.data.code == 1) {
            let data = res.data.content;
            for (let item of data) {
              item.images = item.images.split(',');
              this.list.push(item);
            }
            this.fillDataList();
          } else {
            this.isInfiniteDisabled = true;
          }
        });
      }
    },
    selectCategory(idx, categoryId) {
      for (let item of this.categories) {
        item.ifClick = false;
      }
      this.categories[idx].ifClick = true;
      this.categoryId = categoryId;
      this.getContents();
    },
    select(title) {
      this.title = title;
      this.getContents();
    },

    // Scroll back up to the Refresh screen
    logScrolling(event) {
      this.scrollToTop_val = event.detail.scrollTop;
    },
    getContentScrolling() {
      document.querySelector('ion-content').scrollToTop(800);
      let time = setTimeout(() => {
        this.getContents();
        clearTimeout(time);
      }, 600);
    }
  }
};
</script>

<style scoped>
@import url('../common/svg/style.css');
.container {
  position: relative;
  padding-top: 90px;
}
ion-refresher {
  margin-top: 35px;
  z-index: 1;
}
.container-header {
  background: #fff;
  width: 100%;
  height: 90px;
  border-bottom: 1px solid #f2f2f2;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 10px;
  z-index: 999;
  padding-top: 43px;
}
.avatar img {
  border-radius: 50%;
  width: 35px;
  height: 35px;
  object-fit: cover;
}
.container-header .title {
  width: 40%;
  display: flex;
  justify-content: space-between;
  font-size: 1.08rem;
  padding-bottom: 2px;
}
.container-header ion-icon {
  font-size: 1.6rem;
}
.top-nav {
  color: #ff9820;
}

.game-category {
  padding: 0 20px;
  position: fixed;
  top: 90px;
  left: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
  height: 40px;
  background: #fff;
  border-bottom: 1px solid #f4f4f4;
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
}

/* Game part style */
.game {
  padding-top: 40px;
}
.category {
  font-size: 0.88rem;
}
/* Waterfall scrolling style */
.game-list {
  margin-top: 5px;
  padding: 0 5px;
  margin-bottom: 65px;
  columns: 2;
  column-gap: 5px;
}
.game-list-item {
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  transition: 0.3s;
  border-radius: 4px;
  background: #fff;
  overflow: hidden;
  padding-bottom: 10px;
  margin-bottom: 5px;
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
  overflow: hidden;
}
.game-list-item .icon {
  display: flex;
  padding: 2px 8px 0 0;
  float: right;
  color: #999;
  align-items: center;
}
.game-list-item .number {
  margin: 0 5px;
  font-size: 0.9rem;
  color: #999;
}

/* Follow part style */
.follow-item {
  padding-bottom: 10px;
  margin-bottom: 24px;
  border-bottom: 1px solid #f2f2f2;
}

.follow-list-header {
  height: 50px;
  display: flex;
  align-items: center;
  padding: 0 10px;
}
.follow-list-header img {
  border-radius: 50%;
  width: 32px;
  height: 32px;
  object-fit: cover;
}
.follow-list-header .name {
  font-size: 0.95rem;
  margin: 0 10px;
}
.follow-list-header .time {
  font-size: 0.7rem;
  color: #999;
}
.follow-item .title {
  margin: 12px 20px;
  font-size: 1rem;
  font-weight: 450;
}
</style>
