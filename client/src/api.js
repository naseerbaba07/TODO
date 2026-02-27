import axios from "axios";

const API = axios.create({
  baseURL: "https://todos-s9ie.onrender.com"
});

export default API;
