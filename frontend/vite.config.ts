import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import * as path from "path";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  envDir: "../",
  resolve: {
    alias: {
      "@styles": path.resolve(__dirname, "./src/styles"),
      "@data": path.resolve(__dirname, "./src/data"),
      "@context": path.resolve(__dirname, "./src/context"),
      "@reducer": path.resolve(__dirname, "./src/reducer"),
      "@service": path.resolve(__dirname, "./src/service"),
      "@": path.resolve(__dirname, "./src"),
    },
  },
})
