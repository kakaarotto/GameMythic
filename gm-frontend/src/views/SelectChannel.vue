<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>Selelct Channle</ion-title>
      <GoBack :routeName="routeName"/>
      <ion-searchbar placeholder="Search Channle" v-model="keyword" @ionChange="onSearchChange" @ionClear="onSearchClear"></ion-searchbar>
    </ion-header>

    <ion-content>
      <!-- Channle list -->
      <div v-for="(item, index) in channels" :key="index">
        <div class="channel-item" @click="jump(item)">
          <div class="channel-item-content">
            <img :src="item.logo" alt />
            <div class="channel-item-right">
              <ion-text>{{ item.name }}</ion-text>
            </div>
          </div>
        </div>
      </div>
      <div class="bottom-h"></div>
    </ion-content>
  </ion-page>
</template>

<script>
import { getChannels } from '../api/channel';
import GoBack from '@/components/GoBack.vue';
import { IonPage, IonHeader, IonToolbar, IonTitle, IonContent, IonText,IonSearchbar } from '@ionic/vue';

export default {
  name: 'SelectChannel',
  components: {
    GoBack,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonText,
    IonSearchbar
  },
  data() {
    return {
      channels: [],
      channelsCache: [],
      keyword: '',
      searchRuning: false,
	  routeName:''
    };
  },
  beforeRouteEnter(to,from,next){
  	next(vm=>{
  		vm.routeName = from.name
  	})
  },
  mounted() {
    this.getChannels();
  },
  methods: {
    // Get channle list
    getChannels() {
      let params = {
        size: 1000,
        page: 0
      };
      getChannels(params).then((res) => {
        if (res.data.code == 1) {
          this.channels = res.data.content;
          this.channelsCache = res.data.content;
        } else {
          this.channels = [];
          this.channelsCache = [];
        }
      });
    },
    // Select channle
    jump(item) {
      this.$mybus.emit('testBus', item);
      this.$router.back();
    },
    // Search channles
    onSearchChange() {
      if(!this.searchRuning && this.keyword) {
        this.searchRuning = true;

        let searchResult = []
        this.channelsCache.map(cha => {
          if(cha.name.toLowerCase().indexOf(this.keyword.toLowerCase()) > -1) {
            searchResult.push(cha)
          }
        })
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

/* Channle list */
.channel-item {
  padding: 5px 0;
  border-bottom: 1px solid #f4f4f4;
}
.channel-item-content {
  padding: 0 15px;
  display: flex;
}
.channel-item img {
  border-radius: 5px;
  width: 60px;
  height: 60px;
  object-fit: cover;
}
.channel-item-right {
  margin-left: 10px;
}
.channel-item-right ion-text {
  font-size: 0.8rem;
}
</style>
