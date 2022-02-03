<template>
  <ion-page class="container">
    <ion-header>
      <ion-toolbar>
        <ion-title>Notifications</ion-title>
      </ion-toolbar>
    </ion-header>

    <ion-content>
      <ion-refresher slot="fixed" @ionRefresh="doRefresher($event)">
        <ion-refresher-content :pulling-icon="syncOutline" refreshing-spinner="crescent"></ion-refresher-content>
      </ion-refresher>

      <div class="topbox">
        <div class="topbox-btn">
          <!-- New Fans -->
          <ion-button id="follow-btn" @click="follower">
            <ion-icon :icon="people" class="msg-btn" id="fans"></ion-icon>
          </ion-button>
          <ion-badge color="warning" v-if="followerCount > 0">{{followerCount}}</ion-badge>
          <div class="btn-txet">Followers</div>
        </div>
        <div class="topbox-btn">
          <!-- Like and Collection -->
          <ion-button id="like-btn" @click="Liked">
            <ion-icon :icon="star" class="msg-btn" id="like"></ion-icon>
          </ion-button>
          <ion-badge color="warning" v-if="likeCount > 0">{{likeCount}}</ion-badge>
          <div class="btn-txet">Liked</div>
        </div>
        <div class="topbox-btn">
          <!-- Comments -->
          <ion-button id="comment-btn" @click="Commented">
            <ion-icon :icon="chatbubbleEllipses" class="msg-btn" id="comment"></ion-icon>
          </ion-button>
          <ion-badge color="warning" v-if="commentCount > 0">{{commentCount}}</ion-badge>
          <div class="btn-txet">Commented</div>
        </div>
      </div>

      <!-- Meesage list -->
      <ion-list>
        <ion-item-sliding v-for="(item, index) in messages" :key="index">
          <ion-item @click="showMessage(item, index)">
            <ion-label>
              <h2>{{item.title}}</h2>
              <p>{{item.content}}</p>
            </ion-label>
            <ion-note slot="end">{{addTimeShow(item.addTime)}}</ion-note>
          </ion-item>

          <ion-item-options side="end">
            <ion-item-option color="danger" @click="onDeleteMessage(item, index)">
              <ion-icon slot="icon-only" :icon="trash"></ion-icon>
              <ion-text>Delete</ion-text>
            </ion-item-option>
          </ion-item-options>
        </ion-item-sliding>
      </ion-list>

      <!-- Load more message -->
      <ion-infinite-scroll
        @ionInfinite="doNextPage($event)"
        threshold="100px"
        id="infinite-scroll"
        :disabled="isInfiniteDisabled"
      >
        <ion-infinite-scroll-content></ion-infinite-scroll-content>
      </ion-infinite-scroll>
    </ion-content>

    <!-- Show message detail -->
    <ion-modal :is-open="isOpen" :swipe-to-close="true" @didDismiss="isOpen = false">
      <ion-page>
        <ion-header translucent>
          <ion-toolbar>
            <ion-title>Message</ion-title>
            <ion-buttons slot="end">
              <ion-button class="modal-btn" @click="isOpen = false">Close</ion-button>
            </ion-buttons>
          </ion-toolbar>
        </ion-header>
        <ion-content>
          <ion-card>
            <ion-card-header>
              <ion-card-subtitle>{{currentMessage.addTime}}</ion-card-subtitle>
              <ion-card-title>{{currentMessage.title}}</ion-card-title>
            </ion-card-header>
            <ion-card-content>{{currentMessage.content}}</ion-card-content>
          </ion-card>
        </ion-content>
      </ion-page>
    </ion-modal>
  </ion-page>
</template>

<script>
import { chatbubbleEllipses, people, star, trash, syncOutline } from 'ionicons/icons';
import {
  IonPage,
  IonHeader,
  IonToolbar,
  IonTitle,
  IonContent,
  IonList,
  IonButton,
  IonLabel,
  IonItem,
  IonIcon,
  IonText,
  IonNote,
  IonCard,
  IonButtons,
  IonBadge,
  IonInfiniteScroll,
  IonInfiniteScrollContent,
  IonRefresher,
  IonRefresherContent,
  IonCardHeader,
  IonCardTitle,
  IonCardSubtitle,
  IonCardContent,
  IonItemSliding,
  IonItemOptions,
  IonItemOption,
  IonModal
} from '@ionic/vue';

import moment from 'moment';
import { getMessages, deleteMessage, getMessageCount } from '../api/message';

export default {
  name: 'Notification',
  components: {
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonList,
    IonButton,
    IonLabel,
    IonItem,
    IonIcon,
    IonText,
    IonNote,
    IonButtons,
    IonBadge,
    IonInfiniteScroll,
    IonInfiniteScrollContent,
    IonRefresher,
    IonRefresherContent,
    IonCard,
    IonCardHeader,
    IonCardTitle,
    IonCardSubtitle,
    IonCardContent,
    IonItemSliding,
    IonItemOptions,
    IonItemOption,
    IonModal
  },
  setup() {
    return {
      chatbubbleEllipses,
      people,
      star,
      trash,
      syncOutline
    };
  },
  data() {
    return {
      messages: [],
      isInfiniteDisabled: false,
      currentParams: {
        size: 10,
        page: 0,
        readState: 0,
        // Only content review message
        type: 4 
      },
      isOpen: false,
      currentMessage: {
        title: '',
        content: ''
      },
      followerCount: 0,
      likeCount: 0,
      commentCount: 0
    };
  },
  beforeDestroy() {
    this.$mybus.off('tabRriggerReload');
    this.$mybus.off('updateMessageCount');
  },
  created() {
    this.getMessageCount();
    // Receive tab click refresh events
    this.$mybus.on('tabRriggerReload', (data) => {
      // reload data
      this.getMessages();
    });
    this.$mybus.on('updateMessageCount', (data) => {
      this.getMessageCount();
    });
  },
  mounted() {
    this.getMessages();
  },
  methods: {
    follower() {
      this.$router.push({
        name: 'notificationDetail',
        query: {
          name: 'newfollwers'
        },
        params: {
          routeMode: 'push'
        }
      });
    },
    Liked() {
      this.$router.push({
        name: 'notificationDetail',
        query: {
          name: 'liked'
        },
        params: {
          routeMode: 'push'
        }
      });
    },
    Commented() {
      this.$router.push({
        name: 'notificationDetail',
        query: {
          name: 'commented'
        },
        params: {
          routeMode: 'push'
        }
      });
    },
    addTimeShow(t) {
      return moment(t).fromNow();
    },
    getMessageCount() {
      getMessageCount().then((res) => {
        if (res.data.code == 1) {
          this.followerCount = res.data.content.followerCount;
          this.likeCount = res.data.content.likeCount;
          this.commentCount = res.data.content.commentCount;
        }
      });
    },
    doRefresher(e) {
      console.log('Begin async operation');
      this.getMessages();
      setTimeout(() => {
        console.log('Async operation has ended');
        e.target.complete();
      }, 500);
    },
    doNextPage(e) {
      console.log('Begin Load data');
      this.getNextMessages();
      setTimeout(() => {
        console.log('Loaded data');
        e.target.complete();
      }, 800);
    },
    getMessages() {
      this.currentParams.page = 0;
      getMessages(this.currentParams).then((res) => {
        if (res.data.code == 1) {
          this.messages = res.data.content;
        } else {
          this.messages = [];
        }
      });
    },
    getNextMessages() {
      this.currentParams.page++;
      getMessages(this.currentParams).then((res) => {
        if (res.data.code == 1) {
          this.messages = this.messages.concat(res.data.content);
        } else {
          this.isInfiniteDisabled = true;
        }
      });
    },
    onDeleteMessage(item, index) {
      this.$common.actionSheet({
        header: 'Do you want to delete this message ?',
        buttons: [
          {
            text: 'Delete',
            role: 'destructive',
            handler: () => {
              this.doDeleteMessage(item, index);
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
    doDeleteMessage(item, index) {
      this.$common.showLoading('Deleting...');
      deleteMessage(item.messageId)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.messages.splice(index, 1);
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    showMessage(item, index) {
      this.currentMessage = item;
      this.isOpen = true;
    }
  }
};
</script>
<style scoped>
ion-toolbar {
  --border-style: none;
}

/* btn text */
ion-button {
  --background-activated: none;
}
ion-badge {
  position: absolute;
  top: 0px;
  right: 5px;
}
ion-list {
  padding-top: 5px;
}
.topbox {
  display: flex;
  align-items: center;
  justify-content: space-around;
  border-bottom: 8px solid #f2f2f2;
}
.topbox-btn {
  padding-top: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  position: relative;
  flex: 1;
  margin: 0 16px;
}
.msg-btn {
  font-size: 1.5rem;
}
.btn-txet {
  padding: 8px 0 20px 0;
  font-weight: 500;
  font-size: 0.9rem;
}
.modal-btn {
  height: 30px;
  padding: 0 10px;
}

/* btn style */
#follow-btn {
  --background: #d3f0fd;
}
#fans {
  color: #3174e7;
}
#like-btn {
  --background: #ffe1e1;
}
#like {
  color: #f05454;
}
#comment-btn {
  --background: #d4fcd6;
}
#comment {
  color: #30df20;
}
</style>