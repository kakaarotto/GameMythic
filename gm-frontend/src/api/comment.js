import request from "./request";

// Post comment message
export function createData(data) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/comment`,
    method: "post",
    data: {
      ...data,
    },
  });
}
