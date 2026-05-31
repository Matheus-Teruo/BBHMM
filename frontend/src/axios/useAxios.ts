import axios from "axios";
import { useEffect } from "react";

const api = axios.create({
  baseURL: "/api",
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
