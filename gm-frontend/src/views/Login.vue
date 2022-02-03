<template>
  <ion-page class="container">
    <ion-header>
      <GoBack :routeName="routeName"/>
    </ion-header>
    <h3>Login Game Mythic</h3>
    <!-- Input box -->
    <ion-list>
      <!-- Email -->
      <ion-item>
        <ion-input type="email" placeholder="Email" v-model="loginForm.email"></ion-input>
      </ion-item>

      <!-- Password -->
      <ion-item>
        <ion-input :type="passwordType" placeholder="Password" v-model="loginForm.password"></ion-input>
        <!-- Show password -->
        <ion-icon
          v-if="loginForm.password && passwordType == 'password'"
          :icon="eye"
          @click="changePasswordType"
        ></ion-icon>
        <ion-icon
          v-if="loginForm.password && passwordType == 'text'"
          :icon="eyeOff"
          @click="changePasswordType"
        ></ion-icon>
      </ion-item>
      <!-- <ion-router-link color="primary">Forgot Password ？</ion-router-link> -->
    </ion-list>
    <ion-button expand="block" @click="onLogin">Login</ion-button>
  </ion-page>
</template>

<script>
import { IonPage, IonHeader, IonButton, IonInput, IonList, IonItem, IonIcon } from '@ionic/vue';
import { eye, eyeOff } from 'ionicons/icons';
import GoBack from '@/components/GoBack.vue';
import { login } from '../api/login';
import { getUserInfo } from '../api/account';
export default {
  name: 'Login',
  components: { IonPage, IonHeader, IonButton, IonInput, IonList, IonItem, IonIcon, GoBack },
  setup() {
    return {
      eye,
      eyeOff
    };
  },
  data() {
    return {
      passwordType: 'password',
      passwordIcon: 'eye-outline',
      loginForm: {
        email: '',
        password: ''
      },
	  routeName:""
    };
  },
  beforeRouteEnter(to,from,next){
	 next(vm=>{
	 	vm.routeName = from.name
	 })
  },

  methods: {
    // Show password
    changePasswordType() {
      if (this.passwordType == 'password') {
        this.passwordType = 'text';
      } else {
        this.passwordType = 'password';
      }
    },
    // Verify login
    onLogin() {
      let data = {
        username: this.loginForm.email,
        password: this.loginForm.password
      };
      this.$common.showLoading('Logging...');
      login(data)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code === 1) {
            //Save token as localStorage after successful login, take this token  when use the app functions
            localStorage.setItem('authenticationToken', res.data.id_token);
            this.getUserInfo();
          } else {
            this.$common.error('Please check the email and password', 'Login Failed');
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    // Check user info
    getUserInfo() {
      getUserInfo().then((res) => {
        if (res.data.code == 1) {
          let userInfo = res.data.content;
          userInfo.loginTime = new Date();
          localStorage.setItem('userInfo', JSON.stringify(userInfo));
          setTimeout(() => {
            this.$router.replace({ path: '/tabs/home' });
          }, 800);
        }
      });
    }
  }
};
</script>

<style scoped>
.container {
  padding-top: 15%;
}
h3 {
  margin-top: 10%;
  text-align: center;
}
ion-list {
  width: 85%;
  margin: 0 auto;
  margin-top: 15%;
}
ion-item {
  font-size: 0.8rem;
  margin-top: 30px;
}
ion-router-link {
  float: right;
  font-size: 0.9rem;
  margin-top: 10px;
}
ion-button {
  margin: 0 auto;
  margin-top: 20%;
  width: 60%;
}
</style>
