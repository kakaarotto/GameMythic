<template>
  <ion-page class="container">
    <ion-header>
      <ion-title>Setting</ion-title>
      <GoBack :routeName='routeName'/>
    </ion-header>

    <ion-content>
      <section class="account">
        <!-- User Avatar -->
        <div class="account-section" @click="onPhotoPicker(true)">
          <ion-text>Avatar</ion-text>
          <div class="account-section-right">
            <ion-avatar>
              <img :src="userInfo.avatar" />
            </ion-avatar>
            <ion-icon :icon="chevronForwardOutline"></ion-icon>
          </div>
        </div>

        <!-- Username -->
          <div class="account-section" @click="usernameEdit">
            <ion-text>Username</ion-text>
            <div class="account-section-right">
              <ion-text class="username-text">{{userInfo.username}}</ion-text>
              <ion-icon :icon="chevronForwardOutline"></ion-icon>
            </div>
          </div>

        <!-- Change Password -->
          <div class="account-section" @click="Changespassword">
            <ion-text>Change password</ion-text>
            <ion-icon :icon="chevronForwardOutline"></ion-icon>
          </div>
      </section>

      <!-- Notification Setting -->
      <section class="settings">
		  <div class="account-section" @click='notificationSwitch'>
			<ion-text>Notification setting</ion-text>
			<ion-icon :icon="chevronForwardOutline"></ion-icon>
		  </div>
      </section>


      <div class="logout">
        <ion-button expand="block" @click="logoutAlertConfirm">Logout</ion-button>
      </div>
    </ion-content>
  </ion-page>
</template>

<script>
import GoBack from '@/components/GoBack.vue';
import { chevronForwardOutline } from 'ionicons/icons';
import { Camera, CameraResultType } from '@capacitor/camera';
import { uploadPicture } from '../api/picture';
import { updateAvatar } from '../api/account';
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
  IonAvatar,
  IonButton,
  alertController
} from '@ionic/vue';

export default {
  name: 'Setting',
  components: {
    GoBack,
    IonHeader,
    IonToolbar,
    IonTitle,
    IonContent,
    IonPage,
    IonText,
    IonLabel,
    IonItem,
    IonList,
    IonAvatar,
    IonButton,
    IonIcon
  },
  setup() {
    return {
      chevronForwardOutline
    };
  },
  data() {
    return {
      userInfo: {
        avatar: null,
        username: '',
		routeName:''
      }
    };
  },
  mounted() {
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
  },
  beforeDestroy() {
    this.$mybus.off('settingChange');
  },
  created() {
    this.$mybus.on('settingChange', (data) => {
      this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    });
  },
  beforeRouteEnter(to,from,next){
  	next(vm=>{
  		vm.routeName = "profile"
  	})
  },
  methods: {
    async logoutAlertConfirm() {
      const alert = await alertController.create({
        cssClass: 'my-custom-class',
        header: 'Are you sure you want to logout?',
        buttons: [
          {
            text: 'Cancel',
            role: 'cancel',
            cssClass: 'secondary',
            handler: (blah) => {
              console.log('Confirm Cancel:', blah);
            }
          },
          {
            text: 'Confirm',
            handler: () => {
              console.log('Confirm save');
              this.logout();
            }
          }
        ]
      });
      return alert.present();
    },
    logout() {
      localStorage.removeItem('authenticationToken');
      localStorage.removeItem('userInfo');
      setTimeout(() => {
        this.$router.replace({ path: '/' });
      }, 500);
    },
    onSave() {
      updateAvatar(this.userInfo.avatar)
        .then((res) => {
          this.$common.hideLoading();
          if (res.data.code == 1) {
            //this.userInfo.avatar = this.userInfo
            localStorage.setItem('userInfo', JSON.stringify(this.userInfo));
          } else {
            this.$common.error(res.data.message);
          }
        })
        .catch(() => {
          this.$common.hideLoading();
        });
    },
    // Change user avatar
    onPhotoPicker() {
      Camera.getPhoto({
        quality: 90,
        allowEditing: true,
        resultType: CameraResultType.DataUrl
      }).then((res) => {
        let imageUrl = res.dataUrl;
        let file = this.dataURLtoFile(imageUrl, 'test.jpeg');
        this.uploadPicture(file);
      });
    },
    dataURLtoFile(dataurl, filename) {
      var arr = dataurl.split(',');
      var mime = arr[0].match(/:(.*?);/)[1];
      var bstr = window.atob(arr[1]);
      var n = bstr.length;
      var u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      var blob = new Blob([u8arr], { type: mime });
      blob.lastModifiedDate = new Date();
      blob.name = filename;
      return blob;
    },
    // Uoload img path
    uploadPicture(file) {
      let fileObj = file;
      const form = new FormData();
      form.append('file', fileObj);
      form.append('path', '/avatar/');
      uploadPicture(form).then((res) => {
        if (res.status == 200) {
          this.userInfo.avatar = res.data.itemMaps.imgUrl;
          this.onSave();
        }
      });
    },
	usernameEdit()
	{
		this.$router.push({
		  name:'usernameEdit',
		  params:{
			  routeMode:'push'
		  },
		});
	},
	Changespassword()
	{
		this.$router.push({
		  name:'passwordEdit',
		  params:{
			  routeMode:'push'
		  },
		});
	},

	notificationSwitch()
	{
		this.$router.push({
		  name:'notificationSwitch',
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
  padding-top: 15%;
}

ion-header {
  height: 40px;
}

ion-title {
  height: 32px;
  padding-bottom: 5px;
}

ion-content {
  --background: #f2f2f2;
}

/* Account part */
.account ion-icon {
  color: #999;
  padding-right: 10px;
  font-size: 1.1rem;
}
.settings ion-icon {
  color: #999;
  padding-right: 10px;
  font-size: 1.1rem;
}
.account {
  margin-top: 12px;
}
.settings {
  margin-top: 12px;
}
.settings .account-section {
  border-bottom: 1px solid #ddd;
}
.account-section {
  display: flex;
  justify-content: space-between;
  align-items: center;
  line-height: 50px;
  font-size: 0.9rem;
  padding-left: 13px;
  text-align: left;
  background: #fff;
  border-top: 1px solid #ddd;
}
.account-section-right {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
ion-avatar {
  padding: 10px;
  height: 70px;
  width: 70px;
  object-fit: cover;
}
.username-text {
  padding-right: 18px;
}
.account .account-section:last-child {
  border-bottom: 1px solid #ddd;
}

/* Logout btn */
.logout {
  padding: 25px 12px;
}
.logout ion-button {
  height: 50px;
  padding: 4px 0;
}
</style>
