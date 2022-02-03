<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>Change Password</ion-title>
      <GoBack :routeName="routeName"/>
      <ion-button expand="block" :disabled="buttonDisable" @click="onSave">Save</ion-button>
    </ion-header>
    <ion-content>
      <ion-list>
        <!-- Old password -->
        <div class="password-input">
          <ion-input
            type="password"
            placeholder="Enter old password"
            v-model="oldPassword"
            required
            @ionChange="onPasswordValid"
            class="password-modify"
          ></ion-input>

          <!-- New password -->
          <ion-input
            type="password"
            placeholder="Enter new password"
            v-model="newPassword"
            required
            @ionChange="onPasswordValid"
            class="password-modify"
          ></ion-input>

          <!-- Confirm password -->
          <ion-input
            type="password"
            placeholder="Confirm new password"
            v-model="newPasswordConfirm"
            required
            @ionChange="onPasswordValid"
            class="password-modify"
          ></ion-input>
        </div>

        <!-- password alert -->
        <div class="password-err" v-if="passwordValidationText">
          <ion-text color="danger">{{ passwordValidationText }}</ion-text>
        </div>

        <div class="password-tips">
          <span>Please set password must be no less than 6 digits and must contain both uppercase and numbers</span>
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
  IonButton,
  IonItem,
  IonList,
  IonInput,
  IonIcon
} from '@ionic/vue';
import GoBack from '@/components/GoBack.vue';
import { updatePassword } from '../api/account';

export default {
  name: 'PasswordEdit',
  components: {
    GoBack,
    IonPage,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonText,
    IonLabel,
    IonButton,
    IonItem,
    IonList,
    IonInput,
    IonIcon
  },
  data() {
    return {
      oldPassword: '',
      newPassword: '',
      newPasswordConfirm: '',
      buttonDisable: true,
      passwordValidationText: '',
	  routeName:""
    }
  },
  beforeRouteEnter(to,from,next){
  	next(vm=>{
  		vm.routeName = from.name
  	})
  },
  methods: {
    // Password verification
    onPasswordValid() {
      this.buttonDisable = true;
      // Old
      if (!this.oldPassword) {
        this.passwordValidationText = "Please enter your old password"
      } else if (this.oldPassword.length < 6) {
        this.passwordValidationText = 'The old password must be at least 6 characters long';
      } else if (!this.oldPassword.match(/([A-Z])+/)) {
        this.passwordValidationText = 'The old password must contain uppercase letters';
      } else if (!this.oldPassword.match(/([0-9])+/)) {
        this.passwordValidationText = 'The old password must contain numbers';
      } 
      // New
      else if (!this.newPassword) {
        this.passwordValidationText = "Please enter your new password"
      } else if (this.newPassword.length < 6) {
        this.passwordValidationText = 'The new password must be at least 6 characters long';
      } else if (!this.newPassword.match(/([A-Z])+/)) {
        this.passwordValidationText = 'The new password must contain uppercase letters';
      } else if (!this.newPassword.match(/([0-9])+/)) {
        this.passwordValidationText = 'The new password must contain numbers';
      } 
      // Confirm
      else if (!this.newPasswordConfirm) {
        this.passwordValidationText = "Please enter your confirm password"
      } else if (this.newPasswordConfirm.length < 6) {
        this.passwordValidationText = 'The confirm password must be at least 6 characters long';
      } else if (!this.newPasswordConfirm.match(/([A-Z])+/)) {
        this.passwordValidationText = 'The confirm password must contain uppercase letters';
      } else if (!this.newPasswordConfirm.match(/([0-9])+/)) {
        this.passwordValidationText = 'The confirm password must contain numbers';
      } 
      else if (this.newPassword != this.newPasswordConfirm) {
        this.passwordValidationText = 'Confirm password mismatch';
      } 

      else {
        this.passwordValidationText = '';
        this.buttonDisable = false;
      }
    },
    // Save new password
    onSave() {
      if(this.oldPassword == '') {
        return;
      }
      if(this.oldPassword == '') {
        return;
      }
      if(this.newPassword != this.newPasswordConfirm) {
        return;
      }
      this.$common.showLoading();
      updatePassword({
        oldPassword: this.oldPassword,
        password: this.newPassword
      }).then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
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
  --color:#FF9820;
  --background:unset;
  --background-activated:none;
  width:auto;
  display:inline;
  position:absolute;
  right:1px;
  bottom:1px;
}

.list-ios {
  padding-top: 30px;
  height: 100%;
  background-color: #f2f2f2;
}
.password-modify {
  margin-bottom: 15px;
}
.password-modify:last-child {
  margin-bottom: 0;
}
.password-input {
  background-color: #fff;
  padding: 10px 0 1px 0;
  margin-bottom: 20px;
}
.password-tips {
  padding: 0 16px;
  font-size: 0.8rem;
  color: #a0a0a0;
}
.password-err {
  padding: 0 16px 30px 16px;
  font-size: 0.8rem;
}
</style>

<style type="text/css">
.password-modify .native-input {
  font-size: 1rem;
  border-bottom: 1px solid #eeeeee;
  padding: 10px 3px;
  padding-left: 15px;
}
</style>