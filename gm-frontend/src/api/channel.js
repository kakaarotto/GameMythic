import request from "./request";

// Get channels info
export function getChannels(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels`,
    method: "get",
    params: {
      ...params,
    },
  });
}

// Get channel details info
export function getChannelsDetail(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels/${id}`,
    method: "get",
  });
}

// Get channel categories
export function getChannelsCategories(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-categories`,
    method: "get",
    params: {
      ...params,
    },
  });
}

// Get list of subscribed channels
export function getChannelsSubscription(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-subscription`,
    method: "get",
    params: {
      ...params,
    },
  });
}

// Get list of channel subscription
export function addChannelsSubscription(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-subscription/${id}`,
    method: "post"
  })
}

// Delete channel subscription
export function deleteChannelsSubscription(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-subscription/${id}`,
    method: "delete"
  })
}

// Get list of channels browsing history
export function getChannelsHistory(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-history`,
    method: "get",
    params: {
      ...params,
    },
  });
}

// Add browsing history channel
export function addChannelsHistory(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-history/${id}`,
    method: "post"
  })
}

// Delete channel browsing history
export function deleteChannelsHistory(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-history/${id}`,
    method: "delete"
  })
}

// Get list of popular channels
export function getPopularChannels(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/channels-contents`,
    method: "get",
    params: {
      ...params,
    },
  });
}