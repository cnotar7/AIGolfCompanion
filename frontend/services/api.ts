import axios from "axios";

const API_BASE = "http://192.168.0.155:8080/golfcompanion";
// replace with your machine IP when testing on device

export const api = axios.create({
  baseURL: API_BASE,
  timeout: 5000,
});
