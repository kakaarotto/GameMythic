<template>
  <ion-page class="container">
    <ion-header>
      <GoBack :routeName="routeName" />
    </ion-header>
    <h3>Add your username</h3>

    <!-- Username Input -->
    <ion-list>
      <ion-item>
        <ion-input
          type="text"
          placeholder="Username"
          maxlength="20"
          v-model="registerForm.username"
          required
          @ionChange="onUsernameValid"
        ></ion-input>
        <ion-icon v-if="usernameValidationSuccess" color="success" :icon="checkmarkCircleOutline"></ion-icon>
      </ion-item>
      <ion-text color="danger">{{ usernameValidationText }}</ion-text>
    </ion-list>

    <!-- Next button -->
    <ion-button expand="block" :disabled="buttonDisable" @click="onNext">Next</ion-button>
  </ion-page>
</template>

<script>
import { IonPage, IonHeader, IonButton, IonInput, IonList } from '@ionic/vue';
import { checkmarkCircleOutline } from 'ionicons/icons';
import { findAccount, register, login } from '../api/login';
import { getUserInfo } from '../api/account';
import GoBack from '@/components/GoBack.vue';
export default {
  name: 'ResgisterUsername',
  components: { IonPage, IonHeader, IonButton, IonInput, IonList, GoBack },
  setup() {
    return {
      checkmarkCircleOutline
    };
  },
  data() {
    return {
      registerForm: {
        email: '',
        password: '',
        avatar: '',
        username: ''
      },
      usernameValidationSuccess: false,
      usernameValidationText: '',
      buttonDisable: true
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.routeName = from.name;
    });
  },
  created() {
    let dataString = localStorage.getItem('registerData');
    let registerData = JSON.parse(dataString);

    this.registerForm.email = registerData.email;
    this.registerForm.password = registerData.password;
    this.registerForm.avatar = registerData.avatar;
  },
  methods: {
    // Check username
    onNext() {
      this.$common.showLoading();
      findAccount(this.registerForm.username)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.usernameValidationText = res.data.message;
            this.buttonDisable = true;
          } else {
            this.onRegister();
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    // Registration Verification
    onRegister() {
      this.$common.showLoading('Logging...');
      register(this.registerForm)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code === 1) {
            // Successful registration will automatic login
            this.onLogin();
          } else {
            this.$common.error(res.data.message);
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    onLogin() {
      let data = {
        username: this.registerForm.email,
        password: this.registerForm.password
      };
      login(data)
        .then((res) => {
          if (res.data.code === 1) {
            localStorage.setItem('authenticationToken', res.data.id_token);
            this.getUserInfo();
          } else {
            this.$common.error('Please check the email and password', 'login failed');
            setTimeout(() => {
              this.$router.replace({ path: '/login' });
            }, 500);
          }
        });
    },
    // Matching user information
    getUserInfo() {
      getUserInfo().then((res) => {
        if (res.data.code == 1) {
          let userInfo = res.data.content;
          userInfo.loginTime = new Date();
          localStorage.setItem('userInfo', JSON.stringify(userInfo));
          setTimeout(() => {
            this.$router.replace({ path: '/tabs/home' });
          }, 500);
        }
      });
    },
    // Email regular validation
    onUsernameValid() {
      this.buttonDisable = true;
      this.usernameValidationSuccess = false;
      if (!this.registerForm.username) {
        this.usernameValidationText = 'Please enter your username';
      } else if (!this.registerForm.username.match(/^[a-zA-Z0-9]+$/)) {
        this.usernameValidationText = 'Please enter the username format';
      } else {
        this.usernameValidationText = '';
        this.buttonDisable = false;
        this.usernameValidationSuccess = true;
      }
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
ion-text {
  font-size: 0.8rem;
  padding-left: 20px;
}
ion-button {
  margin: 0 auto;
  margin-top: 20%;
  width: 60%;
}
</style>
