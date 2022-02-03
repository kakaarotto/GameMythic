import request from "./request";

// Get user info
export function getUserInfo() {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account`,
    method: "get",
  });
}

// Update username
export function updatePassword(data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/change-password`,
    method: "post",
    data,
  });
}

// Update username
export function updateUsername(username) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/updateUsername`,
    method: "post",
    params: {
      username,
    },
  });
}
// Update user avatar
export function updateAvatar(avatar) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/updateAvatar`,
    method: "post",
    params: {
      avatar,
    },
  });
}
// Update user notification
export function updateNotificationEnabled(enabled) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/updateNotificationEnabled`,
    method: "post",
    params: {
      enabled,
    },
  });
}
// Get My following
export function getMyFollowing() {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/myFollowing`,
    method: "get",
  });
}
// Get my followers
export function getMyFollowers() {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/myFollowers`,
    method: "get",
  });
}
// Get my follow count
export function getMyFollowCount() {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/follow-count`,
    method: "get",
  });
}