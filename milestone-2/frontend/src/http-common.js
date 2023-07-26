import axios from "axios";
import { REST_HOST } from "./constants";

export default axios.create({
  baseURL: "http://" + REST_HOST + ":8080/api",
  headers: {
    "Content-type": "application/json"
  }
});