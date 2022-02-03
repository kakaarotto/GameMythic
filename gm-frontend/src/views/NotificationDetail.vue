<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>Notification received</ion-title>
      <GoBack :routeName="routerName" />
    </ion-header>
    <ion-header class="container-header">
      <div class="title">
        <ion-text
          :class="title == item.id ? 'top-nav' : ''"
          v-for="(item, index) in headerTitles"
          :key="index"
          @click="select(item.id)"
        >{{ item.title }}</ion-text>
      </div>
    </ion-header>

    <ion-content>
      <ion-list class="received-list">
        <ion-item
          v-for="(item, index) in messages"
          :key="index"
          @click="onShowPopover(item, index, $event)"
        >
          <ion-avatar slot="start">
            <img :src="item.fromUser.avatar" />
          </ion-avatar>
          <ion-label>
            <h2>{{item.fromUser.username}}</h2>
            <p v-if="title == 'newfollwers'">Followed you {{addTimeShow(item.addTime)}}</p>
            <p v-if="title == 'liked'">Liked your content {{addTimeShow(item.addTime)}}</p>
            <p v-if="title == 'commented'">Comment your content {{addTimeShow(item.addTime)}}</p>
            <h4 v-if="title == 'commented'">{{item.fromCommentContent}}</h4>
          </ion-label>
          <!-- <ion-button v-if="title == 'newfollwers'" shape="round" size="small" fill="outline" @click="onShowPopover(item, index, $event)">Follow</ion-button> -->
          <img
            v-if="title == 'liked' || title == 'commented'"
            class="content-cover"
            :src="item.fromContent.imageList[0]"
          />
        </ion-item>
      </ion-list>
      <div class="bottom-h"></div>
    </ion-content>

    <!-- Message pop -->
    <ion-popover
      :is-open="isShowPopover"
      :event="popoverEvent"
      :translucent="true"
      @didDismiss="isShowPopover = false"
    >
      <ion-list>
        <ion-item class="message-pop" lines="none" detail="false" button @click="onDelete()">
          <ion-label>Delete</ion-label>
          <ion-icon :icon="trash" slot="end"></ion-icon>
        </ion-item>
      </ion-list>
    </ion-popover>
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
  IonPopover
} from '@ionic/vue';
import { trash } from 'ionicons/icons';
import moment from 'moment';
import { getMessages, updateReadMultiple, deleteMessage } from '../api/message';

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
    IonThumbnail,
    IonPopover
  },
  setup() {
    return {
      trash
    };
  },
  data() {
    return {
      title: this.$route.query.name,
      headerTitles: [
        {
          id: 'newfollwers',
          title: 'New Follwers'
        },
        {
          id: 'liked',
          title: 'Liked'
        },
        {
          id: 'commented',
          title: 'Commented'
        }
      ],
      messages: [],
      routerName: '',
      isShowPopover: false,
      popoverData: null,
      popoverIndex: 0,
      popoverEvent: false
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.routerName = from.name;
    });
  },
  mounted() {
    console.log(this.title);
    // Type： 1 followed，2 liked，3 commented，4 content
    this.getMessages();
  },
  methods: {
    onShowPopover(item, index, event) {
      console.log(item);
      this.isShowPopover = true;
      this.popoverData = item;
      this.popoverIndex = index;
      this.popoverEvent = event;
    },
    onDelete() {
      if (this.title == 'newfollwers') {
        this.isShowPopover = false;
        this.onDoDelete();
      }
      if (this.title == 'liked') {
        this.isShowPopover = false;
        this.onDoDelete();
      }
      if (this.title == 'commented') {
        this.isShowPopover = false;
        this.onDoDelete();
      }
    },
    // Delete recived message
    onDoDelete() {
      this.$common.showLoading('Deleting...');
      deleteMessage(this.popoverData.messageId)
        .then((res) => {
          // this.isShowPopover = false;
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.messages.splice(this.popoverIndex, 1);
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    select(title) {
      this.title = title;
      this.getMessages();
    },
    addTimeShow(t) {
      return moment(t).startOf('hour').fromNow();
    },
    getMessages() {
      this.messages = [];
      if (this.title == 'newfollwers') {
        this.type = 1;
      }
      if (this.title == 'liked') {
        this.type = 2;
      }
      if (this.title == 'commented') {
        this.type = 3;
      }
      let pamras = {
        page: 0,
        size: 1000,
        type: this.type
      };
      // Get message list
      getMessages(pamras).then((res) => {
        if (res.data.code == 1) {
          this.messages = res.data.content;
          let ids = [];
          // Get the id of an unread status message
          for (let item of this.messages) {
            if (item.readState == 0) {
              // Save to ids
              ids.push(item.messageId);
            }
          }
          // >0 is unread message
          if (ids.length > 0) {
            // Update read status
            this.updateReadMultiple(ids);
          }
        }
      });
    },
    updateReadMultiple(ids) {
      updateReadMultiple({
        ids
      }).then((res) => {
        this.$mybus.emit('updateMessageCount');
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
  height: 40px;
}

ion-title {
  height: 32px;
  padding-bottom: 5px;
}

.top-nav {
  color: #ff9820;
}

.container-header {
  background: #fff;
  width: 100%;
  border-bottom: 1px solid #f2f2f2;
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 0 10px;
  z-index: 999;
  height: 46px;
}

.container-header .title {
  width: 80%;
  display: flex;
  justify-content: space-between;
  font-size: 0.95rem;
}

.received-list {
  margin: 0 0 0 -7px;
}
.received-list img {
  object-fit: cover;
}
ion-avatar {
  height: 42px;
  width: 42px;
  margin-inline: unset;
  margin-right: 12px;
}
.received-list p {
  margin-top: 2px;
  font-size: 0.7rem;
}

.content-cover {
  height: 38px;
  width: 38px;
  margin-right: 12px;
  border-radius: 4px;
}

.commented-avatar {
  margin-bottom: 27px;
}
ion-button {
  width: 58px;
  margin-right: 12px;
  --border-style: #808289;
  --background: #808289;
}
h4 {
  margin-top: 8px;
}
.message-pop {
  color: #ff4961;
}
.message-pop ion-label {
  text-align: center;
}
</style>
