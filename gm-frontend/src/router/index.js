import { createRouter, createWebHistory } from "@ionic/vue-router";
import Tabs from "../views/Tabs.vue";

const routes = [
  {
    path: "/",
    component: () => import("../views/Index.vue"),
    name: "indexPage"
  },
  {
    path: "/login",
    component: () => import("../views/Login.vue"),
    name: "login"
  },
  {
    path: "/register",
    component: () => import("../views/Register.vue"),
    name: "register"
  },
  {
    path: "/registerAvatar",
    component: () => import("../views/RegisterAvatar.vue"),
    name: "registerAvatar"
  },
  {
    path: "/registerUsername",
    component: () => import("../views/ResgisterUsername.vue"),
    name: "registerUsername"
  },
  {
    path: "/selectChannel",
    component: () => import("../views/SelectChannel.vue"),
    name: "selectChannel",
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/publish",
    component: () => import("../views/Publish.vue"),
    meta: {
      // Cached pages
      keepAlive: true
    },
    name: 'publish'
  },
  {
    path: "/detail",
    component: () => import("../views/Detail.vue"),
    name: 'detail',
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/search",
    component: () => import("../views/Search.vue"),
    name: "searchPage",
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/allChannles",
    component: () => import("../views/AllChannels.vue"),
    name: "allChannles",
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/mySubChannles",
    component: () => import("../views/MySubChannles.vue"),
    name: "mySubChannles",
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/channleDetail/",
    component: () => import("../views/ChannleDetail.vue"),
    name: 'channleDetail',
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/notificationDetail",
    component: () => import("../views/NotificationDetail.vue"),
    name: "notificationDetail",
    meta: {
      keepAlive: false
    }

  },
  {
    path: "/setting",
    component: () => import("../views/Setting.vue"),
    name: "setting",
    meta: {
      keepAlive: false
    }

  },
  {
    path: "/following&follower",
    component: () => import("../views/Following&Follower.vue"),
    name: 'following&follower',
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/usernameEdit",
    component: () => import("../views/UsernameEdit.vue"),
    name: 'usernameEdit',
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/passwordEdit",
    component: () => import("../views/PasswordEdit.vue"),
    name: 'passwordEdit',
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/notificationSwitch",
    component: () => import("../views/NotificationSwitch.vue"),
    name: 'notificationSwitch',
    meta: {
      keepAlive: false
    }
  },
  {
    path: "/tabs/",
    component: Tabs,
    children: [
      {
        path: "",
        redirect: "/tabs/home",
      },
      {
        path: "home",
        component: () => import("@/views/Home.vue"),
        name: 'home',
        meta: {
          keepAlive: true
        }
      },
      {
        path: "channels",
        component: () => import("@/views/Channels.vue"),
        name: 'channels',
        meta: {
          keepAlive: false
        }
      },
      {
        path: "notification",
        component: () => import("@/views/Notification.vue"),
        name: 'notification',
        meta: {
          keepAlive: false
        }

      },
      {
        path: "profile",
        component: () => import("@/views/Profile.vue"),
        name: 'profile',
        meta: {
          keepAlive: false
        }

      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});
export default router;
