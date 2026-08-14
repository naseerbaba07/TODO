import axios from "axios";

const API = axios.create({
  baseURL: "https://todo-1-fy7b.onrender.com/todos"
});
export const CHAT_API =
    "https://todo-1-fy7b.onrender.com/chat";

export default API;
