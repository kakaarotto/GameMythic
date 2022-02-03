<template>
  <ion-page>
    <ion-header>
      <ion-title>Content Edit</ion-title>
      <ion-icon class="back-home" @click="confirmationSheet" :icon="chevronBack" />
    </ion-header>

    <ion-content>
      <!-- Add pictures -->
      <div class="pictures">
        <div class="pictures-item">
          <div class="selected-cover">
            <div class="add-picture" @click="addPicture">
              <ion-icon :icon="addOutline"></ion-icon>
            </div>
          </div>
        </div>
        <!-- Already selected pictures -->
        <div v-for="(item, index) in fileList" :key="index" class="pictures-item">
          <div class="selected-cover">
            <img :src="item" alt />
            <ion-icon :icon="closeCircle" class="delete-icon" @click="deletePic(index)"></ion-icon>
          </div>
        </div>
      </div>

      <ion-list>
        <!-- Contnet title -->
        <ion-item>
          <ion-input
            type="text"
            v-model="publishForm.title"
            maxlength="100"
            placeholder="Title"
            @ionChange="onPublishName"
            style="flex:none;width:89%"
          ></ion-input>
          <div class="character-show">
            <span>{{watchMaxInt}}</span>
            <span>/</span>
            <span>100</span>
          </div>
        </ion-item>

        <!-- Contnet main text -->
        <ion-item>
          <ion-textarea
            rows="10"
            maxlength="9999"
            placeholder="Write the main content"
            v-model="publishForm.content"
          ></ion-textarea>
        </ion-item>

        <!-- Select contnet category -->
        <ion-item>
          <ion-text color="medium">
            <p>Select Category</p>
            <ion-button
              class="category-btn"
              :fill="item.ifClick ? 'solid' : 'outline'"
              :color="item.ifClick ? 'warning' : ' medium'"
              @click="select(index, item.categoryId)"
              v-for="(item, index) in categories"
              :key="index"
            >{{ item.name }}</ion-button>
          </ion-text>
        </ion-item>

        <!-- Select Contnet channle -->
        <ion-item @click="selectChannle">
          <ion-text color="medium" class="select-channle">
            <p>{{ selectTitle }}</p>
            <ion-icon :icon="chevronForwardOutline"></ion-icon>
          </ion-text>
        </ion-item>
      </ion-list>

      <!-- Contnet publish and save -->
      <ion-footer class="footer">
        <div class="footer-left">
          <ion-icon :icon="document" class="save-icon" @click="saveAlertConfirm"></ion-icon>
          <span>save</span>
        </div>
        <ion-button shape="round" @click="onPublish">Publish</ion-button>
      </ion-footer>
    </ion-content>
  </ion-page>
</template>

<script>
import { getCategories } from '../api/category';
import { Camera, CameraResultType } from '@capacitor/camera';
import { createContent, getContent, updateContent } from '../api/content';
import { uploadPicture } from '../api/picture';
import {
  addOutline,
  chevronForwardOutline,
  closeCircle,
  document,
  chevronBack
} from 'ionicons/icons';
import {
  IonPage,
  IonHeader,
  IonTitle,
  IonContent,
  IonButton,
  IonInput,
  IonList,
  IonTextarea,
  IonIcon,
  IonItem,
  IonText,
  IonFooter,
  actionSheetController,
  alertController
} from '@ionic/vue';

export default {
  name: 'Publish',
  components: {
    IonPage,
    IonHeader,
    IonTitle,
    IonContent,
    IonButton,
    IonInput,
    IonList,
    IonTextarea,
    IonIcon,
    IonItem,
    IonText,
    IonFooter
  },
  setup() {
    return {
      addOutline,
      chevronForwardOutline,
      closeCircle,
      document,
      chevronBack
    };
  },
  data() {
    return {
      id: null,
      userInfo: null,
      contentId: null,
      selectTitle: 'Select Channel',
      categories: [],
      publishForm: {
        title: '',
        content: '',
        categoryId: '',
        channelsId: ''
      },
      fileList: [],
      saveKey: '',
      watchMaxInt: 0
    };
  },
  watch: {
    $route(to, from) {
      console.log('watch from---->', from.path);
      if (from.path != '/selectChannel') {
        console.log('watch---->' + this.$route.meta.keepAlive);
        console.log(
          'watch getCategories ---->' + (this.$route.query.contentId || this.$route.query.id)
        );
        // Response based on parameter data
        if (this.$route.query.contentId || this.$route.query.id) {
          this.getCategories();
        }
      }
    }
  },
  mounted() {
    console.log('mounted---->' + this.$route.meta.keepAlive);
    this.userInfo = JSON.parse(localStorage.getItem('userInfo'));
    // Distinguish the Key saved in the user's draft box
    this.saveKey = 'list' + this.userInfo.userId;
    this.getCategories();
  },
  beforeDestroy() {
    this.$mybus.off('testBus');
    console.log('mybus.off');
  },
  created() {
    console.log('mybus.created');
    this.$mybus.on('testBus', (data) => {
      console.log(data);
      this.publishForm.channelsId = data.channelsId;
      this.selectTitle = data.name;
    });
  },
  methods: {
    // Get category list
    getCategories() {
      let params = {
        size: 1000,
        page: 0
      };
      getCategories(params).then((res) => {
        if (res.data.code == 1) {
          this.categories = res.data.content;
          if (this.$route.query.id) {
            this.id = this.$route.query.id;
            this.getDetail();
          }
          if (this.$route.query.contentId) {
            this.contentId = this.$route.query.contentId;
            this.getDetail();
          }
        }
      });
    },
    // Use capacitor camera select image to upload
    addPicture() {
      // Maximum 7 images limit
      if (this.fileList.length >= 7) {
        this.$common.error('Add a maximum of 7 images', 'Alert');
        return;
      }
      // Invoking camera function configuration parameters
      Camera.getPhoto({
        quality: 90,
        allowEditing: true,
        resultType: CameraResultType.DataUrl
      }).then((res) => {
        // Asynchronously return the camera data and dataUrl
        let imageUrl = res.dataUrl;
        // Convert data
        let file = this.dataURLtoFile(imageUrl, 'test.jpeg');
        // upload
        this.uploadPicture(file);
      });
    },
    // Raw data conversion file to binary save, get raw data
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
      // Create a form object File for submitting data
      const form = new FormData();
      form.append('file', fileObj);
      // Save path
      form.append('path', '/content/');
      uploadPicture(form).then((res) => {
        if (res.status == 200) {
          // Get the URL of the image and render it to the element
          this.fileList.push(res.data.itemMaps.imgUrl);
        }
      });
    },
    select(idx, categoryId) {
      for (let item of this.categories) {
        item.ifClick = false;
      }
      this.categories[idx].ifClick = true;
      this.publishForm.categoryId = categoryId;
    },
    // Delete picture
    deletePic(idx) {
      this.fileList.splice(idx, 1);
    },
    // Jump to select channle page
    selectChannle() {
      this.$router.push({
        name: 'selectChannel',
        params: {
          routeMode: 'push'
        }
      });
    },
    clearData() {
      // clear data
      this.publishForm = {
        title: '',
        content: '',
        categoryId: '',
        channelsId: ''
      };
      this.fileList = [];
      this.selectTitle = 'Select Channel';
      for (let item of this.categories) {
        item.ifClick = false;
      }
    },
    // Publish content validation
    onPublish() {
      if (this.fileList.length == 0) {
        this.$common.error('Please upload pictures.', 'Alert');
        return;
      }
      if (this.publishForm.title == '') {
        this.$common.error('Please write title.', 'Alert');
        return;
      }
      if (this.publishForm.content == '') {
        this.$common.error('Please input content.', 'Alert');
        return;
      }
      if (this.publishForm.categoryId == '' || this.publishForm.categoryId == 0) {
        this.$common.error('Please select category.', 'Alert');
        return;
      }
      if (this.publishForm.channelsId == '' || this.publishForm.channelsId == 0) {
        this.$common.error('Please select channel.', 'Alert');
        return;
      }
      let data = {
        ...this.publishForm
      };
      data.images = this.fileList.toString();
      data.userId = this.userInfo.userId;
      if (this.$route.query.contentId) {
        this.$common.showLoading('Content under review');
        updateContent(this.$route.query.contentId, data)
          .then((res) => {
            this.$common.hideLoading();
            if (res.data.code == 1) {
            }
            // clear data
            this.clearData();
            this.$router.replace('/tabs/home');
          })
          .catch((err) => {
            this.$common.hideLoading();
          });
      } else {
        this.$common.showLoading('Content under review');
        createContent(data)
          .then((res) => {
            this.$common.hideLoading();
            if (res.data.code == 1) {
              if (this.$route.query.id) {
                let list = JSON.parse(localStorage.getItem(this.saveKey));
                for (let i in list) {
                  if (this.$route.query.id == list[i].id) {
                    list.splice(i, 1);
                  }
                }
                localStorage.setItem(this.saveKey, JSON.stringify(list));
              }
            }
            // clear data
            this.clearData();
            this.$router.replace('/tabs/home');
          })
          .catch((err) => {
            this.$common.hideLoading();
          });
      }
    },
    getDetail() {
      // query id not null, it’s from user drafts
      if (this.$route.query.id) {
        // get all draft's content list from localStorage
        let list = JSON.parse(localStorage.getItem(this.saveKey));
        for (let item of list) {
          // find current modify content
          if (this.id == item.id) {
            // fill content data to page show
            this.selectTitle = item.selectTitle;
            this.publishForm.title = item.title;
            this.publishForm.content = item.content;
            this.publishForm.categoryId = item.categoryId;
            this.publishForm.channelsId = item.channelsId;
            this.fileList = item.images.split(',');
            for (let citem of this.categories) {
              if (this.publishForm.categoryId == citem.categoryId) {
                citem.ifClick = true;
              }
            }
          }
        }
      }

      // query contentId not null, from content edit
      if (this.$route.query.contentId) {
        // get content detail from getContent api
        getContent(this.$route.query.contentId).then((res) => {
          if (res.data.code == 1) {
            // fill content data to page show
            let data = res.data.content;
            this.selectTitle = data.channels.name;
            this.publishForm.title = data.title;
            this.publishForm.content = data.content;
            if (data.category) {
              this.publishForm.categoryId = data.category.categoryId;
              for (let citem of this.categories) {
                if (this.publishForm.categoryId == citem.categoryId) {
                  citem.ifClick = true;
                }
              }
            }
            if (data.channels) {
              this.publishForm.channelsId = data.channels.channelsId;
            }
            this.fileList = data.images.split(',');
          }
        });
      }
    },
    // Save draft
    save() {
      // create a new object to save user input data
      let obj = {
        ...this.publishForm
      };
      // convert image array to string
      obj.images = this.fileList.toString();
      // get current login user id
      obj.userId = JSON.parse(localStorage.getItem('userInfo')).userId;
      obj.selectTitle = this.selectTitle;
      // if find id, save to draft with localStorage
      if (this.id) {
        // get all draft content
        let list = JSON.parse(localStorage.getItem(this.saveKey));
        for (let i in list) {
          // find the index of the current draft content in list
          if (this.id == list[i].id) {
            // remove the old draft
            list.splice(i, 1);
          }
        }
        obj.id = this.id;
        // save new draft to list
        list.push(obj);
        // save all draft to localStorage
        localStorage.setItem(this.saveKey, JSON.stringify(list));
      } else {
        // check localStorage draft have data
        if (
          localStorage.getItem(this.saveKey) == null ||
          JSON.parse(localStorage.getItem(this.saveKey)).length == 0
        ) {
          // localStorage not found data, create new list
          let list = [];
          // set id
          obj.id = 1;
          // push to list
          list.push(obj);
          // save to localStorage
          localStorage.setItem(this.saveKey, JSON.stringify(list));
        } else {
          // localStorage have data, get the list
          let list = JSON.parse(localStorage.getItem(this.saveKey));
          // set id
          obj.id = list.length + 1;
          // push to list
          list.push(obj);
          // save to localStorage
          localStorage.setItem(this.saveKey, JSON.stringify(list));
        }
      }
    },
    async saveAlertConfirm() {
      if (this.fileList.length == 0) {
        this.$common.error('Please upload the cover before saving.', 'Alert');
        return;
      }
      const alert = await alertController.create({
        cssClass: 'my-custom-class',
        header: 'Do you want to save it?',
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
            text: 'Save',
            handler: () => {
              console.log('Confirm save');
              this.save();
              // clear data
              this.clearData();
              this.$router.replace('/tabs/profile?title=Drafts');
            }
          }
        ]
      });
      return alert.present();
    },
    async confirmationSheet() {
      const actionSheet = await actionSheetController.create({
        header: 'Want to return to the home ?',
        cssClass: 'my-custom-class',
        buttons: [
          {
            text: 'Back to Home',
            handler: () => {
              // clear data
              this.clearData();
              let router = this.$router;
              router.push({
                name: 'home',
                params: {
                  routeMode: 'pop'
                }
              });
              setTimeout(() => {
                this.$router.go(0);
              }, 100);
            }
          },
          {
            text: 'Save and Exit',
            handler: () => {
              console.log('save clicked');
              if (this.fileList.length == 0) {
                this.$common.error('Please upload the cover before saving.', 'Alert');
              } else {
                this.save();
                // clear data
                this.clearData();
                this.$router.replace('/tabs/profile?title=Drafts');
              }
            }
          },
          {
            text: 'Cancel',
            role: 'cancel',
            handler: () => {
              console.log('Cancel clicked');
            }
          }
        ]
      });
      await actionSheet.present();
    },
    onPublishName() {
      this.watchMaxInt = this.publishForm.title.length;
    }
  }
};
</script>

<style scoped>
ion-header {
  height: 90px;
  padding-top: 42px;
  z-index: 99;
}
.back-home {
  font-size: 1.6rem;
  margin-left: 10px;
  margin-top: 9px;
}
ion-title {
  height: 126px;
}

/* Font size */
ion-list {
  margin: 20px 15px 0 0;
  padding-bottom: 150px;
}
ion-input {
  font-size: 0.9rem;
}
ion-textarea {
  font-size: 0.9rem;
}
ion-text {
  font-size: 0.9rem;
}

/* Upload cover */
.pictures {
  display: flex;
  flex-wrap: wrap;
  padding-right: 25px;
  margin-top: 20px;
}
.selected-cover {
  padding-left: 20px;
}
.add-picture {
  color: #ccc;
  border-radius: 5px;
  border: 3px solid #ccc;
  width: 100%;
  height: calc(25vw - 31px);
  display: flex;
  align-items: center;
  justify-content: center;
}
.add-picture ion-icon {
  font-size: 3rem;
}
.pictures-item {
  position: relative;
  flex-basis: 25%;
}
.pictures-item img {
  border-radius: 4px;
  height: 62.5px;
  width: 100%;
  object-fit: cover;
}
.pictures-item .delete-icon {
  position: absolute;
  opacity: 0.7;
  font-size: 1.5rem;
  top: -9px;
  right: -9px;
  z-index: 999;
}

/* Select part */
.select-channle {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.select-channle ion-icon {
  font-size: 1.5rem;
}
.category-btn {
  padding: 0 8px 0 0;
  margin-bottom: 14px;
}

/* Footer part */
.footer {
  height: 40px;
  width: 100%;
  padding: 0 25px;
  display: flex;
  position: relative;
  bottom: 40px;
  align-items: center;
  justify-content: space-between;
  z-index: 99;
}

.footer ion-button {
  width: 80%;
  height: 40px;
}
.footer-left {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.footer-left .save-icon {
  font-size: 1.3rem;
  color: #616161;
}
.footer-left span {
  display: block;
  font-size: 15px;
  color: #999;
}

.character-show {
  display: flex;
  box-sizing: border-box;
  position: absolute;
  right: 8px;
  top: 10.5px;
  color: #b3b3b3;
  font-size: 0.9rem;
  z-index: 2;
}
</style>
