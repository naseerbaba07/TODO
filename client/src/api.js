import axios from "axios";

const API = axios.create({
  baseURL: "https://todo-1-avon.onrender.com/todos"
});

export default API;
