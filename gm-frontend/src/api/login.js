import request from "./request";

// Login
export function login(data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/authenticate`,
    method: "post",
    data: {
      ...data,
    },
  });
}

// Register
export function register(data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/register`,
    method: "post",
    data: {
      ...data,
    },
  });
}

// Find account
export function findAccount(username) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/findAccount`,
    method: "get",
    params: {
      username
    },
  });
}

// Find email
export function findEmail(email) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/findEmail`,
    method: "get",
    params: {
      email
    },
  });
}