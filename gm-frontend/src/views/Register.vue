<template>
  <ion-page class="container">
    <ion-header>
      <GoBack :routeName="routeName" />
    </ion-header>
    <h3>Enter email and password</h3>
    <!-- Inpunt box -->
    <ion-list>
      <!-- Email -->
      <ion-item>
        <ion-input
          type="email"
          inputmode="email"
          placeholder="Email"
          v-model="registerForm.email"
          required
          @ionChange="onEmailValid"
        ></ion-input>
        <ion-icon v-if="emailValidationSuccess" color="success" :icon="checkmarkCircleOutline"></ion-icon>
      </ion-item>
      <ion-text color="danger">{{ emailValidationText }}</ion-text>

      <!-- Password -->
      <ion-item>
        <ion-input
          type="password"
          placeholder="Password"
          v-model="registerForm.password"
          required
          @ionChange="onPasswordValid"
        ></ion-input>
        <ion-icon v-if="passwordValidationSuccess" color="success" :icon="checkmarkCircleOutline"></ion-icon>
      </ion-item>
      <ion-text color="danger">{{ passwordValidationText }}</ion-text>
    </ion-list>

    <!-- Next button -->
    <ion-button expand="block" :disabled="buttonDisable" @click="onNext">Next</ion-button>
  </ion-page>
</template>

<script>
import {
  IonPage,
  IonHeader,
  IonButton,
  IonInput,
  IonList,
  IonItem,
  IonText,
  IonIcon
} from '@ionic/vue';
import { checkmarkCircleOutline } from 'ionicons/icons';
import GoBack from '@/components/GoBack.vue';
import { findEmail } from '../api/login';
export default {
  name: 'Resgister',
  components: {
    IonPage,
    IonHeader,
    IonButton,
    IonInput,
    IonList,
    IonItem,
    IonText,
    IonIcon,
    GoBack
  },
  setup() {
    return {
      checkmarkCircleOutline
    };
  },
  data() {
    return {
      registerForm: {
        email: '',
        password: ''
      },
      buttonDisable: true,
      emailValidationSuccess: false,
      emailValidationText: '',
      passwordValidationSuccess: false,
      passwordValidationText: '',
      routeName: ''
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      if (from.name == 'registerAvatar') {
        vm.routeName = 'indexPage';
      }
    });
  },

  methods: {
    onNext() {
      // Check if registration is approved
      findEmail(this.registerForm.email).then((res) => {
        if (res.data.code === 1) {
          this.emailValidationText = res.data.message;
          this.buttonDisable = true;
          this.emailValidationSuccess = false;
        } else {
          let data = {
            email: this.registerForm.email,
            password: this.registerForm.password,
            avatar: null
          };
          localStorage.setItem('registerData', JSON.stringify(data));
          setTimeout(() => {
            this.$router.push({ path: '/registerAvatar' });
          }, 500);
        }
      });
    },
    // Email regular validation
    onEmailValid() {
      this.buttonDisable = true;
      this.emailValidationSuccess = false;
      if (!this.registerForm.email) {
        this.emailValidationText = 'Please enter your email';
      } else if (
        !this.registerForm.email.match(
          /^[a-z0-9]+([._\\-]*[a-z0-9])*@([a-z0-9]+[-a-z0-9]*[a-z0-9]+.){1,63}[a-z0-9]+$/
        )
      ) {
        this.emailValidationText = 'Please enter the email format';
      } else {
        this.emailValidationText = '';
        this.buttonDisable = this.passwordValidationText == '' ? false : true;
        this.emailValidationSuccess = true;
      }
    },
    // Password regular validation
    onPasswordValid() {
      this.buttonDisable = true;
      this.passwordValidationSuccess = false;
      if (!this.registerForm.password) {
        this.passwordValidationText = 'Please enter your password';
      } else if (this.registerForm.password.length < 6) {
        this.passwordValidationText = 'The password must be at least 6 characters long';
      } else if (!this.registerForm.password.match(/([A-Z])+/)) {
        this.passwordValidationText = 'The password must contain uppercase letters';
      } else if (!this.registerForm.password.match(/([0-9])+/)) {
        this.passwordValidationText = 'The password must contain numbers';
      } else {
        this.passwordValidationText = '';
        this.buttonDisable = this.emailValidationText == '' ? false : true;
        this.passwordValidationSuccess = true;
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
