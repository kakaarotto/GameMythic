<template>
  <ion-page class="container">
    <ion-header>
      <GoBack :routeName="routeName" />
    </ion-header>
    <h3>Add your profile picture</h3>

    <!-- user avatar -->
    <div class="picture">
      <ion-icon class="upload" v-if="avatar == null" :icon="personAdd" @click="onPhotoPicker(true)"></ion-icon>
      <img
        v-else
        id="avatar"
        class="upload ios hydrated"
        :src="avatar"
        @click="onPhotoPicker(true)"
      />
    </div>

    <!-- Next buttn & Skip -->
    <ion-button expand="block" :disabled="buttonDisable" @click="onNext">Next</ion-button>
    <ion-text @click="registerUsername" style="text-align:center">Skip</ion-text>
  </ion-page>
</template>

<script>
import { IonPage, IonHeader, IonButton, IonIcon, IonActionSheet, IonText } from '@ionic/vue';
import { defineComponent } from 'vue';
import { personAdd } from 'ionicons/icons';
import GoBack from '@/components/GoBack.vue';
import { Camera, CameraResultType } from '@capacitor/camera';
import { uploadPicture } from '../api/picture';

export default defineComponent({
  name: 'ResgisterAvatar',
  components: { IonPage, IonHeader, IonButton, IonIcon, IonActionSheet, IonText, GoBack },
  setup() {
    return {
      personAdd
    };
  },
  data() {
    return {
      avatar: null,
      buttonDisable: true,
      routeName: ''
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      console.log(from);
      switch (from.name) {
        case 'registerUsername':
          vm.routeName = 'register';
          break;
        case 'register':
          vm.routeName = 'register';
          break;
        case 'registerAvatar':
          vm.routeName = 'register';
          break;
      }
    });
  },
  methods: {
    // Stor user info go next page
    onNext() {
      // Take the previous page of data
      let dataString = localStorage.getItem('registerData');
      let data = JSON.parse(dataString);
      data.avatar = this.avatar;
      // Storing the current data again
      localStorage.setItem('registerData', JSON.stringify(data));
      setTimeout(() => {
        this.$router.push({ path: '/registerUsername' });
      }, 500);
    },
    // Go to registerUsername page
    registerUsername() {
      this.$router.push({
        name: 'registerUsername',
        params: {
          routeMode: 'push'
        }
      });
    },
    // Use capacitor camera select image or take picture to upload
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
    uploadPicture(file) {
      let fileObj = file;
      const form = new FormData();
      form.append('file', fileObj);
      form.append('path', '/avatar/');
      uploadPicture(form).then((res) => {
        if (res.status == 200) {
          this.avatar = res.data.itemMaps.imgUrl;
          this.buttonDisable = false;
        }
      });
    }
  }
});
</script>

<style scoped>
.container {
  padding-top: 15%;
}
h3 {
  margin-top: 10%;
  text-align: center;
}

/* user avatar */
.picture {
  display: flex;
  justify-content: center;
  margin-top: 20%;
}
.upload {
  padding: 10px;
  font-size: 5rem;
  border: 0.2rem dashed #000;
}
img.upload {
  width: 108px;
  height: 108px;
  object-fit: cover;
}

/* Next buttn & Skip */
ion-button {
  width: 60%;
  margin: 0 auto;
  margin-top: 20%;
}

ion-text {
  display: block;
  text-align: center;
  margin-top: 20px;
  color: #ff9820;
}
</style>
