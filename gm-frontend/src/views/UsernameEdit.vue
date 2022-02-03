<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>Username Edit</ion-title>
      <GoBack :routeName="routeName"/>
      <ion-button expand="block" :disabled="buttonDisable" @click="onSave">Save</ion-button>
    </ion-header>

    <ion-content>
      <!-- Edit username -->
      <ion-list>
        <ion-input
          type="text"
          placeholder="Username"
          maxlength="20"
          class="username-modify"
          required
          v-model="username"
          @ionChange="onUsernameValid"
        ></ion-input>
        <div class="character-show">
          <span>{{watchMaxInt}}</span>
          <span>/</span>
          <span>20</span>
        </div>
        <div class="username-tips">
          <span>Please without invalid characters such as !~@>/^{?</span>
        </div>
      </ion-list>
    </ion-content>
  </ion-page>
</template>

<script>
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
  IonInput
} from '@ionic/vue';
import GoBack from '@/components/GoBack.vue';
import { updateUsername } from '../api/account';

export default {
  name: 'UsernameEdit',
  components: {
    GoBack,
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
    IonInput
  },
  data() {
    return {
      userInfo: null,
      username: '',
      usernameValidationSuccess: false,
      usernameValidationText: '',
      buttonDisable: true,
      watchMaxInt: 0,
	  routeName:""
    };
  },

  beforeRouteEnter(to,from,next){
  	next(vm=>{
  		vm.routeName = from.name
  	})
  },
  created() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    this.username = this.userInfo.username;
    this.watchMaxInt = this.username.length;
  },
  methods: {
    // Verify the changed username
    onUsernameValid() {
      this.buttonDisable = true;
      this.usernameValidationSuccess = false;
      this.watchMaxInt = this.username.length;
      if (!this.username) {
        // this.usernameValidationText = "Please enter your username"
      } else if (!this.username.match(/^[a-zA-Z0-9]+$/)) {
        this.usernameValidationText = 'Please enter the username format';
      } else {
        this.usernameValidationText = '';
        this.buttonDisable = false;
        this.usernameValidationSuccess = true;
      }
    },
    // Save the changed username
    onSave() {
      this.$common.showLoading();
      updateUsername(this.username)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            this.usernameValidationText = res.data.message;
            this.buttonDisable = true;

            this.userInfo.username = this.username;
            localStorage.setItem('userInfo', JSON.stringify(this.userInfo));

            this.$router.back();
          } else {
            this.$common.error(res.data.message);
          }
        })
        .catch(() => {
          this.$common.hideLoading();
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
ion-button {
  margin: 0 auto;
  --color: #ff9820;
  --background: unset;
  --background-activated: none;
  width: auto;
  display: inline;
  position: absolute;
  right: 1px;
  bottom: 1px;
}
.list-ios {
  padding: 10px 12px;
  height: 100%;
  background-color: #f2f2f2;
}
.username-modify {
  border-radius: 8px;
  background: #fff;
  margin: 10px 0 10px 0;
  padding-left: 10px;
}
.username-tips {
  font-size: 0.8rem;
  color: #a0a0a0;
}
.character-show {
  display: flex;
  box-sizing: -webkit-border-box;
  align-items: center;
  position: absolute;
  right: 22px;
  top: 30px;
  color: #b3b3b3;
  font-size: 0.9rem;
  z-index: 2;
}
</style>
<style type="text/css">
.username-modify .native-input[placeholder] {
  font-size: 1rem;
  padding-left: 10px;
}
</style>