<template>
  <ion-page class="container">
    <h1>Game Mythic</h1>
    <ion-button shape="round" @click="register">Create Account</ion-button>
    <ion-router-link color="primary" @click="login">Log in</ion-router-link>
    <!-- <section>
      <ion-icon :icon="logoGoogle" />
      <ion-icon :icon="logoFacebook" />
      <ion-icon :icon="logoTwitter" />
    </section>-->
  </ion-page>
</template>

<script>
import { IonPage, IonButton, IonIcon } from '@ionic/vue';
import { logoGoogle, logoFacebook, logoTwitter } from 'ionicons/icons';

export default {
  name: 'Index',
  components: { IonPage, IonButton, IonIcon },
  setup() {
    return {
      logoGoogle,
      logoFacebook,
      logoTwitter
    };
  },
  created() {
    // If user logged in, go to Home page
    if (localStorage.getItem('authenticationToken')) {
      // Stor user state
      let userInfoJson = localStorage.getItem('userInfo');
      if (userInfoJson) {
        let userInfo = JSON.parse(userInfoJson);
        if (userInfo && userInfo.loginTime) {
          let nowTime = new Date();
          let loginTime = new Date(userInfo.loginTime);
          let dayTime = nowTime.getTime() - loginTime.getTime();
          let dayNum = Math.floor(dayTime / (1000 * 60 * 60 * 24));
          // If user have not operated the app for more than 30 days, user need to login again
          if (dayNum > 30) {
            localStorage.removeItem('authenticationToken');
          } else {
            // Refresh time
            userInfo.loginTime = new Date();
            localStorage.setItem('userInfo', JSON.stringify(userInfo));
            // Go to home page
            this.$router.replace({ path: '/tabs/home' });
          }
        }
      }
    }
  },
  methods:{
	  register(){
		 this.$router.push({
		  name:'register',	
		  params:{
		  	routeMode:'push'
		  },
		 }); 
	  },
	  login(){
		this.$router.push({
		  name:'login',	
		  params:{
		  	routeMode:'push'
		  },
		});  
	  }
  }
};
</script>

<style scoped>
.container {
  text-align: center;
  padding-top: 20%;
}

ion-button {
  width: 60%;
  margin-top: 40%;
}
ion-router-link {
  display: block;
  margin-top: 20px;
  color: #ff9820;
}
/* section {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
}
ion-icon {
  font-size: 2rem;
  margin-top: 40%;
} */
</style>
