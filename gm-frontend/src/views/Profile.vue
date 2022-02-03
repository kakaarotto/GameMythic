<template>
  <ion-page class="container">
    <ion-header class="container-header">
      <!-- Avatar and Username -->
      <div class="userAvatar" v-if="userInfo">
        <img :src="userInfo.avatar" alt />
        <ion-text>{{ userInfo.username }}</ion-text>
      </div>

      <!-- User follws and Fans -->
      <div class="container-list">
        <div class="container-list-left">
          <div @click="toFollowing" class="container-list-item">
            <ion-text class="num">{{followingCount}}</ion-text>
            <ion-text>Following</ion-text>
          </div>
          <div @click="toFollowers" class="container-list-item">
            <ion-text class="num">{{followerCount}}</ion-text>
            <ion-text>Followers</ion-text>
          </div>
        </div>

        <!-- Settings-->
        <div class="container-list-right">
          <ion-button shape="round" size="small" fill="outline" id="setting" @click="toSetting">
            <ion-icon :icon="settings"></ion-icon>
          </ion-button>
        </div>
      </div>

      <!-- Category title -->
      <div class="container-title">
        <ion-text
          v-for="(item, index) in titleList"
          :class="title == item.label ? 'contnet-nav' : ''"
          :key="index"
          @click="selectClick(item.label)"
        >{{ item.label }}</ion-text>
      </div>
    </ion-header>

    <ion-content>
      <ion-refresher slot="fixed" @ionRefresh="doRefresher($event)">
        <ion-refresher-content :pulling-icon="syncOutline" refreshing-spinner="crescent"></ion-refresher-content>
      </ion-refresher>

      <!-- List Content -->
      <div v-if="title == 'Published' || title == 'Collection'">
        <div class="content-list">
          <div
            @click="gotoDetail(item.contentId)"
            class="content-item"
            v-for="(item, index) in list"
            :key="index"
          >
            <img :src="item.images[0]" alt />
            <ion-text class="content-title">{{ item.title }}</ion-text>
          </div>
        </div>
      </div>

      <!-- Drafts -->
      <div v-if="title == 'Drafts'">
        <div class="draft-list">
          <div class="draft-itme" v-for="(item, index) in saveList" :key="index">
            <img :src="item.images[0]" alt />
            <div class="darft-cover" @click="jumpPage(item)">
              <ion-icon :icon="fileTrayFullOutline"></ion-icon>
              <ion-text>To be publish</ion-text>
            </div>
            <div class="darft-delete">
              <ion-icon :icon="trash" @click="deleteDraft(index)"></ion-icon>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-h"></div>
    </ion-content>
  </ion-page>
</template>

<script>
import { getContents, collectContents } from '../api/content';
import { getMyFollowCount } from '../api/account';

import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonText,
  IonIcon,
  IonButton,
  IonRefresher,
  IonRefresherContent,
  alertController
} from '@ionic/vue';
import { settings, fileTrayFullOutline, trash, syncOutline } from 'ionicons/icons';
export default {
  name: 'Profile',
  components: {
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonText,
    IonIcon,
    IonButton,
    IonRefresher,
    IonRefresherContent
  },
  setup() {
    return {
      settings,
      fileTrayFullOutline,
      trash,
      syncOutline
    };
  },
  data() {
    return {
      userInfo: null,
      saveList: [],
      list: [],
      title: 'Published',
      titleList: [
        {
          label: 'Published'
        },
        {
          label: 'Collection'
        },
        {
          label: 'Drafts'
        }
      ],
      saveKey: '',
      followingCount: 0,
      followerCount: 0
    };
  },
  mounted() {
    if (this.$route.query.title) {
      this.title = this.$route.query.title;
    }
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    // Distinguish the Key saved in the user's draft box
    this.saveKey = 'list' + this.userInfo.userId;
    this.getContents();
    this.getMyFollowCount();
  },
  beforeDestroy() {
    this.$mybus.off('tabRriggerReload');
  },
  created() {
    // Receive tab click refresh events
    this.$mybus.on('tabRriggerReload', (data) => {
      this.getContents();
    });
  },
  methods: {
    getMyFollowCount() {
      getMyFollowCount().then((res) => {
        if (res.data.code == 1) {
          this.followingCount = res.data.content.followingCount;
          this.followerCount = res.data.content.followerCount;
        }
      });
    },
    doRefresher(e) {
      console.log('Begin async operation');
      this.getContents();
      setTimeout(() => {
        console.log('Async operation has ended');
        e.target.complete();
      }, 1000);
    },
    // Go to content detail
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
    toSetting(contentId) {
      this.$router.push({
        name: 'setting',
        params: {
          routeMode: 'push'
        }
      });
    },
    toFollowing() {
      this.$router.push({
        name: 'following&follower',
        params: {
          routeMode: 'push'
        },
        query: {
          name: 'following'
        }
      });
    },
    toFollowers() {
      this.$router.push({
        name: 'following&follower',
        params: {
          routeMode: 'push'
        },
        query: {
          name: 'followers'
        }
      });
    },
    // Get Content list
    getContents() {
      let params = {
        size: 1000,
        page: 0
      };
      // Get user published content
      if (this.title == 'Published') {
        params.collect = false;
        params.userId = JSON.parse(localStorage.getItem('userInfo')).userId;
        getContents(params).then((res) => {
          if (res.data.code == 1) {
            let data = res.data.content;
            for (let item of data) {
              item.images = item.images.split(',');
            }
            this.list = data;
          } else {
            this.list = [];
          }
        });
        // Get user collect content
      } else if (this.title == 'Collection') {
        collectContents().then((res) => {
          if (res.data.code == 1) {
            let data = res.data.content;
            for (let item of data) {
              item.images = item.images.split(',');
            }
            this.list = data;
          } else {
            this.list = [];
          }
        });
        return;
        // Get user save draft content
      } else if (this.title == 'Drafts') {
        let dataJson = localStorage.getItem(this.saveKey);
        if (dataJson) {
          let data = JSON.parse(dataJson);
          for (let item of data) {
            item.images = item.images.split(',');
          }
          this.saveList = data;
        } else {
          this.saveList = [];
        }
      }
    },
    // Top nav
    selectClick(title) {
      this.title = title;
      this.getContents();
    },
    // Go to edit content page
    jumpPage(item) {
      this.$router.push('/publish?id=' + item.id);
    },
    // Delete Draft content
    async deleteDraft(index) {
      const alert = await alertController.create({
        cssClass: 'my-custom-class',
        header: 'Are you sure you want to delete this draft?',
        buttons: [
          {
            text: 'Cancel',
            role: 'cancel',
            handler: () => {
              console.log('Cancel clicked');
            }
          },
          {
            text: 'Yes',
            role: 'Yes',
            handler: () => {
              this.saveList.splice(index, 1);

              let dataJson = localStorage.getItem(this.saveKey);
              let data = JSON.parse(dataJson);
              data.splice(index, 1);
              localStorage.setItem(this.saveKey, JSON.stringify(data));
            }
          }
        ]
      });
      return alert.present();
    }
  }
};
</script>

<style scoped>
.container-header {
  padding-top: 50px;
  height: 320px;
  background: url('https://i.ibb.co/HtNVgSB/IMG-3493.jpg') no-repeat;
  position: relative;
  /* background-size:100%; */
}
ion-content {
  --background: #f2f2f2;
}

.userAvatar {
  padding: 0 20px;
  display: flex;
  align-items: center;
  color: #fff;
  height: 130px;
}
.userAvatar img {
  border-radius: 50%;
  width: 65px;
  height: 65px;
  margin-right: 15px;
  object-fit: cover;
}

.userAvatar ion-text {
  font-size: 1.2rem;
  font-weight: 600;
}

/* User setting */
.container-list {
  height: 50px;
  width: 100%;
  display: flex;
  justify-content: space-between;
  position: absolute;
  bottom: 55px;
  padding: 0 20px;
}
.container-list-left {
  display: flex;
  align-items: center;
  padding-bottom: 6px;
}
.container-list-right {
  display: flex;
  align-items: center;
}
.container-list-item ion-text {
  display: flex;
  justify-content: center;
  font-size: 0.85rem;
  color: #fff;
  margin-right: 15px;
}
.num {
  font-weight: 600;
}

.container-list-right ion-icon {
  color: #fff;
}

#setting {
  margin-left: 10px;
  width: 71px;
  --color: #fff;
  --border-color: #fff;
  --background: none;
}

/* Content category title */
.container-title {
  display: flex;
  justify-content: space-evenly;
  border-radius: 20px 20px 0 0;
  width: 100%;
  height: 50px;
  background: #fff;
  position: absolute;
  bottom: 0;
  border-bottom: 1px solid #f2f2f2;
  align-items: center;
}
.container-title ion-text {
  font-size: 0.95rem;
  color: #666;
}
.contnet-nav {
  color: #ff9820 !important;
}

/* Published content and Collections */
.content-list {
  margin-top: 5px;
  padding: 0 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.content-item {
  border-radius: 4px;
  background: #fff;
  margin-bottom: 5px;
  padding-bottom: 8px;
  width: calc(50% - 2px);
  overflow: hidden;
}
.content-item img {
  width: 100%;
  height: 180px;
  object-fit: cover;
}
.content-title {
  display: block;
  margin: 5px 0;
  padding: 0 10px;
  font-size: 0.87rem;
  font-weight: 450;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

/* Drafts */
.draft-list {
  margin-top: 5px;
  padding: 0 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.draft-itme {
  margin-bottom: 5px;
  width: calc(50% - 2px);
  position: relative;
}

.draft-itme img {
  border-radius: 4px;
  width: 100%;
  height: 180px;
  vertical-align: middle;
  object-fit: cover;
}

.darft-cover {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.9rem;
  font-weight: 600;
  color: #fff;
  list-style: none;
  position: absolute;
  top: 0;
  bottom: 0;
  width: 100%;
  background: rgba(0, 0, 0, 0.4);
  border-radius: 4px;
}

.darft-cover ion-icon {
  position: absolute;
  top: 46px;
  font-size: 2.2rem;
}
.darft-delete {
  position: absolute;
  bottom: 0;
  right: 0;
}
.darft-delete ion-icon {
  font-size: 1.2rem;
  color: #fff;
}

.bottom-h {
  height: 330px;
}
</style>
