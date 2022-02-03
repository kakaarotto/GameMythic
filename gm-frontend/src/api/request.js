import axios from "axios";

// Adding request interceptor
axios.interceptors.request.use(
  function(config) {
    let token = localStorage.getItem("authenticationToken");
    config.headers.Authorization = "Bearer " + token;
    return config;
  },
  function(error) {
    // Request error
    return Promise.reject(error);
  }
);

// Adding response interceptor
axios.interceptors.response.use(
  function(response) {
    if (response.status === 204) {
    }
    return response;
  },
  function(error) {
    console.log('response error')
    console.log(error)
    // Response error
    if (!error.response || !error.response.data) {
      return Promise.reject({ code: -1, message: error});
    }
    return Promise.reject(error);
  }
);

export default axios;
