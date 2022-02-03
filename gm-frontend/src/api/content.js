import request from "./request";

// Get home page content
export function getContents(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/contents`,
    method: "get",
    params: {
      ...params,
    },
  });
}
export function getNexPageContents(data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/contents-nextPage`,
    method: "post",
    data,
  });
}

// Get collection content
export function collectContents() {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/collect-contents`,
    method: "get"
  });
}

// Published content
export function createContent(data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/content`,
    method: "post",
    data: {
      ...data,
    },
  });
}

// Edit content
export function updateContent(id, data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/content/${id}`,
    method: "put",
    data: {
      ...data,
    },
  });
}

// Get content detail
export function getContent(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/content/${id}`,
    method: "get",
  });
}

// Like
export function good(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/content/${id}/good`,
    method: "post",
  });
}

// Collect
export function collect(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/content/${id}/collect`,
    method: "post",
  });
}

// Delete Content
export function deleteContent(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/content/${id}`,
    method: "delete",
  });
}

// Search content
export function searchByKeyword(keyword, size = 10) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/content/searchByKeyword`,
    method: "get",
    params: {
      keyword,
      size,
      page: 0
    }
  });
}

// Follow user
export function collectUser(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/${id}/collect`,
    method: "post",
  });
}

// Unfollow user
export function unCollectUser(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/account/${id}/un-collect`,
    method: "post",
  });
}