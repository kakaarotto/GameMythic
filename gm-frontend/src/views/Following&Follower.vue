<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>Gamers</ion-title>
      <GoBack :routeName="routeName" />
    </ion-header>
    <ion-header class="container-header">
      <div class="title">
        <ion-text
          :class="title == item.title ? 'top-nav' : ''"
          v-for="(item, index) in headerTitles"
          :key="index"
          @click="select(item.title, index)"
        >{{ item.title }}</ion-text>
      </div>
    </ion-header>

    <ion-content>
      <!-- All follow list -->
      <ion-list class="follow-list">
        <ion-item v-for="(item, index) in datalist" :key="index">
          <ion-avatar slot="start">
            <img :src="item.avatar" />
          </ion-avatar>

          <ion-label>
            <h2>{{item.username}}</h2>
          </ion-label>

          <!-- Follwing state -->
          <ion-button
            v-if="item.following"
            shape="round"
            size="small"
            fill="solid"
            color="medium"
            class="follwing-state"
            @click="onRemoveFollow(item, index)"
          >Following</ion-button>

          <!-- Follow state -->
          <ion-button
            v-if="!item.following"
            shape="round"
            size="small"
            fill="outline"
            class="follow-state"
            @click="onAddFollow(item, index)"
          >Follow</ion-button>
        </ion-item>
      </ion-list>
      <div class="bottom-h"></div>
    </ion-content>
  </ion-page>
</template>

<script>
import GoBack from '@/components/GoBack.vue';
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
  IonButton,
  IonAvatar,
  IonThumbnail,
  alertController
} from '@ionic/vue';

import { getMyFollowing, getMyFollowers } from '../api/account';
import { collectUser, unCollectUser } from '../api/content';

export default {
  name: 'Follower&Follower',
  components: {
    GoBack,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonText,
    IonLabel,
    IonItem,
    IonList,
    IonIcon,
    IonButton,
    IonAvatar,
    IonThumbnail
  },
  data() {
    return {
      title: this.firstToUpper(this.$route.query.name),
      titleIndex: 0,
      headerTitles: [
        {
          title: 'Following',
          count: ''
        },
        {
          title: 'Followers',
          count: ''
        }
      ],
      datalist: [],
      routeName: ''
    };
  },
  created() {
    this.getData();
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.routeName = from.name;
    });
  },
  methods: {
    select(title, index) {
      this.titleIndex = index;
      this.title = title;
      this.getData();
    },
    // Username uppercase
    firstToUpper(str) {
      return str.replace(/\b(\w)(\w*)/g, function ($0, $1, $2) {
        return $1.toUpperCase() + $2.toLowerCase();
      });
    },
    // Get following and followers
    getData() {
      this.datalist = [];
      if (this.title == 'Following') {
        getMyFollowing().then((res) => {
          if (res.data.code == 1) {
            this.datalist = res.data.content;
            this.headerTitles[this.titleIndex].count = res.data.content.length;
          }
        });
      }
      if (this.title == 'Followers') {
        getMyFollowers().then((res) => {
          if (res.data.code == 1) {
            this.datalist = res.data.content;
            this.headerTitles[this.titleIndex].count = res.data.content.length;
          }
        });
      }
    },
    // Unfollow user
    async onRemoveFollow(item, index) {
      // this.$common.showLoading();
      const alert = await alertController.create({
        cssClass: 'my-custom-class',
        header: 'Unfollow this user',
        buttons: [
          {
            text: 'Cancel',
            role: 'cancel',
            cssClass: 'secondary',
            handler: (blah) => {
              console.log('Confirm Cancel:', blah);
            }
          },
          {
            text: 'Confirm',
            handler: () => {
              if (item.following) {
                unCollectUser(item.userId)
                  .then((res) => {
                    this.$common.hideLoading();
                    if (res.data.code == 1) {
                      this.datalist.splice(index, 1);
                    }
                  })
                  .catch(() => {
                    this.$common.hideLoading();
                  });
              }
            }
          }
        ]
      });
      return alert.present();
    },
    // Following user
    async onAddFollow(item, index) {
      // this.$common.showLoading();
      if (item.following) {
        const alert = await alertController.create({
          cssClass: 'my-custom-class',
          header: 'Unfollow this user',
          buttons: [
            {
              text: 'Cancel',
              role: 'cancel',
              cssClass: 'secondary',
              handler: (blah) => {
                console.log('Confirm Cancel:', blah);
              }
            },
            {
              text: 'Confirm',
              handler: () => {
                if (item.following) {
                  collectUser(item.userId)
                    .then((res) => {
                      this.$common.hideLoading();
                      if (res.data.code == 1) {
                        item.following = true;
                      }
                    })
                    .catch(() => {
                      this.$common.hideLoading();
                    });
                }
              }
            }
          ]
        });
        return alert.present();
      } else {
        collectUser(item.userId)
          .then((res) => {
            this.$common.hideLoading();
            if (res.data.code == 1) {
              item.following = true;
            }
          })
          .catch(() => {
            this.$common.hideLoading();
          });
      }
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
.container-header {
  background: #fff;
  width: 100%;
  border-bottom: 1px solid #f2f2f2;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 0 52px;
  z-index: 999;
  height: 46px;
}

.container-header .title {
  width: 80%;
  display: flex;
  justify-content: space-between;
  font-size: 0.95rem;
}
.top-nav {
  color: #ff9820;
}

.follow-list {
  margin: 0 0 0 -7px;
}

.follow-list ion-item {
  padding: 10px 0;
}

.follow-list ion-button {
  height: 25px;
  padding-right: 5px;
}

.follow-state {
  width: 70px;
  --color: #ff9820;
  --background: none;
  --border-color: #ff9820;
}
.follwing-state {
  width: 84px;
}

ion-avatar {
  margin-inline: unset;
  margin-right: 12px;
}
</style>