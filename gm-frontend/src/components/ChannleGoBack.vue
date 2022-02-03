<template>
  <ion-icon :icon="chevronBack" @click="goBack" />
</template>

<script>
import { IonIcon } from '@ionic/vue';
import { chevronBack } from 'ionicons/icons';
export default {
  name: 'ChannleGoBack',
  components: { IonIcon },
  props:{
	  routeName:{
		  type:String,
		  default:'home'
	  }
  },
  setup() {
    return {
      chevronBack
    };
  },
  methods: {
    goBack() {
      let router = this.$router;
      router.push({
		  name:this.routeName,
		  params:{
			  routeMode:'pop'
		  },
      	
      });
    }
  }
};
</script>

<style scoped>
ion-icon {
  color: #fff;
  font-size: 1.6rem;
  margin-left: 10px;
}
</style>
