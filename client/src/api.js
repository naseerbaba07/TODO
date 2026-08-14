import axios from "axios";

const API = axios.create({
  baseURL: "https://todo-1-avon.onrender.com/todos"
});
export const CHAT_API =
    "https://todo-1-avon.onrender.com/chat";

export default API;
