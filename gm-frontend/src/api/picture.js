import axios from "axios";

// Upload images
export const uploadPicture = (data) => {
  let config = {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  };
  return axios.post(`${process.env.VUE_APP_Upload_URL}/up-img`, data, config);
};
