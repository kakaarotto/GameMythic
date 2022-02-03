<template>
  <ion-icon :icon="chevronBack" @click="goBack(routeName)" />
</template>

<script>
import { IonIcon } from '@ionic/vue';
import { chevronBack } from 'ionicons/icons';
import { useRouter } from "vue-router";
export default {
  name: 'GoBack',
  props:{
	routeName:[String],
	param:[Object]
  },
  components: { IonIcon },
  setup() {
    return {
      chevronBack,
	  useRouter
    };
  },
  
  methods: {
	// If there is no path to jump to the home page
    goBack(name) {
	  let router = this.$router;
	  console.log()
	  if(name){
		  if(this.param){
			 router.push({
				name:name,
				params:{
					routeMode:'pop'
				},
				query:{
					id:this.param.id
				}
			 }); 
		  }else{
			  router.push({
				name:name,
				params:{
					routeMode:'pop'
				},
			  });
		  }
		  
	  }
	  else if(localStorage.userInfo)
	  {
		  router.push({
			  name:'home',
			  params:{
				  routeMode:'pop'
			  },
		  	
		  });
	  }else{
		 router.push({
			  name:'indexPage',
			  params:{
				  routeMode:'pop'
			  },
		 }); 
	  }
      
    }
  },
};
</script>

<style scoped>
ion-icon {
  font-size: 1.6rem;
  margin-left: 10px;
}
</style>
