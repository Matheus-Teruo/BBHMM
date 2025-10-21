import axios from "axios";
import { useEffect } from "react";

const api = axios.create({
  baseURL: import.meta.env.VITE_BACKEND_BASE_URL || "http://localhost:8080/",
  withCredentials: true,
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
  },
});

function useAxios() {
  useEffect(() => {
    const requestInterceptor = api.interceptors.request.use((config) => {
      return config;
    });

    return () => {
      api.interceptors.request.eject(requestInterceptor);
    };
  }, []);

  return api;
}

export default useAxios;
