import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { IonicVue, toastController, alertController, actionSheetController, loadingController } from "@ionic/vue";

/* Core CSS required for Ionic components to work properly */
import "@ionic/vue/css/core.css";

/* Basic CSS for apps built with Ionic */
import "@ionic/vue/css/normalize.css";
import "@ionic/vue/css/structure.css";
import "@ionic/vue/css/typography.css";

/* Optional CSS utils that can be commented out */
import "@ionic/vue/css/padding.css";
import "@ionic/vue/css/float-elements.css";
import "@ionic/vue/css/text-alignment.css";
import "@ionic/vue/css/text-transformation.css";
import "@ionic/vue/css/flex-utils.css";
import "@ionic/vue/css/display.css";

/* Theme variables */
import "./theme/variables.css";
import "./common/style.css";

/* Axios */
import axios from "axios";

import mitt from "mitt"

const app = createApp(App)
  .use(IonicVue)
  .use(router);

// Set global axios
app.config.globalProperties.$axios = axios;

// Set global alert
app.config.globalProperties.$common = {
  toast: async function (message, duration = 2000, position = 'top', color = 'success') {
    let toastCtl = await toastController.create({
      message,
      duration,
      position,
      color
    });
    return toastCtl.present();
  },
  alert: async function (message) {
    let obj = typeof message === 'string' ? {
      message
    } : message;
    let alertCtl = await alertController.create(obj);
    return alertCtl.present();
  },
  actionSheet: async function (options) {
    let actionSheet = await actionSheetController.create(options);
    return actionSheet.present();
  },
  error: async function (message, header = 'Error') {
    let alertCtl = await alertController.create({
      cssClass: 'my-custom-class',
      header,
      message,
      buttons: ['OK']
    });
    return alertCtl.present();
  },
  loading: null,
  showLoading: async function (message = "Loading ...") {
    this.loading = await loadingController.create({
      cssClass: 'my-custom-class',
      message
    });
    await this.loading.present();
  },
  hideLoading: async function () {
    if (this.loading) await this.loading.dismiss();
  }
}

app.config.globalProperties.$mybus = new mitt()


router.isReady().then(() => {
  app.mount("#app");
});
