<template>
  <ion-page class="container router-view" v-if="detail">
    <ion-header class="container-header">
      <div class="user-header">
        <GoBack :routeName="routeName" v-if="routeQuery" :param="routeQuery" />
        <GoBack :routeName="routeName" v-else />
        <img :src="detail.user.avatar" alt class="avatar" />
        <ion-text class="username">{{ detail.user.username }}</ion-text>
      </div>

      <div
        v-if="!isShowMore"
        :class="detail.collectUser?'inactive activated':'inactive'"
        @click="follow"
      >{{followText}}</div>

      <!-- user self posts content to show editors -->
      <ion-icon
        :icon="ellipsisHorizontal"
        v-if="isShowMore"
        @click="presentActionSheet"
        id="option"
      ></ion-icon>
    </ion-header>

    <!-- Main Contnet -->
    <ion-content :fullscreen="false">
      <div>
        <ion-slides pager="true">
          <ion-slide v-for="(item, index) in detail.images" :key="index">
            <img :src="item" alt />
          </ion-slide>
        </ion-slides>
        <div class="title">
          <ion-text>{{ detail.title }}</ion-text>
        </div>
        <div class="content">
          <ion-text>{{ detail.content }}</ion-text>
        </div>
        <div class="date">
          <ion-text class="time">Published in {{ detail.addTime.slice(0, 10) }}</ion-text>
        </div>
      </div>

      <!-- User Comment -->
      <ion-list class="comment">
        <template v-for="(item) in detail.commentDTOS" :key="item.id">
          <ion-item>
            <ion-avatar slot="start">
              <img :src="item.user.avatar" />
            </ion-avatar>
            <ion-label>
              <p>{{ item.user.username }}</p>
              <div class="comment-pos">
                <h3 id="comment-content">{{ item.content }}</h3>
                <p id="comment-time">{{ addTimeShow(item.addTime) }}</p>
              </div>
            </ion-label>
          </ion-item>
        </template>
      </ion-list>

      <!-- Footer -->
      <ion-footer class="footer">
        <ion-button shape="round" color="light" @click="display = true">Say something...</ion-button>
        <!-- Like-->
        <div class="item" style="margin-left:10px;">
          <ion-text>Like</ion-text>
          <div @click="like">
            <i class="ico icon-like-block" v-if="collectActives > 0"></i>
            <i class="ico icon-like" v-else></i>
          </div>
          <ion-text v-if="goodCount > 0">{{goodCount}}</ion-text>
          <ion-text v-else>0</ion-text>
        </div>

        <!-- Collect -->
        <div class="item">
          <ion-text>Collect</ion-text>
          <div @click="collect">
            <i class="ico icon-collection-block" v-if="collectCollectedActives > 0"></i>
            <i class="ico icon-collection" v-else></i>
          </div>

          <ion-text v-if="collectCount > 0">{{collectCount}}</ion-text>
          <ion-text v-else>0</ion-text>
        </div>
      </ion-footer>
    </ion-content>
  </ion-page>
  <Comment :display="display"></Comment>
</template>

<script>
import {
  getContent,
  good,
  collect,
  deleteContent,
  collectUser,
  unCollectUser
} from '../api/content';
import GoBack from '@/components/GoBack.vue';
import Comment from '@/components/DetailsComment.vue';
import { ellipsisHorizontal } from 'ionicons/icons';
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonText,
  IonIcon,
  IonAvatar,
  IonSlides,
  IonSlide,
  IonButton,
  IonList,
  IonItem,
  IonLabel,
  actionSheetController,
  alertController
} from '@ionic/vue';

export default {
  name: 'Detail',
  components: {
    GoBack,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonText,
    IonIcon,
    IonAvatar,
    IonSlides,
    IonSlide,
    IonButton,
    IonList,
    IonItem,
    IonLabel,
    Comment
  },
  setup() {
    return {
      ellipsisHorizontal
    };
  },
  data() {
    return {
      userInfo: null,
      detail: null,
      collectLiked: false,
      collectCollected: false,
      // default like btn off
      collectActives: 0,
      // default collect btn off
      collectCollectedActives: 0,
      contentId: null,
      goodCount: 0,
      collectCount: 0,
      routeName: '',
      routeQuery: '',
      // Show editable icons if it’s posted by user
      isShowMore: false,
      followText: 'Follow',
      display: false
    };
  },
  beforeDestroy() {
    this.$mybus.off('detailBus');
  },
  created() {
    this.$mybus.on('detailBus', (data) => {
      this.getContent();
      this.display = data;
    });
  },
  // Get routing hook
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.routeName = from.name;
      if (from.query.id != undefined) {
        vm.routeQuery = from.query;
      }
    });
  },
  mounted() {
    if (this.$route.query.contentId) {
      this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
      this.contentId = this.$route.query.contentId;
      this.getContent();
    }
  },
  methods: {
    addTimeShow(time) {
      // Only the date is displayed
      if (time) {
        let times = /\d{4}-\d{1,2}-\d{1,2}/g.exec(time);
        if (times.length > 0) {
          return times[0];
        }
      }
      return '';
    },
    // Follow user
    async follow() {
      // If the user is already following, invoke the unfollow api show alert before unfollowing
      if (this.detail.collectUser) {
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
                if (this.detail.collectUser) {
                  unCollectUser(this.detail.user.userId).then((res) => {
                    if (res.data.code == 1) {
                      this.followText = 'Follow';
                      this.$common.hideLoading();
                      this.detail.collectUser = !this.detail.collectUser;
                    }
                  });
                }
              }
            }
          ]
        });
        return alert.present();
      } else {
        // Following other user
        collectUser(this.detail.user.userId).then((res) => {
          if (res.data.code == 1) {
            this.followText = 'Following';
            this.$common.hideLoading();
            this.detail.collectUser = !this.detail.collectUser;
          }
        });
      }
    },
    // Get content detail
    getContent() {
      let _this = this;
      getContent(this.contentId).then((res) => {
        if (res.data.code == 1) {
          res.data.content.images = res.data.content.images.split(',');
          this.detail = res.data.content;

          // First entry to determine if content has been liked
          if (this.detail.collectLiked == true) {
            this.collectLiked = true;
            this.collectActives = 1;
          } else {
            this.collectLiked = false;
            this.collectActives = 0;
          }
          // First entry to determine if content has been collected
          if (this.detail.collectCollected == true) {
            this.collectCollected = true;
            this.collectCollectedActives = 1;
          } else {
            this.collectCollected = false;
            this.collectCollectedActives = 0;
          }
          this.goodCount = this.detail.goodCount;
          this.collectCount = this.detail.collectCount;
          // Like the style change
          this.addAnimate = this.detail.goodCount > 0 ? '' : '';
          // Collect style change
          if (res.data.content.user.username == this.userInfo.username) {
            this.isShowMore = true;
          }
          // If you are already following this user, show following
          if (this.detail.collectUser) {
            this.followText = 'Following';
          }
        }
      });
    },
    // Collect contnet
    collect() {
      collect(this.contentId).then((res) => {
        if (res.data.code == 1) {
          // collect button style change
          this.collectCollected = !this.collectCollected; 
          if (this.collectCollected == true) {
            // Collected
            this.collectCollectedActives = 1;
          } else {
            // Uncollect
            this.collectCollectedActives = 0;
          }
          this.goodCount = res.data.content.goodCount;
          this.collectCount = res.data.content.collectCount;
        }
      });
    },
    // Like content
    like() {
      good(this.contentId).then((res) => {
        if (res.data.code == 1) {
          // like button style change
          this.collectLiked = !this.collectLiked;
          if (this.collectLiked == true) {
            this.collectActives = 1;
          } else {
            this.collectActives = 0;
          }
          this.goodCount = res.data.content.goodCount;
          this.collectCount = res.data.content.collectCount;
        }
      });
    },
    // Delet contnet
    delete() {
      deleteContent(this.contentId).then((res) => {
        if (res.data.code == 1) {
          this.$router.go(-1);
        }
      });
    },
    // Edit contnet
    gotoPublish() {
      this.$router.push('/publish?contentId=' + this.contentId);
    },
    // Edit contnet action sheet
    async presentActionSheet() {
      const actionSheet = await actionSheetController.create({
        header: 'Edit your content',
        cssClass: 'my-custom-class',
        buttons: [
          {
            text: 'Delete',
            role: 'destructive',
            handler: () => {
              this.delete();
            }
          },
          {
            text: 'Edit',
            handler: () => {
              this.gotoPublish();
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
      await actionSheet.present();
    }
  }
};
</script>

<style scoped type="text/css">
@import url('../common/svg/style.css');
.container {
  padding-top: 90px;
}
.container-header {
  background: #fff;
  width: 100%;
  height: 90px;
  position: fixed;
  top: 0;
  left: 0;
  display: flex;
  align-items: center;
  z-index: 100;
  padding-top: 40px;
}

.user-header {
  display: flex;
  align-items: center;
}

.container-header .avatar {
  border-radius: 50%;
  width: 35px;
  height: 35px;
  margin: 0 15px;
}

.container-header .username {
  font-size: 1rem;
  margin-right: 15px;
}

#option {
  position: absolute;
  font-size: 1.3rem;
  left: 336px;
}

/* Content cover */
ion-slide {
  height: 100%;
  width: 100%;
}

/* Content text */
.title {
  margin: 16px 15px 6px 15px;
  font-size: 1.1rem;
  font-weight: 550;
}
.content {
  margin: 0 15px;
  text-align: left;
  font-size: 0.9rem;
  line-height: 1.5rem;
  color: #000;
  padding-bottom: 50px;
}

.date {
  color: rgb(151, 151, 151);
  font-size: 0.8rem;
  padding: 0 0 15px 15px;
  border-bottom: 1px solid #f2f2f2;
}

/* Comment */
.comment-pos {
  display: flex;
  justify-content: space-between;
}
.comment {
  margin: 0 0 100px -7px;
}
.comment ion-avatar {
  width: 35px;
  height: 35px;
}

#comment-time {
  font-size: 0.7rem;
}

ion-avatar {
  margin-inline: unset;
  margin-right: 12px;
}

/* footer */
.footer {
  width: 100%;
  padding: 5px 10px;
  padding-bottom: 40px;
  display: flex;
  position: fixed;
  bottom: 0;
  align-items: center;
  justify-content: space-between;
  background: #fff;
  border-top: 1px solid #f2f2f2;
  z-index: 100;
}
/* Comment btn */
.footer ion-button {
  width: 35%;
  height: 32px;
  font-size: 0.8rem;
}
/* Icon */
.footer .item {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}
.footer .item ion-text:last-child {
  font-size: 0.9rem;
  width: 39px;
}

/* Follow btn */
.inactive {
  position: absolute;
  width: 70px;
  left: 286px;
  padding: 6px 1px;
}
.activated {
  padding: 6px 1px;
  font-size: 0.8rem;
  line-height: 0.8125rem;
}
</style>
<style type="text/css">
.something input.native-input {
  color: #000;
  border-radius: 50px;
  background-color: #d5d5d5;
  text-align: center;
  width: 100%;
  margin-right: 10px;
}
</style>