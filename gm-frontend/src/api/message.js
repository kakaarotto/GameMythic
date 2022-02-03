import request from "./request";

// Get list of messages
export function getMessages(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/messages`,
    method: "get",
    params: {
      ...params,
    },
  });
}

// Set read status
export function updateRead(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/message-read/${id}`,
    method: "put"
  });
}
// Set read status
export function updateReadMultiple(data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/message-read-multiple`,
    method: "post",
    data: {
      ...data
    }
  });
}

// Delte message
export function deleteMessage(id) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/message/${id}`,
    method: "delete",
  });
}

// Get Message count
export function getMessageCount() {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/message-count`,
    method: "get",
  });
}