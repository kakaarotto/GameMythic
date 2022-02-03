<template>
  <ion-page class="container">
    <ion-header>
      <ion-searchbar
        v-model="keyword"
        show-cancel-button="always"
        @ionCancel="onCancel"
        @ionChange="onSearchChange"
        placeholder="Search content"
      ></ion-searchbar>
    </ion-header>
    <ion-content>
      <!-- Search result -->
      <div class="game">
        <div class="game-list">
          <div
            class="game-list-item"
            v-for="(item, index) in searchResults"
            :key="index"
            @click="gotoDetail(item.contentId)"
          >
            <img :src="item.images" alt />
            <ion-text class="content-title">{{ item.title }}</ion-text>
            <div class="icon">
              <i class="ico icon-like"></i>
              <ion-text class="number">{{ item.goodCount }}</ion-text>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-h"></div>
    </ion-content>
  </ion-page>
</template>

<script>
import { IonPage, IonHeader, IonContent, IonText, IonSearchbar } from '@ionic/vue';
import { searchByKeyword } from '../api/content';
export default {
  name: 'Search',
  components: {
    IonHeader,
    IonContent,
    IonPage,
    IonText,
    IonSearchbar
  },
  data() {
    return {
      keyword: '',
      searchResults: [],
      searchRuning: false,
      isInfiniteDisabled: false
    };
  },
  methods: {
    // Go to content detail
    gotoDetail(contentId) {
      this.$router.push('/detail?contentId=' + contentId);
    },
    onCancel() {
      this.$router.push('/tabs/home');
    },
    // Serach content and list result
    onSearchChange() {
      if (!this.searchRuning && this.keyword) {
        this.searchRuning = true;
        searchByKeyword(this.keyword)
          .then((res) => {
            this.searchRuning = false;
            if (res.data.code === 1) {
              this.searchResults = res.data.content;
            } else {
              this.searchResults = [];
            }
          })
          .catch((res) => {
            this.searchRuning = false;
          });
      }
    }
  }
};
</script>

<style scoped>
.container {
  position: relative;
  padding-top: 40px;
}

ion-router-link {
  float: right;
  padding-right: 12px;
}
ion-searchbar {
  --cancel-button-color: black;
}

/* Search result card */
.game-list {
  margin-top: 5px;
  padding: 0 5px;
  columns: 2;
  column-gap: 5px;
}
.game-list-item {
  box-shadow: 0 2px 12px 0 rgb(0 0 0 / 10%);
  transition: 0.3s;
  border-radius: 5px;
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
  line-height: 1rem;
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
  font-size: 0.8rem;
  color: #999;
}
</style>
