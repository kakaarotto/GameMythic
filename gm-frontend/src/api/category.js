import request from "./request";

// Get list of categories
export function getCategories(params) {
  return request({
    url: `${process.env.VUE_APP_BASE_URL}/api/categories`,
    method: "get",
    params: {
      ...params,
    },
  });
}
